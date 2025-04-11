package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.MasterService;


@RequestMapping("/master")
@RestController
@CrossOrigin
public class MasterController {

	@Autowired
	MasterService masterService;


////////Get Master Department List ///////////////////////////////

@RequestMapping(value = "/getDepartmentList", method = RequestMethod.GET)
public String departmentList(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
	return masterService.departmentList(jsondata, request);
}

////////////////// Get Master ICD List /////////////////////////////

@RequestMapping(value = "/getICDList", method = RequestMethod.GET)
public String getICD(@RequestBody HashMap<String, String> jsondata, HttpServletRequest request) {
	return masterService.getICD(jsondata, request);
}

/**
* @author rajdeo.kumar
* @param request
* @return
*/
@RequestMapping(value = "/getAllStates", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllStates(@RequestBody HashMap<String, String> stateDataPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String stateData = "";
	JSONObject jsonObject = new JSONObject(stateDataPayload);
	stateData = masterService.getAllStates(jsonObject, request, response);
	return stateData;
}


/*************************************
* Mas Command
***************************************************************/
/**
* 
* @param request
* @param response
* @return
*/

@RequestMapping(value = "/addCommand", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addCommand(@RequestBody Map<String, Object> command, HttpServletRequest request,
		HttpServletResponse response) {
	String addCmd = "";
	JSONObject json = new JSONObject(command);
	addCmd = masterService.addCommand(json, request, response);
	return addCmd;
}

@RequestMapping(value = "/getAllCommand", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllCommand(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String cmd = "";
	JSONObject jsonObj = new JSONObject(payload);
	cmd = masterService.getAllCommand(jsonObj, request, response);
	return cmd;
}

@RequestMapping(value = "/getCommand", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getCommand(@RequestBody HashMap<String, Object> command, HttpServletRequest request) {
	String cmd = "";
	cmd = masterService.getCommand(command, request);
	return cmd;
}

@RequestMapping(value = "/updateCommand", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateCommand(@RequestBody HashMap<String, Object> command, HttpServletRequest request,
		HttpServletResponse response) {
	String updateCmd = "";
	updateCmd = masterService.updateCommand(command, request, response);
	return updateCmd;
}

@RequestMapping(value = "/statusCommand", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusCommand(@RequestBody HashMap<String, Object> command, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusCommand(command, request, response);
	return status;
}

@RequestMapping(value = "/getCommandTypeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getCommandTypeList(HttpServletRequest request, HttpServletResponse response) {
	String addCmdTyp = "";
	addCmdTyp = masterService.getCommandTypeList(request, response);
	return addCmdTyp;
}

/************************************
* UNIT MASTER
*********************************************/
@RequestMapping(value = "/getAllUnit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllUnit(@RequestBody Map<String, Object> unitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String unit = "";
	JSONObject jsonObj = new JSONObject(unitPayload);
	unit = masterService.getAllUnit(jsonObj, request, response);
	return unit;
}

@RequestMapping(value = "/getCommandList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getCommandList(HttpServletRequest request, HttpServletResponse response) {
	String commandList = "";
	commandList = masterService.getCommandList(request, response);
	return commandList;
}

@RequestMapping(value = "/addUnit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addUnits(@RequestBody Map<String, Object> unitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addunit = "";
	JSONObject jsonObject = new JSONObject(unitPayload);
	addunit = masterService.addUnits(jsonObject, request, response);
	return addunit;
}

@RequestMapping(value = "/getUnitTypeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getUnitTypeList(HttpServletRequest request, HttpServletResponse response) {
	String unitTyp = "";
	unitTyp = masterService.getUnitTypeList(request, response);
	return unitTyp;
}

@RequestMapping(value = "/updateUnit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateUnit(@RequestBody HashMap<String, Object> unitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String updateUnit = "";
	updateUnit = masterService.updateUnit(unitPayload, request, response);
	return updateUnit;
}

@RequestMapping(value = "/updateStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateStatus(@RequestBody HashMap<String, Object> unitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String updtStatus = "";
	JSONObject jsonObject = new JSONObject(unitPayload);
	updtStatus = masterService.updateStatus(jsonObject, request, response);
	return updtStatus;
}

/*****************************************
* MAS HOSPITAL
********************************************************************/

@RequestMapping(value = "/addMasHospital", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addMasHospital(@RequestBody Map<String, Object> hospitalPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addHospital = "";
	JSONObject jsonObject = new JSONObject(hospitalPayload);
	addHospital = masterService.addMasHospital(jsonObject, request, response);
	return addHospital;
}

@RequestMapping(value = "/getUnitNameList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getUnitNameList(HttpServletRequest request, HttpServletResponse response) {
	String unitNameList = "";
	unitNameList = masterService.getUnitNameList(request, response);
	return unitNameList;
}

@RequestMapping(value = "/getAllHospital", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllHospital(@RequestBody Map<String, Object> hospitalPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String hospitalList = "";
	JSONObject jsonObject = new JSONObject(hospitalPayload);
	hospitalList = masterService.getAllHospital(jsonObject, request, response);

	return hospitalList;
}

@RequestMapping(value = "/updateHospitalMasterStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateHospitalMasterStatus(@RequestBody Map<String, Object> hospitalPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateHospital = "";
	JSONObject jsonObject = new JSONObject(hospitalPayload);
	updateHospital = masterService.updateHospitalMasterStatus(jsonObject, request, response);
	return updateHospital;
}

@RequestMapping(value = "/updateHospitalDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateHospitalDetails(@RequestBody Map<String, Object> hospitalPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String updateHospital = "";
	JSONObject jsonObject = new JSONObject(hospitalPayload);
	updateHospital = masterService.updateHospitalDetails(jsonObject, request, response);
	return updateHospital;

}

/**********************************************
* MAS RELATION
*********************************************************************/
@RequestMapping(value = "/getAllRelation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllRelation(@RequestBody Map<String, Object> masRelation, HttpServletRequest request,
		HttpServletResponse response) {
	String unit = "";
	JSONObject jsonObject = new JSONObject(masRelation);
	unit = masterService.getAllRelation(jsonObject, request, response);
	return unit;
}

@RequestMapping(value = "/updateRelationDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRelationDetails(@RequestBody HashMap<String, Object> masRelation, HttpServletRequest request,
		HttpServletResponse response) {
	String updateRel = "";
	JSONObject jsonObject = new JSONObject(masRelation);
	updateRel = masterService.updateRelationDetails(jsonObject, request, response);
	return updateRel;
}

@RequestMapping(value = "/updateRelationStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRelationStatus(@RequestBody HashMap<String, Object> masRelation, HttpServletRequest request,
		HttpServletResponse response) {
	String updateRelStatus = "";
	JSONObject jsonObject = new JSONObject(masRelation);
	updateRelStatus = masterService.updateRelationStatus(jsonObject, request, response);
	return updateRelStatus;
}

@RequestMapping(value = "/addRelation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addRelation(@RequestBody Map<String, Object> relationPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addRelation = "";
	JSONObject jsonObject = new JSONObject(relationPayload);
	addRelation = masterService.addRelation(jsonObject, request, response);
	return addRelation;
}

/********************************************
* MAS DISPOSAL
************************************************************/
@RequestMapping(value = "/addDisposal", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addDisposal(@RequestBody Map<String, Object> disposalPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addDisposl = "";
	JSONObject jsonObject = new JSONObject(disposalPayload);
	addDisposl = masterService.addDisposal(jsonObject, request, response);
	return addDisposl;
}

@RequestMapping(value = "/getAllDisposal", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllDisposal(@RequestBody Map<String, Object> masDisposal, HttpServletRequest request,
		HttpServletResponse response) {
	String alldisposal = "";
	JSONObject jsonObject = new JSONObject(masDisposal);
	alldisposal = masterService.getAllDisposal(jsonObject, request, response);
	return alldisposal;
}

@RequestMapping(value = "/updateDisposalDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateDisposalDetails(@RequestBody HashMap<String, Object> masDisposal, HttpServletRequest request,
		HttpServletResponse response) {
	String updateDispo = "";
	JSONObject jsonObject = new JSONObject(masDisposal);
	updateDispo = masterService.updateDisposalDetails(jsonObject, request, response);
	return updateDispo;
}

@RequestMapping(value = "/updateDisposalStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateDisposalStatus(@RequestBody HashMap<String, Object> masDisposal, HttpServletRequest request,
		HttpServletResponse response) {
	String updateDispoStatus = "";
	JSONObject jsonObject = new JSONObject(masDisposal);
	updateDispoStatus = masterService.updateDisposalStatus(jsonObject, request, response);
	return updateDispoStatus;
}

/****************************************
* MAS APPOINTMENT TYPE
***********************************************************/

@RequestMapping(value = "/addAppointmentType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addAppointmentType(@RequestBody Map<String, Object> appointmentTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String addAppointment = "";
	JSONObject jsonObject = new JSONObject(appointmentTypePayload);
	addAppointment = masterService.addAppointmentType(jsonObject, request, response);
	return addAppointment;
}

@RequestMapping(value = "/getAllAppointmentType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllAppointmentType(@RequestBody Map<String, Object> appointmentTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String alldisposal = "";
	JSONObject jsonObject = new JSONObject(appointmentTypePayload);
	alldisposal = masterService.getAllAppointmentType(jsonObject, request, response);
	return alldisposal;
}

@RequestMapping(value = "/updateAppointmentTypeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateAppointmentTypeDetails(@RequestBody HashMap<String, Object> appointmentTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDispo = "";
	JSONObject jsonObject = new JSONObject(appointmentTypePayload);
	updateDispo = masterService.updateAppointmentTypeDetails(jsonObject, request, response);
	return updateDispo;
}

@RequestMapping(value = "/updateAppointmentTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateAppointmentTypeStatus(@RequestBody HashMap<String, Object> appointmentTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDispoStatus = "";
	JSONObject jsonObject = new JSONObject(appointmentTypePayload);
	updateDispoStatus = masterService.updateAppointmentTypeStatus(jsonObject, request, response);
	return updateDispoStatus;
}

/*****************************
* MAS DEPARTMENT
*****************************************************/

@RequestMapping(value = "/getAllDepartment", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllDepartment(@RequestBody Map<String, Object> departmentPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allDept = "";
	JSONObject jsonObject = new JSONObject(departmentPayload);
	allDept = masterService.getAllDepartment(jsonObject, request, response);
	return allDept;
}

@RequestMapping(value = "/getDepartmentTypeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getDepartmentTypeList(@RequestBody Map<String, Object> departmentPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String deptType = "";
	JSONObject jsonObject = new JSONObject(departmentPayload);
	deptType = masterService.getDepartmentTypeList(jsonObject, request, response);
	return deptType;
}

@RequestMapping(value = "/addDepartment", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addDepartment(@RequestBody Map<String, Object> departmentPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addDepart = "";
	JSONObject jsonObject = new JSONObject(departmentPayload);
	addDepart = masterService.addDepartment(jsonObject, request, response);
	return addDepart;
}

@RequestMapping(value = "/updateDepartmentDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateDepartmentDetails(@RequestBody HashMap<String, Object> departmentPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDepart = "";
	JSONObject jsonObject = new JSONObject(departmentPayload);
	updateDepart = masterService.updateDepartmentDetails(jsonObject, request, response);
	return updateDepart;
}

@RequestMapping(value = "/updateDepartmentStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateDepartmentStatus(@RequestBody HashMap<String, Object> departmentPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDeptStatus = "";
	JSONObject jsonObject = new JSONObject(departmentPayload);
	updateDeptStatus = masterService.updateDepartmentStatus(jsonObject, request, response);
	return updateDeptStatus;
}

/*********************************************
* MAS FREQUENCY
********************************************************/
@RequestMapping(value = "/getAllOpdFrequency", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllOpdFrequency(@RequestBody Map<String, Object> frequencyPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allDept = "";
	JSONObject jsonObject = new JSONObject(frequencyPayload);
	allDept = masterService.getAllOpdFrequency(jsonObject, request, response);
	return allDept;
}

@RequestMapping(value = "/addOpdFrequency", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addOpdFrequency(@RequestBody Map<String, Object> frequencyPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addDepart = "";
	//JSONObject jsonObject = new JSONObject(frequencyPayload);
	addDepart = masterService.addOpdFrequency(frequencyPayload, request, response);
	return addDepart;
}

@RequestMapping(value = "/updateOpdFrequencyDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateOpdFrequencyDetails(@RequestBody HashMap<String, Object> frequencyPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDepart = "";
	//JSONObject jsonObject = new JSONObject(frequencyPayload);
	updateDepart = masterService.updateOpdFrequencyDetails(frequencyPayload, request, response);
	return updateDepart;
}

@RequestMapping(value = "/updateOpdFrequencyStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateOpdFrequencyStatus(@RequestBody HashMap<String, Object> frequencyPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDeptStatus = "";
	JSONObject jsonObject = new JSONObject(frequencyPayload);
	updateDeptStatus = masterService.updateOpdFrequencyStatus(jsonObject, request, response);
	return updateDeptStatus;
}

/********************************
* MAS EMPANELLED Hospital Master
********************************************************/

@RequestMapping(value = "/getAllEmpanelledHospital", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllEmpanelledHospital(@RequestBody Map<String, Object> impanneledHospPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String allempHosp = "";
	JSONObject jsonObject = new JSONObject(impanneledHospPayload);
	allempHosp = masterService.getAllEmpanelledHospital(jsonObject, request, response);
	return allempHosp;
}

@RequestMapping(value = "/addEmpanelledHospital", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addEmpanelledHospital(@RequestBody String impanneledHospPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String allempHosp = "";
	allempHosp = masterService.addEmpanelledHospital(impanneledHospPayload, request, response);
	return allempHosp;
}

@RequestMapping(value = "/updateEmpanelledHospital", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateEmpanelledHospital(@RequestBody HashMap<String, Object> impanneledHospPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDepart = "";
	JSONObject jsonObject = new JSONObject(impanneledHospPayload);
	updateDepart = masterService.updateEmpanelledHospital(jsonObject, request, response);
	return updateDepart;
}

@RequestMapping(value = "/getHospitalListByRegion", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getHospitalListByRegion(HttpServletRequest request, HttpServletResponse response) {
	String getHospital = "";
	getHospital = masterService.getHospitalListByRegion(request, response);
	return getHospital;
}


/**********************************
* Phsiotherapy/Procedure         *
**********************************/

@RequestMapping(value = "/getAllPhysiotherapy", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllPhysiotherapy(@RequestBody Map<String, Object> medExamPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String physiotherapyList = "";
	JSONObject jsonObject = new JSONObject(medExamPayload);
	physiotherapyList = masterService.getAllPhsiotherapy(jsonObject, request, response);

	return physiotherapyList;
}

@RequestMapping(value = "/addPhsiotherapyCare", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addPhsiotherapyCare(@RequestBody Map<String, Object> PhsiotherapyPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addPhsiotherapy = "";
	JSONObject jsonObject = new JSONObject(PhsiotherapyPayload);
	addPhsiotherapy = masterService.addPhsiotherapy(jsonObject, request, response);
	return addPhsiotherapy;
}

@RequestMapping(value = "/updatePhysiotherapyDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updatePhysiotherapyDetails(@RequestBody HashMap<String, Object> physiotherapyPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updatePhysiotherapyDetails = "";
	JSONObject jsonObject = new JSONObject(physiotherapyPayload);
	updatePhysiotherapyDetails = masterService.updatePhysiotherapyDetails(jsonObject, request, response);
	return updatePhysiotherapyDetails;
}

/**********************************
* MAS IDEAL WEIGHT
******************************************************/

@RequestMapping(value = "/getAllIdealWeight", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllIdealWeight(@RequestBody Map<String, Object> idealweightPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String getIdealWeight = "";
	JSONObject jsonObject = new JSONObject(idealweightPayload);
	getIdealWeight = masterService.getAllIdealWeight(jsonObject, request, response);
	return getIdealWeight;
}

@RequestMapping(value = "/getAge", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAge(@RequestBody Map<String, Object> idealweightPayload, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject jsonObject = new JSONObject(idealweightPayload);
	String resObject = masterService.getAge(jsonObject, request, response);
	return resObject;
}

@RequestMapping(value="/updateIdealWeight", method=RequestMethod.POST, produces="application/json", consumes="application/json")
public String updateIdealWeight(@RequestBody Map<String, Object> idealweightPayload, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject jsonObject = new JSONObject(idealweightPayload);
	return masterService.updateIdealWeight(jsonObject, request, response);
}

@RequestMapping(value="/addIdealWeight", method=RequestMethod.POST, produces="application/json", consumes="application/json")
public String addIdealWeight(@RequestBody Map<String, Object> idealweightPayload, HttpServletRequest request,
		HttpServletResponse response) {
	JSONObject jsonObject = new JSONObject(idealweightPayload);
	return masterService.addIdealWeight(jsonObject, request, response);
}

/*************************************
* Mas Service Type
***************************************************************/
/**
* 
* @param request
* @param response
* @return
*/

@RequestMapping(value = "/getAllServiceType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllServiceType(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String sType = "";
	JSONObject jsonObj = new JSONObject(payload);
	sType = masterService.getAllServiceType(jsonObj, request, response);
	return sType;
}

@RequestMapping(value = "/updateServiceType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateServiceType(@RequestBody HashMap<String, Object> serviceType, HttpServletRequest request,
		HttpServletResponse response) {
	String updateServiceType = "";
	updateServiceType = masterService.updateServiceType(serviceType, request, response);
	return updateServiceType;
}

@RequestMapping(value = "/addServiceType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addServiceType(@RequestBody Map<String, Object> stype, HttpServletRequest request,
		HttpServletResponse response) {
	String addStype = "";
	JSONObject json = new JSONObject(stype);
	addStype = masterService.addServiceType(json, request, response);
	return addStype;
}

@RequestMapping(value = "/statusServiceType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusServiceType(@RequestBody HashMap<String, Object> serviceType, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusServiceType(serviceType, request, response);
	return status;
}

/***********************************
* Rank Master
*************************************/
/**
* @param request
* @param response
* @return
*/

@RequestMapping(value = "/addRank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addRank(@RequestBody HashMap<String, Object> rank, HttpServletRequest request,
		HttpServletResponse response) {
	String addRank = "";
	JSONObject json = new JSONObject(rank);
	addRank = masterService.addRank(json, request, response);
	return addRank;
}

@RequestMapping(value = "/getAllRank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllRank(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String rank = "";
	JSONObject jsonObj = new JSONObject(payload);
	rank = masterService.getAllRank(jsonObj, request, response);
	return rank;
}

@RequestMapping(value = "/getRank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getRank(@RequestBody HashMap<String, Object> rank, HttpServletRequest request) {
	String rank1 = "";
	rank1 = masterService.getRank(rank, request);
	return rank1;
}

@RequestMapping(value = "/updateRank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRank(@RequestBody HashMap<String, Object> rank, HttpServletRequest request,
		HttpServletResponse response) {
	String updateRank = "";
	updateRank = masterService.updateRank(rank, request, response);
	return updateRank;
}

@RequestMapping(value = "/statusRank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusRank(@RequestBody HashMap<String, Object> rank, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusRank(rank, request, response);
	return status;
}

@RequestMapping(value = "/getEmployeeCategoryList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getEmployeeCategoryList(HttpServletRequest request, HttpServletResponse response) {
	String addEmployeeCategory = "";

	addEmployeeCategory = masterService.getEmployeeCategoryList(request, response);
	return addEmployeeCategory;
}

/*************************************************
* TRADE MASTER
*******************************************************************/

@RequestMapping(value = "/addTrade", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addTrade(@RequestBody HashMap<String, Object> trade, HttpServletRequest request,
		HttpServletResponse response) {
	String addTrade = "";
	JSONObject json = new JSONObject(trade);
	addTrade = masterService.addTrade(json, request, response);
	return addTrade;
}

@RequestMapping(value = "/getAllTrade", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllTrade(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String trade = "";
	JSONObject jsonObj = new JSONObject(payload);
	trade = masterService.getAllTrade(jsonObj, request, response);
	return trade;
}

@RequestMapping(value = "/getTrade", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getTrade(@RequestBody HashMap<String, Object> trade, HttpServletRequest request) {
	String trade1 = "";
	trade1 = masterService.getTrade(trade, request);
	return trade1;
}

@RequestMapping(value = "/updateTrade", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateTrade(@RequestBody HashMap<String, Object> trade, HttpServletRequest request,
		HttpServletResponse response) {
	String updateTrade = "";
	updateTrade = masterService.updateTrade(trade, request, response);
	return updateTrade;
}

@RequestMapping(value = "/statusTrade", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusTrade(@RequestBody HashMap<String, Object> trade, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusTrade(trade, request, response);
	return status;
}

@RequestMapping(value = "/getServiceTypeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getServiceTypeList(HttpServletRequest request, HttpServletResponse response) {
	String addServiceType = "";

	addServiceType = masterService.getServiceTypeList(request, response);
	return addServiceType;
}

/*********************************************
* MAS RELIGION
********************************************************/
@RequestMapping(value = "/getAllReligion", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllReligion(@RequestBody Map<String, Object> religionPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allDept = "";
	JSONObject jsonObject = new JSONObject(religionPayload);
	allDept = masterService.getAllReligion(jsonObject, request, response);
	return allDept;
}

@RequestMapping(value = "/addReligion", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addReligion(@RequestBody Map<String, Object> religionPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addDepart = "";
	JSONObject jsonObject = new JSONObject(religionPayload);
	addDepart = masterService.addReligion(jsonObject, request, response);
	return addDepart;
}

@RequestMapping(value = "/updateReligionDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateReligionDetails(@RequestBody HashMap<String, Object> religionPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDepart = "";
	JSONObject jsonObject = new JSONObject(religionPayload);
	updateDepart = masterService.updateReligionDetails(jsonObject, request, response);
	return updateDepart;
}

@RequestMapping(value = "/updateReligionStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateReligionStatus(@RequestBody HashMap<String, Object> religionPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateDeptStatus = "";
	JSONObject jsonObject = new JSONObject(religionPayload);
	updateDeptStatus = masterService.updateReligionStatus(jsonObject, request, response);
	return updateDeptStatus;
}

/*********************************************
* MAS MARITAL STATUS
********************************************************/
@RequestMapping(value = "/getAllMaritalStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllMaritalStatus(@RequestBody Map<String, Object> maritalStatusPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allMaritalStatus = "";
	JSONObject jsonObject = new JSONObject(maritalStatusPayload);
	allMaritalStatus = masterService.getAllIdentificationType(jsonObject, request, response);
	return allMaritalStatus;
}

@RequestMapping(value = "/addMaritalStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addMaritalStatus(@RequestBody Map<String, Object> maritalStatusPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addMaritalStatus = "";
	JSONObject jsonObject = new JSONObject(maritalStatusPayload);
	addMaritalStatus = masterService.addIdentificationType(jsonObject, request, response);
	return addMaritalStatus;
}

@RequestMapping(value = "/updateMaritalStatusDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMaritalStatusDetails(@RequestBody HashMap<String, Object> maritalStatusPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMaritalStatus = "";
	JSONObject jsonObject = new JSONObject(maritalStatusPayload);
	updateMaritalStatus = masterService.updateIdentificationType(jsonObject, request, response);
	return updateMaritalStatus;
}

@RequestMapping(value = "/updateMaritalStatusStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMaritalStatusStatus(@RequestBody HashMap<String, Object> maritalStatusPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMaritalStatusStatus = "";
	JSONObject jsonObject = new JSONObject(maritalStatusPayload);
	updateMaritalStatusStatus = masterService.updateIdentificationTypeStatus(jsonObject, request, response);
	return updateMaritalStatusStatus;
}

/*********************************************
* MAS Employee Category
********************************************************/
@RequestMapping(value = "/getAllEmployeeCategory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllEmployeeCategory(@RequestBody Map<String, Object> employeeCategoryPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allEmployeeCategory = "";
	JSONObject jsonObject = new JSONObject(employeeCategoryPayload);
	allEmployeeCategory = masterService.getAllEmployeeCategory(jsonObject, request, response);
	return allEmployeeCategory;
}

@RequestMapping(value = "/addEmployeeCategory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addEmployeeCategory(@RequestBody Map<String, Object> employeeCategoryPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addEmployeeCategory = "";
	JSONObject jsonObject = new JSONObject(employeeCategoryPayload);
	addEmployeeCategory = masterService.addEmployeeCategory(jsonObject, request, response);
	return addEmployeeCategory;
}

@RequestMapping(value = "/updateEmployeeCategoryDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateEmployeeCategoryDetails(@RequestBody HashMap<String, Object> employeeCategoryPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateEmployeeCategory = "";
	JSONObject jsonObject = new JSONObject(employeeCategoryPayload);
	updateEmployeeCategory = masterService.updateEmployeeCategoryDetails(jsonObject, request, response);
	return updateEmployeeCategory;
}

@RequestMapping(value = "/updateEmployeeCategoryStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateEmployeeCategoryStatus(@RequestBody HashMap<String, Object> employeeCategoryPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateEmployeeCategoryStatus = "";
	JSONObject jsonObject = new JSONObject(employeeCategoryPayload);
	updateEmployeeCategoryStatus = masterService.updateEmployeeCategoryStatus(jsonObject, request, response);
	return updateEmployeeCategoryStatus;
}

/*********************************************
* MAS Administrative Sex
********************************************************/
@RequestMapping(value = "/getAllAdministrativeSex", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllAdministrativeSex(@RequestBody Map<String, Object> administrativeSexPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allAdministrativeSex = "";
	JSONObject jsonObject = new JSONObject(administrativeSexPayload);
	allAdministrativeSex = masterService.getAllAdministrativeSex(jsonObject, request, response);
	return allAdministrativeSex;
}

@RequestMapping(value = "/addAdministrativeSex", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addAdministrativeSex(@RequestBody Map<String, Object> administrativeSexPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addAdministrativeSex = "";
	JSONObject jsonObject = new JSONObject(administrativeSexPayload);
	addAdministrativeSex = masterService.addAdministrativeSex(jsonObject, request, response);
	return addAdministrativeSex;
}

@RequestMapping(value = "/updateAdministrativeSexDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateAdministrativeSexDetails(@RequestBody HashMap<String, Object> administrativeSexPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateAdministrativeSex = "";
	JSONObject jsonObject = new JSONObject(administrativeSexPayload);
	updateAdministrativeSex = masterService.updateAdministrativeSexDetails(jsonObject, request, response);
	return updateAdministrativeSex;
}

@RequestMapping(value = "/updateAdministrativeSexStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateAdministrativeSexStatus(@RequestBody HashMap<String, Object> administrativeSexPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateAdministrativeSexStatus = "";
	JSONObject jsonObject = new JSONObject(administrativeSexPayload);
	updateAdministrativeSexStatus = masterService.updateAdministrativeSexStatus(jsonObject, request, response);
	return updateAdministrativeSexStatus;
}

/*********************************************
* MAS MedicalCategory
********************************************************/
@RequestMapping(value = "/getAllMedicalCategory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllMedicalCategory(@RequestBody Map<String, Object> medicalCategoryPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allMedicalCategory = "";
	JSONObject jsonObject = new JSONObject(medicalCategoryPayload);
	allMedicalCategory = masterService.getAllMedicalCategory(jsonObject, request, response);
	return allMedicalCategory;
}

@RequestMapping(value = "/addMedicalCategory", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addMedicalCategory(@RequestBody Map<String, Object> medicalCategoryPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addMedicalCategory = "";
	JSONObject jsonObject = new JSONObject(medicalCategoryPayload);
	addMedicalCategory = masterService.addMedicalCategory(jsonObject, request, response);
	return addMedicalCategory;
}

@RequestMapping(value = "/updateMedicalCategoryDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMedicalCategoryDetails(@RequestBody HashMap<String, Object> medicalCategoryPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMedicalCategory = "";
	JSONObject jsonObject = new JSONObject(medicalCategoryPayload);
	updateMedicalCategory = masterService.updateMedicalCategoryDetails(jsonObject, request, response);
	return updateMedicalCategory;
}

@RequestMapping(value = "/updateMedicalCategoryStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMedicalCategoryStatus(@RequestBody HashMap<String, Object> medicalCategoryPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMedicalCategoryStatus = "";
	JSONObject jsonObject = new JSONObject(medicalCategoryPayload);
	updateMedicalCategoryStatus = masterService.updateMedicalCategoryStatus(jsonObject, request, response);
	return updateMedicalCategoryStatus;
}

/*********************************************
* MAS BLOODGROUP
********************************************************/
@RequestMapping(value = "/getAllBloodGroup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllBloodGroup(@RequestBody Map<String, Object> bloodGroupPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allBloodGroup = "";
	JSONObject jsonObject = new JSONObject(bloodGroupPayload);
	allBloodGroup = masterService.getAllBloodGroup(jsonObject, request, response);
	return allBloodGroup;
}

@RequestMapping(value = "/addBloodGroup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addBloodGroup(@RequestBody Map<String, Object> bloodGroupPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addBloodGroup = "";
	JSONObject jsonObject = new JSONObject(bloodGroupPayload);
	addBloodGroup = masterService.addBloodGroup(jsonObject, request, response);
	return addBloodGroup;
}

@RequestMapping(value = "/updateBloodGroupDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateBloodGroupDetails(@RequestBody HashMap<String, Object> bloodGroupPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateBloodGroup = "";
	JSONObject jsonObject = new JSONObject(bloodGroupPayload);
	updateBloodGroup = masterService.updateBloodGroupDetails(jsonObject, request, response);
	return updateBloodGroup;
}

@RequestMapping(value = "/updateBloodGroupStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateBloodGroupStatus(@RequestBody HashMap<String, Object> bloodGroupPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateBloodGroupStatus = "";
	JSONObject jsonObject = new JSONObject(bloodGroupPayload);
	updateBloodGroupStatus = masterService.updateBloodGroupStatus(jsonObject, request, response);
	return updateBloodGroupStatus;
}


/*********************************************
* MAS Sample
********************************************************/
@RequestMapping(value = "/getAllSample", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllSample(@RequestBody Map<String, Object> samplePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allSample = "";
	JSONObject jsonObject = new JSONObject(samplePayload);
	allSample = masterService.getAllSample(jsonObject, request, response);
	return allSample;
}

@RequestMapping(value = "/addSample", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addSample(@RequestBody Map<String, Object> samplePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addSample = "";
	JSONObject jsonObject = new JSONObject(samplePayload);
	addSample = masterService.addSample(jsonObject, request, response);
	return addSample;
}

@RequestMapping(value = "/updateSampleDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateSampleDetails(@RequestBody HashMap<String, Object> samplePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateSample = "";
	JSONObject jsonObject = new JSONObject(samplePayload);
	updateSample = masterService.updateSampleDetails(jsonObject, request, response);
	return updateSample;
}

@RequestMapping(value = "/updateSampleStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateSampleStatus(@RequestBody HashMap<String, Object> samplePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateSampleStatus = "";
	JSONObject jsonObject = new JSONObject(samplePayload);
	updateSampleStatus = masterService.updateSampleStatus(jsonObject, request, response);
	return updateSampleStatus;
}

/*********************************************
* MAS UOM
********************************************************/
@RequestMapping(value = "/getAllUOM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllUOM(@RequestBody Map<String, Object> UOMPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allUOM = "";
	JSONObject jsonObject = new JSONObject(UOMPayload);
	allUOM = masterService.getAllUOM(jsonObject, request, response);
	return allUOM;
}

@RequestMapping(value = "/addUOM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addUOM(@RequestBody Map<String, Object> UOMPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addUOM = "";
	JSONObject jsonObject = new JSONObject(UOMPayload);
	addUOM = masterService.addUOM(jsonObject, request, response);
	return addUOM;
}

@RequestMapping(value = "/updateUOMDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateUOMDetails(@RequestBody HashMap<String, Object> UOMPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateUOM = "";
	JSONObject jsonObject = new JSONObject(UOMPayload);
	updateUOM = masterService.updateUOMDetails(jsonObject, request, response);
	return updateUOM;
}

@RequestMapping(value = "/updateUOMStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateUOMStatus(@RequestBody HashMap<String, Object> UOMPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateUOMStatus = "";
	JSONObject jsonObject = new JSONObject(UOMPayload);
	updateUOMStatus = masterService.updateUOMStatus(jsonObject, request, response);
	return updateUOMStatus;
}

/*********************************************
* Item Unit (MasStoreUnit)
********************************************************/
@RequestMapping(value = "/getAllItemUnit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllItemUnit(@RequestBody Map<String, Object> itemUnitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allItemUnit = "";
	JSONObject jsonObject = new JSONObject(itemUnitPayload);
	allItemUnit = masterService.getAllItemUnit(jsonObject, request, response);
	return allItemUnit;
}

@RequestMapping(value = "/addItemUnit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addItemUnit(@RequestBody Map<String, Object> itemUnitPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addItemUnit = "";
	JSONObject jsonObject = new JSONObject(itemUnitPayload);
	addItemUnit = masterService.addItemUnit(jsonObject, request, response);
	return addItemUnit;
}

@RequestMapping(value = "/updateItemUnitDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateItemUnitDetails(@RequestBody HashMap<String, Object> itemUnitPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateItemUnit = "";
	JSONObject jsonObject = new JSONObject(itemUnitPayload);
	updateItemUnit = masterService.updateItemUnitDetails(jsonObject, request, response);
	return updateItemUnit;
}

@RequestMapping(value = "/updateItemUnitStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateItemUnitStatus(@RequestBody HashMap<String, Object> itemUnitPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateItemUnitStatus = "";
	JSONObject jsonObject = new JSONObject(itemUnitPayload);
	updateItemUnitStatus = masterService.updateItemUnitStatus(jsonObject, request, response);
	return updateItemUnitStatus;
}

/*********************************************
* Mas MainChargecode
********************************************************/
@RequestMapping(value = "/getAllMainChargecode", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllMainChargecode(@RequestBody Map<String, Object> mainChargecodePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allMainChargecode = "";
	JSONObject jsonObject = new JSONObject(mainChargecodePayload);
	allMainChargecode = masterService.getAllMainChargecode(jsonObject, request, response);
	return allMainChargecode;
}

@RequestMapping(value = "/addMainChargecode", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addMainChargecode(@RequestBody Map<String, Object> mainChargecodePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addMainChargecode = "";
	JSONObject jsonObject = new JSONObject(mainChargecodePayload);
	addMainChargecode = masterService.addMainChargecode(jsonObject, request, response);
	return addMainChargecode;
}

@RequestMapping(value = "/updateMainChargecodeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMainChargecodeDetails(@RequestBody HashMap<String, Object> mainChargecode,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMainChargecode = "";
	JSONObject jsonObject = new JSONObject(mainChargecode);
	updateMainChargecode = masterService.updateMainChargecodeDetails(mainChargecode, request, response);
	return updateMainChargecode;
}

@RequestMapping(value = "/updateMainChargecodeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateMainChargecodeStatus(@RequestBody HashMap<String, Object> mainChargecode,
		HttpServletRequest request, HttpServletResponse response) {
	String updateMainChargecodeStatus = "";
	JSONObject jsonObject = new JSONObject(mainChargecode);
	updateMainChargecodeStatus = masterService.updateMainChargecodeStatus(mainChargecode, request, response);
	return updateMainChargecodeStatus;
}

@RequestMapping(value = "/getDepartmentList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getDepartmentList(HttpServletRequest request, HttpServletResponse response) {
	String getDepartment = "";
	getDepartment = masterService.getDepartmentList(request, response);
	return getDepartment;
}

/*********************************************
* Users
********************************************************/
@RequestMapping(value = "/getAllUsers", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllUsers(@RequestBody Map<String, Object> usersPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allUsers = "";
	JSONObject jsonObject = new JSONObject(usersPayload);
	allUsers = masterService.getAllUsers(jsonObject, request, response);
	return allUsers;
}

@RequestMapping(value = "/addUsers", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addUsers(@RequestBody Map<String, Object> usersPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addUsers = "";
	JSONObject jsonObject = new JSONObject(usersPayload);
	addUsers = masterService.addUsers(jsonObject, request, response);
	return addUsers;
}

@RequestMapping(value = "/updateUsersDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateUsersDetails(@RequestBody HashMap<String, Object> users,
		HttpServletRequest request, HttpServletResponse response) {
	String updateUsers = "";
	JSONObject jsonObject = new JSONObject(users);
	updateUsers = masterService.updateUsersDetails(users, request, response);
	return updateUsers;
}

@RequestMapping(value = "/updateUsersStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateUsersStatus(@RequestBody HashMap<String, Object> users,
		HttpServletRequest request, HttpServletResponse response) {
	String updateUsersStatus = "";
	JSONObject jsonObject = new JSONObject(users);
	updateUsersStatus = masterService.updateUsersStatus(users, request, response);
	return updateUsersStatus;
}

@RequestMapping(value = "/getHospitalList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getHospitalList(HttpServletRequest request, HttpServletResponse response) {
	String addHospital = "";
	addHospital = masterService.getHospitalList(request, response);
	return addHospital;
}


/*********************************************
* MAS Role
********************************************************/
@RequestMapping(value = "/getAllRole", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllRole(@RequestBody Map<String, Object> rolePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allRole = "";
	JSONObject jsonObject = new JSONObject(rolePayload);
	allRole = masterService.getAllRole(jsonObject, request, response);
	return allRole;
}

@RequestMapping(value = "/addRole", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addRole(@RequestBody Map<String, Object> rolePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addRole = "";
	JSONObject jsonObject = new JSONObject(rolePayload);
	addRole = masterService.addRole(jsonObject, request, response);
	return addRole;
}

@RequestMapping(value = "/updateRoleDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRoleDetails(@RequestBody HashMap<String, Object> rolePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateRole = "";
	JSONObject jsonObject = new JSONObject(rolePayload);
	updateRole = masterService.updateRoleDetails(jsonObject, request, response);
	return updateRole;
}

@RequestMapping(value = "/updateRoleStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRoleStatus(@RequestBody HashMap<String, Object> rolePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateRoleStatus = "";
	JSONObject jsonObject = new JSONObject(rolePayload);
	updateRoleStatus = masterService.updateRoleStatus(jsonObject, request, response);
	return updateRoleStatus;
}

/*********************************************
* Range
********************************************************/
@RequestMapping(value = "/getAllRange", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllRange(@RequestBody Map<String, Object> rangePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allRange = "";
	JSONObject jsonObject = new JSONObject(rangePayload);
	allRange = masterService.getAllRange(jsonObject, request, response);
	return allRange;
}

@RequestMapping(value = "/addRange", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addRange(@RequestBody Map<String, Object> rangePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addRange = "";
	JSONObject jsonObject = new JSONObject(rangePayload);
	addRange = masterService.addRange(jsonObject, request, response);
	return addRange;
}

@RequestMapping(value = "/updateRangeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRangeDetails(@RequestBody HashMap<String, Object> range,
		HttpServletRequest request, HttpServletResponse response) {
	String updateRange = "";
	JSONObject jsonObject = new JSONObject(range);
	updateRange = masterService.updateRangeDetails(range, request, response);
	return updateRange;
}

@RequestMapping(value = "/updateRangeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateRangeStatus(@RequestBody HashMap<String, Object> range,
		HttpServletRequest request, HttpServletResponse response) {
	String updateRangeStatus = "";
	JSONObject jsonObject = new JSONObject(range);
	updateRangeStatus = masterService.updateRangeStatus(range, request, response);
	return updateRangeStatus;
}

@RequestMapping(value = "/getGenderList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getGenderList(HttpServletRequest request, HttpServletResponse response) {
	String addGender = "";
	addGender = masterService.getGenderList(request, response);
	return addGender;
}

@RequestMapping(value="/getMasRange", method=RequestMethod.POST, produces={ MediaType.APPLICATION_JSON_VALUE }, consumes={ MediaType.APPLICATION_JSON_VALUE})
public String getMasRange(@RequestBody HashMap<String, Object> requestPayload, HttpServletRequest request, HttpServletResponse response) {
	JSONObject jsonObject = new JSONObject(requestPayload);
	return masterService.getMasRange(jsonObject, request, response);
}

/*********************************************
* MAS StoreGroup
********************************************************/
@RequestMapping(value = "/getAllStoreGroup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllStoreGroup(@RequestBody Map<String, Object> storeGroupPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allStoreGroup = "";
	JSONObject jsonObject = new JSONObject(storeGroupPayload);
	allStoreGroup = masterService.getAllStoreGroup(jsonObject, request, response);
	return allStoreGroup;
}

@RequestMapping(value = "/addStoreGroup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addStoreGroup(@RequestBody Map<String, Object> storeGroupPayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addStoreGroup = "";
	JSONObject jsonObject = new JSONObject(storeGroupPayload);
	addStoreGroup = masterService.addStoreGroup(jsonObject, request, response);
	return addStoreGroup;
}

@RequestMapping(value = "/updateStoreGroup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateStoreGroup(@RequestBody HashMap<String, Object> storeGroupPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateStoreGroup = "";
	JSONObject jsonObject = new JSONObject(storeGroupPayload);
	updateStoreGroup = masterService.updateStoreGroup(jsonObject, request, response);
	return updateStoreGroup;
}

@RequestMapping(value = "/updateStoreGroupStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateStoreGroupStatus(@RequestBody HashMap<String, Object> storeGroupPayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateStoreGroupStatus = "";
	JSONObject jsonObject = new JSONObject(storeGroupPayload);
	updateStoreGroupStatus = masterService.updateStoreGroupStatus(jsonObject, request, response);
	return updateStoreGroupStatus;
}

/*********************************************
* MAS ItemType
********************************************************/
@RequestMapping(value = "/getAllItemType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllItemType(@RequestBody Map<String, Object> itemTypePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String allItemType = "";
	JSONObject jsonObject = new JSONObject(itemTypePayload);
	allItemType= masterService.getAllItemType(jsonObject, request, response);
	return allItemType;
}

@RequestMapping(value = "/addItemType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addItemType(@RequestBody Map<String, Object> itemTypePayload, HttpServletRequest request,
		HttpServletResponse response) {
	String addItemType = "";
	JSONObject jsonObject = new JSONObject(itemTypePayload);
	addItemType = masterService.addItemType(jsonObject, request, response);
	return addItemType;
}

@RequestMapping(value = "/updateItemType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateItemType(@RequestBody HashMap<String, Object> itemTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateItemType = "";
	JSONObject jsonObject = new JSONObject(itemTypePayload);
	updateItemType = masterService.updateItemType(jsonObject, request, response);
	return updateItemType;
}

@RequestMapping(value = "/updateItemTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateItemTypeStatus(@RequestBody HashMap<String, Object> itemTypePayload,
		HttpServletRequest request, HttpServletResponse response) {
	String updateItemTypeStatus = "";
	JSONObject jsonObject = new JSONObject(itemTypePayload);
	updateItemTypeStatus = masterService.updateItemTypeStatus(jsonObject, request, response);
	return updateItemTypeStatus;
}

/***********************************
* ItemSection Master
*************************************/
/**
* @param request
* @param response
* @return
*/

@RequestMapping(value = "/addStoreSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addStoreSection(@RequestBody HashMap<String, Object> storeSection, HttpServletRequest request,
		HttpServletResponse response) {
	String addStoreSection = "";
	JSONObject json = new JSONObject(storeSection);
	addStoreSection = masterService.addStoreSection(json, request, response);
	return addStoreSection;
}

@RequestMapping(value = "/getAllStoreSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllStoreSection(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String storeSection = "";
	JSONObject jsonObj = new JSONObject(payload);
	storeSection = masterService.getAllStoreSection(jsonObj, request, response);
	return storeSection;
}

@RequestMapping(value = "/getStoreSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getStoreSection(@RequestBody HashMap<String, Object> storeSection, HttpServletRequest request) {
	String storeSection1 = "";
	storeSection1 = masterService.getStoreSection(storeSection, request);
	return storeSection1;
}

@RequestMapping(value = "/updateStoreSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateStoreSection(@RequestBody HashMap<String, Object> storeSection, HttpServletRequest request,
		HttpServletResponse response) {
	String updateStoreSection = "";
	updateStoreSection = masterService.updateStoreSection(storeSection, request, response);
	return updateStoreSection;
}

@RequestMapping(value = "/statusStoreSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusStoreSection(@RequestBody HashMap<String, Object> storeSection, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusStoreSection(storeSection, request, response);
	return status;
}

/*
* @RequestMapping(value = "/getItemTypeList", method = RequestMethod.POST,
* produces = "application/json", consumes = "application/json") public String
* getItemTypeList(HttpServletRequest request, HttpServletResponse response) {
* String addItemType = "";
* 
* addItemType = masterService.getItemTypeList(request, response); return
* addItemType; }
*/


/***********************************
* ItemClass Master
*************************************/
/**
* @param request
* @param response
* @return
*/

@RequestMapping(value = "/addItemClass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String addItemClass(@RequestBody HashMap<String, Object> iItemClass, HttpServletRequest request,
		HttpServletResponse response) {
	String addIItemClass = "";
	JSONObject json = new JSONObject(iItemClass);
	addIItemClass = masterService.addItemClass(json, request, response);
	return addIItemClass;
}

@RequestMapping(value = "/getAllItemClass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getAllIItemClass(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		HttpServletResponse response) {
	String iItemClass = "";
	JSONObject jsonObj = new JSONObject(payload);
	iItemClass = masterService.getAllItemClass(jsonObj, request, response);
	return iItemClass;
}

@RequestMapping(value = "/getIItemClass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String getIItemClass(@RequestBody HashMap<String, Object> iItemClass, HttpServletRequest request) {
	String iItemClass1 = "";
	iItemClass1 = masterService.getItemClass(iItemClass, request);
	return iItemClass1;
}

@RequestMapping(value = "/updateItemClass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String updateItemClass(@RequestBody HashMap<String, Object> itemClass, HttpServletRequest request,
		HttpServletResponse response) {
	String updateIItemClass = "";
	updateIItemClass = masterService.updateItemClass(itemClass, request, response);
	return updateIItemClass;
}

@RequestMapping(value = "/statusItemClass", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
public String statusItemClass(@RequestBody HashMap<String, Object> iItemClass, HttpServletRequest request,
		HttpServletResponse response) {
	String status = "";
	status = masterService.statusItemClass(iItemClass, request, response);
	return status;
}


 @RequestMapping(value = "/getStoreSectionList", method = RequestMethod.POST,
 produces = "application/json", consumes = "application/json") 
 public String getStoreSectionList(HttpServletRequest request, 
		  HttpServletResponse response) { 
 String addItemType = "";
 addItemType = masterService.getStoreSectionList(request, response); return
 addItemType;
 }
 
 /***********************************
	 *Section Master
	 *************************************/
	/**
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/addSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addSection(@RequestBody HashMap<String, Object> section, HttpServletRequest request,
			HttpServletResponse response) {
		String addSection = "";
		JSONObject json = new JSONObject(section);
		addSection = masterService.addSection(json, request, response);
		return addSection;
	}

	@RequestMapping(value = "/getAllSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSection(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String section = "";
		JSONObject jsonObj = new JSONObject(payload);
		section = masterService.getAllSection(jsonObj, request, response);
		return section;
	}

	@RequestMapping(value = "/getSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getSection(@RequestBody HashMap<String, Object> section, HttpServletRequest request) {
		String section1 = "";
		section1 = masterService.getSection(section, request);
		return section1;
	}

	@RequestMapping(value = "/updateSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSection(@RequestBody HashMap<String, Object> section, HttpServletRequest request,
			HttpServletResponse response) {
		String updateSection = "";
		updateSection = masterService.updateSection(section, request, response);
		return updateSection;
	}

	@RequestMapping(value = "/statusSection", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String statusSection(@RequestBody HashMap<String, Object> section, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.statusSection(section, request, response);
		return status;
	}
	
	/***********************************
	 * Item Drug Master
	 *************************************/
	/**
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/addItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addItem(@RequestBody HashMap<String, Object> rank, HttpServletRequest request,
			HttpServletResponse response) {
		String addItem = "";
		JSONObject json = new JSONObject(rank);
		addItem = masterService.addItem(json, request, response);
		return addItem;
	}

	@RequestMapping(value = "/getAllItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllItem(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String item = "";
		JSONObject jsonObj = new JSONObject(payload);
		item = masterService.getAllItem(jsonObj, request, response);
		return item;
	}

	@RequestMapping(value = "/getItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getItem(@RequestBody HashMap<String, Object> item, HttpServletRequest request) {
		String item1 = "";
		item1 = masterService.getItem(item, request);
		return item1;
	}

	@RequestMapping(value = "/updateItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateItem(@RequestBody HashMap<String, Object> item, HttpServletRequest request,
			HttpServletResponse response) {
		String updateItem = "";
		updateItem = masterService.updateItem(item, request, response);
		return updateItem;
	}

	@RequestMapping(value = "/statusItem", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String statusItem(@RequestBody HashMap<String, Object> item, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.statusItem(item, request, response);
		return status;
	}
	
	/***********************************
	 * ME MB Master
	 *************************************/
	
	@RequestMapping(value = "/addMEMBMaster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addMEMBMaster(@RequestBody Map<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		String addMEMB = "";
		JSONObject json = new JSONObject(command);
		addMEMB = masterService.addMEMBMaster(json, request, response);
		return addMEMB;
	}
	
	@RequestMapping(value = "/getAllMEMBMaster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllMEMBMaster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String memb = "";
		JSONObject jsonObj = new JSONObject(payload);
		memb = masterService.getAllMEMBMaster(jsonObj, request, response);
		return memb;
	}
	
	@RequestMapping(value = "/updateMEMBMaster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateMEMBMaster(@RequestBody HashMap<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		String updateMEMB = "";
		updateMEMB = masterService.updateMEMBMaster(command, request, response);
		return updateMEMB;
	}
	
	@RequestMapping(value = "/updateMEMBStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateMEMBStatus(@RequestBody HashMap<String, Object> memb, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateMEMBStatus(memb, request, response);
		return status;
	}
	
	@RequestMapping(value = "/getInvestigationNameList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getInvestigationNameList(@RequestBody Map<String, Object> invPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allInvestigation = "";			
		allInvestigation = masterService.getInvestigationNameList( request, response);
		return allInvestigation;
	}
	
	@RequestMapping(value="/saveMEInvestigation", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String saveMEInvestigation(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return masterService.saveMEInvestigation(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getAllInvestigationMapping", method=RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes= {MediaType.APPLICATION_JSON_VALUE})
	public String getAllInvestigationMapping(@RequestBody Map<String, Object> requestdata, HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject(requestdata);
		return masterService.getAllInvestigationMapping(jsonObject,request,response);
	}
	
	@RequestMapping(value = "/updateMEInvestStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateMEInvestStatus(@RequestBody HashMap<String, Object> meinv, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateMEInvestStatus(meinv, request, response);
		return status;
	}
	
	
	@RequestMapping(value = "/updateInvestigationMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateInvestigationMapping(@RequestBody HashMap<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		String updateMEINV = "";
		updateMEINV = masterService.updateInvestigationMapping(command, request, response);
		return updateMEINV;
	}
	
	
	/*************************Sub Type Master************************************/
	
	@RequestMapping(value = "/getMainTypeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getMainTypeList(HttpServletRequest request, HttpServletResponse response) {
		String getSubType = "";
		getSubType = masterService.getMainTypeList(request, response);
		return getSubType;
	}
	
	@RequestMapping(value = "/addSubType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addSubType(@RequestBody Map<String, Object> subtypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addsub = "";
		JSONObject jsonObject = new JSONObject(subtypePayload);
		addsub = masterService.addSubType(jsonObject, request, response);
		return addsub;
	}
	
	@RequestMapping(value = "/getAllSubTypeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSubTypeDetails(@RequestBody Map<String, Object> subtypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allSubType = "";
		JSONObject jsonObject = new JSONObject(subtypePayload);
		allSubType = masterService.getAllSubTypeDetails(jsonObject, request, response);
		return allSubType;
	}
	
	@RequestMapping(value = "/updateSubTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSubTypeStatus(@RequestBody HashMap<String, Object> subtype, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateSubTypeStatus(subtype, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateSubTypeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSubTypeDetails(@RequestBody HashMap<String, Object> subtype,
			HttpServletRequest request, HttpServletResponse response) {
		String updateSubType = "";
		JSONObject jsonObject = new JSONObject(subtype);
		updateSubType = masterService.updateSubTypeDetails(subtype, request, response);
		return updateSubType;
	}
	
	/*********************************************
	 *Vendor Type Master
	 ********************************************************/
	@RequestMapping(value = "/getAllVendorType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllVendorType(@RequestBody Map<String, Object> vendorTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allVendorType = "";
		JSONObject jsonObject = new JSONObject(vendorTypePayload);
		allVendorType = masterService.getAllVendorType(jsonObject, request, response);
		return allVendorType;
	}
	
	@RequestMapping(value = "/addVendorType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addVendorType(@RequestBody Map<String, Object> vendorTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addVendorType = "";
		JSONObject jsonObject = new JSONObject(vendorTypePayload);
		addVendorType = masterService.addVendorType(jsonObject, request, response);
		return addVendorType;
	}

	@RequestMapping(value = "/updateVendorType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateVendorType(@RequestBody HashMap<String, Object> vendorTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateVendorType = "";
		JSONObject jsonObject = new JSONObject(vendorTypePayload);
		updateVendorType = masterService.updateVendorType(jsonObject, request, response);
		return updateVendorType;
	}

	@RequestMapping(value = "/updateVendorTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateVendorTypeStatus(@RequestBody HashMap<String, Object> vendorTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateVendorTypeStatus = "";
		JSONObject jsonObject = new JSONObject(vendorTypePayload);
		updateVendorTypeStatus = masterService.updateVendorTypeStatus(jsonObject, request, response);
		return updateVendorTypeStatus;
	}
	
	
	/*********************************************
	 *Vendor Master
	 ********************************************************/
	@RequestMapping(value = "/getAllVendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllVendor(@RequestBody Map<String, Object> vendorPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allVendor = "";
		JSONObject jsonObject = new JSONObject(vendorPayload);
		allVendor = masterService.getAllVendor(jsonObject, request, response);
		return allVendor;
	}
	
	@RequestMapping(value = "/addVendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addVendor(@RequestBody Map<String, Object> vendorPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addVendor = "";
		JSONObject jsonObject = new JSONObject(vendorPayload);
		addVendor = masterService.addVendor(jsonObject, request, response);
		return addVendor;
	}

	@RequestMapping(value = "/updateVendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateVendor(@RequestBody HashMap<String, Object> vendorPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateVendor = "";
		JSONObject jsonObject = new JSONObject(vendorPayload);
		updateVendor = masterService.updateVendor(vendorPayload, request, response);
		return updateVendor;
	}

	@RequestMapping(value = "/updateVendorStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateVendorStatus(@RequestBody HashMap<String, Object> vendor,
			HttpServletRequest request, HttpServletResponse response) {
		String updateVendorStatus = "";
		JSONObject jsonObject = new JSONObject(vendor);
		updateVendorStatus = masterService.updateVendorStatus(vendor, request, response);
		return updateVendorStatus;
	}
	
	@RequestMapping(value = "/getStateList", method = RequestMethod.POST,
			  produces = "application/json", consumes = "application/json") 
			  public String getStateList(HttpServletRequest request, 
					  HttpServletResponse response) { 
			  String addState = "";
			  addState = masterService.getStateList(request, response);
			  return addState;
	 }
	
	@RequestMapping(value = "/getDistrictList", method = RequestMethod.POST,
			  produces = "application/json", consumes = "application/json") 
			  public String getDistrictList(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, 
					  HttpServletResponse response) { 
			  String addCity = "";
			  addCity = masterService.getDistrictList(payload,request, response); 
			  return addCity;
			  }
	
	@RequestMapping(value = "/getDistrictListById", method = RequestMethod.POST,
			  produces = "application/json", consumes = "application/json") 
			  public String getDistrictListById(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, 
					  HttpServletResponse response) { 
			  String addCity = "";
			  addCity = masterService.getDistrictListById(payload,request, response); 
			  return addCity;
			  }
	
	/**************************************
	 * Sample Container Master
	 **************************************************/
	
	@RequestMapping(value = "/addSampleContainer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addSampleContainer(@RequestBody String samplePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addSample = "";
		JSONObject jsonObject = new JSONObject(samplePayload);
		addSample = masterService.addSampleContainer(jsonObject, request, response);
		return addSample;
	}
	
	@RequestMapping(value = "/getAllSampleContainer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSampleContainer(@RequestBody Map<String, Object> samplePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allSampleContainer = "";
		JSONObject jsonObject = new JSONObject(samplePayload);
		allSampleContainer = masterService.getAllSampleContainer(jsonObject, request, response);			
		return allSampleContainer;
	}
	
	
	@RequestMapping(value = "/updateSampleContainerStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSampleContainerStatus(@RequestBody HashMap<String, Object> subtype, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateSampleContainerStatus(subtype, request, response);
		return status;
	}
	
	
	@RequestMapping(value = "/updateSampleContainer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSampleContainer(@RequestBody HashMap<String, Object> collection,
			HttpServletRequest request, HttpServletResponse response) {
		String updateCollection = "";
		JSONObject jsonObject = new JSONObject(collection);
		updateCollection = masterService.updateSampleContainer(collection, request, response);
		return updateCollection;
	}
	
	
	/*********************************************
	 *Department Type Master
	 ********************************************************/
	@RequestMapping(value = "/getAllDepartmentType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDepartmentType(@RequestBody Map<String, Object> departmentTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDepartmentType = "";
		JSONObject jsonObject = new JSONObject(departmentTypePayload);
		allDepartmentType = masterService.getAllDepartmentType(jsonObject, request, response);
		return allDepartmentType;
	}
	
	@RequestMapping(value = "/addDepartmentType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDepartmentype(@RequestBody Map<String, Object> departmentTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDepartmentType = "";
		JSONObject jsonObject = new JSONObject(departmentTypePayload);
		addDepartmentType = masterService.addDepartmentType(jsonObject, request, response);
		return addDepartmentType;
	}

	@RequestMapping(value = "/updateDepartmentType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDepartmentType(@RequestBody HashMap<String, Object> departmentTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDepartmentType = "";
		JSONObject jsonObject = new JSONObject(departmentTypePayload);
		updateDepartmentType = masterService.updateDepartmentType(jsonObject, request, response);
		return updateDepartmentType;
	}

	@RequestMapping(value = "/updateDepartmentTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDepartmentTypeStatus(@RequestBody HashMap<String, Object> departmentTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDepartmentTypeStatus = "";
		JSONObject jsonObject = new JSONObject(departmentTypePayload);
		updateDepartmentTypeStatus = masterService.updateDepartmentTypeStatus(jsonObject, request, response);
		return updateDepartmentTypeStatus;
	}
	
	

	
	/**************************************
	 * Investigation UOM Master
	 **************************************************/
	
	@RequestMapping(value = "/addInvestigationUOM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addInvestigationUOM(@RequestBody String uomPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addUOM = "";
		JSONObject jsonObject = new JSONObject(uomPayload);
		addUOM = masterService.addInvestigationUOM(jsonObject, request, response);
		return addUOM;
	}
	
	@RequestMapping(value = "/getAllInvestigationUOM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllInvestigationUOM(@RequestBody Map<String, Object> uomPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allUOM = "";
		JSONObject jsonObject = new JSONObject(uomPayload);
		allUOM = masterService.getAllInvestigationUOM(jsonObject, request, response);			
		return allUOM;
	}
	
	@RequestMapping(value = "/updateInvestigationUOMStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateInvestigationUOMStatus(@RequestBody HashMap<String, Object> uom, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateInvestigationUOMStatus(uom, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateInvestigationUOM", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateInvestigationUOM(@RequestBody HashMap<String, Object> uom,
			HttpServletRequest request, HttpServletResponse response) {
		String updateUOM = "";			
		updateUOM = masterService.updateInvestigationUOM(uom, request, response);
		return updateUOM;
	}
	
	
	/**************************************
	 * Investigation Master
	 **************************************************/
	
	@RequestMapping(value = "/getAllMainChargeList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllMainChargeList(HttpServletRequest request, HttpServletResponse response) {
		String getMainCharge = "";
		getMainCharge = masterService.getAllMainChargeList(request, response);
		return getMainCharge;
	}
	
	@RequestMapping(value = "/getAllModalityList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllModalityList(@RequestBody Map<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		String getModality = "";
		JSONObject json=new JSONObject(payload);
		getModality = masterService.getAllModalityList(json, request, response);
		return getModality;
	}
	
	@RequestMapping(value = "/getAllSampleList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSampleList(HttpServletRequest request, HttpServletResponse response) {
		String getSample = "";
		getSample = masterService.getAllSampleList(request, response);
		return getSample;
	}
	
	@RequestMapping(value = "/getAllCollectionList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCollectionList(HttpServletRequest request, HttpServletResponse response) {
		String getCollection = "";
		getCollection = masterService.getAllCollectionList(request, response);
		return getCollection;
	}
	
	@RequestMapping(value = "/getAllUOMList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllUOMList(HttpServletRequest request, HttpServletResponse response) {
		String getUOM = "";
		getUOM = masterService.getAllUOMList(request, response);
		return getUOM;
	}
	
	
	@RequestMapping(value = "/addInvestigation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addInvestigation(@RequestBody String invPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addInv = "";
		JSONObject jsonObject = new JSONObject(invPayload);
		addInv = masterService.addInvestigation(jsonObject, request, response);
		return addInv;
	}
	
	@RequestMapping(value = "/getAllInvestigationDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllInvestigationDetails(@RequestBody Map<String, Object> uomPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allInv = "";
		JSONObject jsonObject = new JSONObject(uomPayload);
		allInv = masterService.getAllInvestigationDetails(jsonObject, request, response);			
		return allInv;
	}
	
	
	@RequestMapping(value = "/getAllRegionList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllRegionList(HttpServletRequest request, HttpServletResponse response) {
		String getSample = "";
		getSample = masterService.getAllSampleList(request, response);
		return getSample;
	}
	
	@RequestMapping(value = "/updateInvestigationStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateInvestigationStatus(@RequestBody HashMap<String, Object> inv, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateInvestigationStatus(inv, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateInvestigation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateInvestigation(@RequestBody HashMap<String, Object> inv,
			HttpServletRequest request, HttpServletResponse response) {
		String updateInv = "";			
		updateInv = masterService.updateInvestigation(inv, request, response);
		return updateInv;
	}
	
	/**************************************
	 * Sub Investigation Master
	 **************************************************/		
	
	@RequestMapping(value = "/getAllSubInvestigationDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSubInvestigationDetails(@RequestBody Map<String, Object> subPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allSubInv = "";
		JSONObject jsonObject = new JSONObject(subPayload);
		allSubInv = masterService.getAllSubInvestigationDetails(jsonObject, request, response);			
		return allSubInv;
	}
			
	@RequestMapping(value = "/updateSubInvestigation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSubInvestigation(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String addSubInv = "";			
		addSubInv = masterService.updateSubInvestigation(jsondata, request, response);
		return addSubInv;
	}
	
	@RequestMapping(value = "/deleteSunbInvestigationById", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteSunbInvestigationById(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String delSubInv = "";			
		delSubInv = masterService.deleteSunbInvestigationById(jsondata, request, response);
		return delSubInv;
	}
	
	@RequestMapping(value = "/deleteFixedValueById", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String deleteFixedValueById(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String delFixedVal = "";			
		delFixedVal = masterService.deleteFixedValueById(jsondata, request, response);
		return delFixedVal;
	}
	
	/**************************************
	 * DepartmentAndDoctorMappingMaster Master
	 **************************************************/
	
	@RequestMapping(value = "/addDepartmentAndDoctorMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDepartmentAndDoctorMapping(@RequestBody String dndPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDND = "";
		JSONObject jsonObject = new JSONObject(dndPayload);
		addDND = masterService.addDepartmentAndDoctorMapping(jsonObject, request, response);
		return addDND;
	}
	
	@RequestMapping(value = "/getAllDepartmentAndDoctorMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDepartmentAndDoctorMapping(@RequestBody Map<String, Object> dndPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDND = "";
		JSONObject jsonObject = new JSONObject(dndPayload);
		allDND = masterService.getAllDepartmentAndDoctorMapping(jsonObject, request, response);			
		return allDND;
	}
	
	@RequestMapping(value = "/updateDepartmentAndDoctorMappingStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDepartmentAndDoctorMappingStatus(@RequestBody HashMap<String, Object> dnd, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateDepartmentAndDoctorMappingStatus(dnd, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateDepartmentAndDoctorMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDepartmentAndDoctorMapping(@RequestBody HashMap<String, Object> dnd,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDND = "";			
		updateDND = masterService.updateDepartmentAndDoctorMapping(dnd, request, response);
		return updateDND;
	}
	
	/**************************************
	 *Employee Master
	 **************************************************/
	@RequestMapping(value = "/getAllEmployee", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllEmployee(@RequestBody Map<String, Object> employeePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allEmployee = "";
		JSONObject jsonObject = new JSONObject(employeePayload);
		allEmployee = masterService.getAllEmployee(jsonObject, request, response);			
		return allEmployee;
	}
	
	@RequestMapping(value = "/getAllUnitList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllUnitList(HttpServletRequest request, HttpServletResponse response) {
		String getUnit = "";
		getUnit = masterService.getAllUnitList(request, response);
		return getUnit;
	}
	
	/**************************************
	 *Fixed Value Master
	 **************************************************/
	
	@RequestMapping(value = "/updateFixedValue", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateFixedValue(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String addFV = "";			
		addFV = masterService.updateFixedValue(jsondata, request, response);
		return addFV;
	}
	
	

	@RequestMapping(value = "/getAllFixeValueById", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllFixeValueById(@RequestBody Map<String, Object> fvPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allFV = "";
		JSONObject jsonObject = new JSONObject(fvPayload);
		allFV = masterService.getAllFixeValueById(jsonObject, request, response);			
		return allFV;
	}
	
	@RequestMapping(value = "/validateFixedValue", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String validateFixedValue(@RequestBody Map<String, Object> fvPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allFV = "";
		JSONObject jsonObject = new JSONObject(fvPayload);
		allFV = masterService.validateFixedValue(jsonObject, request, response);			
		return allFV;
	}
	/**************************************
	 *Normal Value Master
	 **************************************************/
	
	@RequestMapping(value = "/updateNormalValue", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateNormalValue(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		String addNV = "";			
		addNV = masterService.updateNormalValue(jsondata, request, response);
		return addNV;
	}
	
	@RequestMapping(value = "/getAllNormalValueById", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllNormalValueById(@RequestBody Map<String, Object> fvPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allNV = "";
		JSONObject jsonObject = new JSONObject(fvPayload);
		allNV = masterService.getAllNormalValueById(jsonObject, request, response);			
		return allNV;
	}
	
	@RequestMapping(value = "/validateServiceNo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String validateServiceNo(@RequestBody Map<String, Object> svPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allsv = "";
		JSONObject jsonObject = new JSONObject(svPayload);
		allsv = masterService.validateServiceNo(jsonObject, request, response);			
		return allsv;
	}
	
	/*********************************************
	 * MAS Discharge Status
	 ********************************************************/
	@RequestMapping(value = "/getAllDischargeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDischargeStatus(@RequestBody Map<String, Object> dischargeStatusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDischargeStatus = "";
		JSONObject jsonObject = new JSONObject(dischargeStatusPayload);
		allDischargeStatus = masterService.getAllDischargeStatus(jsonObject, request, response);
		return allDischargeStatus;
	}
	
	@RequestMapping(value = "/addDischargeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDischargeStatus(@RequestBody Map<String, Object> dischargeStatusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDischargeStatus = "";
		JSONObject jsonObject = new JSONObject(dischargeStatusPayload);
		addDischargeStatus = masterService.addDischargeStatus(jsonObject, request, response);
		return addDischargeStatus;
	}

	@RequestMapping(value = "/updateDischargeStatusDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDischargeStatusDetails(@RequestBody HashMap<String, Object> dischargeStatusPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDischargeStatus = "";
		JSONObject jsonObject = new JSONObject(dischargeStatusPayload);
		updateDischargeStatus = masterService.updateDischargeStatusDetails(jsonObject, request, response);
		return updateDischargeStatus;
	}

	@RequestMapping(value = "/updateDischargeStatusStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDischargeStatusStatus(@RequestBody HashMap<String, Object> dischargeStatusPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDischargeStatusStatus = "";
		JSONObject jsonObject = new JSONObject(dischargeStatusPayload);
		updateDischargeStatusStatus = masterService.updateDischargeStatusStatus(jsonObject, request, response);
		return updateDischargeStatusStatus;
	}
	
	/*********************************************
	 * MAS Bed Status
	 ********************************************************/
	@RequestMapping(value = "/getAllBedStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllBedStatus(@RequestBody Map<String, Object> bedStatusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allBedStatus = "";
		JSONObject jsonObject = new JSONObject(bedStatusPayload);
		allBedStatus = masterService.getAllBedStatus(jsonObject, request, response);
		return allBedStatus;
	}
	
	@RequestMapping(value = "/addBedStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addBedStatus(@RequestBody Map<String, Object> bedStatusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addBedStatus = "";
		JSONObject jsonObject = new JSONObject(bedStatusPayload);
		addBedStatus = masterService.addBedStatus(jsonObject, request, response);
		return addBedStatus;
	}

	@RequestMapping(value = "/updateBedStatusDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBedStatusDetails(@RequestBody HashMap<String, Object> bedStatusPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateBedStatus = "";
		JSONObject jsonObject = new JSONObject(bedStatusPayload);
		updateBedStatus = masterService.updateBedStatusDetails(jsonObject, request, response);
		return updateBedStatus;
	}

	@RequestMapping(value = "/updateBedStatusStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBedStatusStatus(@RequestBody HashMap<String, Object> bedStatusPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateBedStatusStatus = "";
		JSONObject jsonObject = new JSONObject(bedStatusPayload);
		updateBedStatusStatus = masterService.updateBedStatusStatus(jsonObject, request, response);
		return updateBedStatusStatus;
	}
	
	/**************************************
	 * Bed Master
	 **************************************************/
	
	@RequestMapping(value = "/addBed", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addBed(@RequestBody String bedPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addBed = "";
		JSONObject jsonObject = new JSONObject(bedPayload);
		addBed = masterService.addBed(jsonObject, request, response);
		return addBed;
	}
	
	@RequestMapping(value = "/getAllBed", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllBed(@RequestBody Map<String, Object> bedPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allBed = "";
		JSONObject jsonObject = new JSONObject(bedPayload);
		allBed = masterService.getAllBed(jsonObject, request, response);			
		return allBed;
	}
	
	@RequestMapping(value = "/updateBedStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBedStatus(@RequestBody HashMap<String, Object> bed, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateBedStatus(bed, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateBed", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBed(@RequestBody HashMap<String, Object> bed,
			HttpServletRequest request, HttpServletResponse response) {
		String updateBed = "";			
		updateBed = masterService.updateBed(bed, request, response);
		return updateBed;
	}
	
	/*********************************************
	 * MAS Speciality
	 ********************************************************/
	@RequestMapping(value = "/getAllSpeciality", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSpeciality(@RequestBody Map<String, Object> specialityPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allSpeciality = "";
		JSONObject jsonObject = new JSONObject(specialityPayload);
		allSpeciality = masterService.getAllSpeciality(jsonObject, request, response);
		return allSpeciality;
	}
	
	@RequestMapping(value = "/addSpeciality", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addSpeciality(@RequestBody Map<String, Object> specialityPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addSpeciality = "";
		JSONObject jsonObject = new JSONObject(specialityPayload);
		addSpeciality = masterService.addSpeciality(jsonObject, request, response);
		return addSpeciality;
	}

	@RequestMapping(value = "/updateSpecialityDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSpecialityDetails(@RequestBody HashMap<String, Object> specialityPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateSpeciality = "";
		JSONObject jsonObject = new JSONObject(specialityPayload);
		updateSpeciality = masterService.updateSpecialityDetails(jsonObject, request, response);
		return updateSpeciality;
	}

	@RequestMapping(value = "/updateSpecialityStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSpecialityStatus(@RequestBody HashMap<String, Object> specialityPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateSpecialityStatus = "";
		JSONObject jsonObject = new JSONObject(specialityPayload);
		updateSpecialityStatus = masterService.updateSpecialityStatus(jsonObject, request, response);
		return updateSpecialityStatus;
	}
	
	
	/*********************************************
	 * MAS AdmissionType
	 ********************************************************/
	@RequestMapping(value = "/getAllAdmissionType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllAdmissionType(@RequestBody Map<String, Object> admissionTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allAdmissionType = "";
		JSONObject jsonObject = new JSONObject(admissionTypePayload);
		allAdmissionType = masterService.getAllAdmissionType(jsonObject, request, response);
		return allAdmissionType;
	}
	
	@RequestMapping(value = "/addAdmissionType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addAdmissionType(@RequestBody Map<String, Object> admissionTypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addAdmissionType = "";
		JSONObject jsonObject = new JSONObject(admissionTypePayload);
		addAdmissionType = masterService.addAdmissionType(jsonObject, request, response);
		return addAdmissionType;
	}

	@RequestMapping(value = "/updateAdmissionTypeDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateAdmissionTypeDetails(@RequestBody HashMap<String, Object> admissionTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateAdmissionType = "";
		JSONObject jsonObject = new JSONObject(admissionTypePayload);
		updateAdmissionType = masterService.updateAdmissionTypeDetails(jsonObject, request, response);
		return updateAdmissionType;
	}

	@RequestMapping(value = "/updateAdmissionTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateAdmissionTypeStatus(@RequestBody HashMap<String, Object> admissionTypePayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateAdmissionTypeStatus = "";
		JSONObject jsonObject = new JSONObject(admissionTypePayload);
		updateAdmissionTypeStatus = masterService.updateAdmissionTypeStatus(jsonObject, request, response);
		return updateAdmissionTypeStatus;
	}
	
	/*********************************************
	 * MAS DisposedTo
	 ********************************************************/
	@RequestMapping(value = "/getAllDisposedTo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDisposedTo(@RequestBody Map<String, Object> disposedToPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDisposedTo = "";
		JSONObject jsonObject = new JSONObject(disposedToPayload);
		allDisposedTo = masterService.getAllDisposedTo(jsonObject, request, response);
		return allDisposedTo;
	}
	
	@RequestMapping(value = "/addDisposedTo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDisposedTo(@RequestBody Map<String, Object> disposedToPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDisposedTo = "";
		JSONObject jsonObject = new JSONObject(disposedToPayload);
		addDisposedTo = masterService.addDisposedTo(jsonObject, request, response);
		return addDisposedTo;
	}

	@RequestMapping(value = "/updateDisposedToDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDisposedToDetails(@RequestBody HashMap<String, Object> disposedToPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDisposedTo = "";
		JSONObject jsonObject = new JSONObject(disposedToPayload);
		updateDisposedTo = masterService.updateDisposedToDetails(jsonObject, request, response);
		return updateDisposedTo;
	}

	@RequestMapping(value = "/updateDisposedToStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDisposedToStatus(@RequestBody HashMap<String, Object> disposedToPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDisposedToStatus = "";
		JSONObject jsonObject = new JSONObject(disposedToPayload);
		updateDisposedToStatus = masterService.updateDisposedToStatus(jsonObject, request, response);
		return updateDisposedToStatus;
	}
	
	/*********************************************
	 * MAS Condition
	 ********************************************************/
	@RequestMapping(value = "/getAllCondition", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCondition(@RequestBody Map<String, Object> conditionPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allCondition= "";
		JSONObject jsonObject = new JSONObject(conditionPayload);
		allCondition = masterService.getAllCondition(jsonObject, request, response);
		return allCondition;
	}
	
	@RequestMapping(value = "/addCondition", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addCondition(@RequestBody Map<String, Object> conditionPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addCondition = "";
		JSONObject jsonObject = new JSONObject(conditionPayload);
		addCondition = masterService.addCondition(jsonObject, request, response);
		return addCondition;
	}

	@RequestMapping(value = "/updateConditionDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateConditionDetails(@RequestBody HashMap<String, Object> conditionPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateCondition = "";
		JSONObject jsonObject = new JSONObject(conditionPayload);
		updateCondition = masterService.updateConditionDetails(jsonObject, request, response);
		return updateCondition;
	}

	@RequestMapping(value = "/updateConditionStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateConditionStatus(@RequestBody HashMap<String, Object> conditionPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateConditionStatus = "";
		JSONObject jsonObject = new JSONObject(conditionPayload);
		updateConditionStatus = masterService.updateConditionStatus(jsonObject, request, response);
		return updateConditionStatus;
	}

	/*********************************************
	 * MAS Diet
	 ********************************************************/
	@RequestMapping(value = "/getAllDiet", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDiet(@RequestBody Map<String, Object> dietPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDiet = "";
		JSONObject jsonObject = new JSONObject(dietPayload);
		allDiet = masterService.getAllDiet(jsonObject, request, response);
		return allDiet;
	}
	
	@RequestMapping(value = "/addDiet", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDiet(@RequestBody Map<String, Object> dietPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDiet = "";
		JSONObject jsonObject = new JSONObject(dietPayload);
		addDiet = masterService.addDiet(jsonObject, request, response);
		return addDiet;
	}

	@RequestMapping(value = "/updateDietDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDietDetails(@RequestBody HashMap<String, Object> disposedToPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDiet = "";
		JSONObject jsonObject = new JSONObject(disposedToPayload);
		updateDiet = masterService.updateDietDetails(jsonObject, request, response);
		return updateDiet;
	}

	@RequestMapping(value = "/updateDietStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDietStatus(@RequestBody HashMap<String, Object> dietPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDietStatus = "";
		JSONObject jsonObject = new JSONObject(dietPayload);
		updateDietStatus = masterService.updateDietStatus(jsonObject, request, response);
		return updateDietStatus;
	}	
	
	@RequestMapping(value = "/getAllNiv", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllNiv(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String niv = "";
		JSONObject jsonObj = new JSONObject(payload);
		niv = masterService.getAllNiv(jsonObj, request, response);
		return niv;
	}
	
	@RequestMapping(value = "/addDisease", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDisease(@RequestBody Map<String, Object> diseasePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDisease = "";
		JSONObject jsonObject = new JSONObject(diseasePayload);
		addDisease = masterService.addDisease(jsonObject, request, response);
		return addDisease;
	}
	
	@RequestMapping(value = "/getAllDisease", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDisease(@RequestBody Map<String, Object> dietPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDisease = "";
		JSONObject jsonObject = new JSONObject(dietPayload);
		allDisease = masterService.getAllDisease(jsonObject, request, response);
		return allDisease;
	}
	
	
	@RequestMapping(value = "/updateDiseaseStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDiseaseStatus(@RequestBody HashMap<String, Object> dis, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateDiseaseStatus(dis, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateDisease", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDisease(@RequestBody HashMap<String, Object> payload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDisease = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateDisease = masterService.updateDisease(jsonObject, request, response);
		return updateDisease;
	}
	
	@RequestMapping(value = "/addDocument", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDocument(@RequestBody Map<String, Object> documentPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDocument = "";
		JSONObject jsonObject = new JSONObject(documentPayload);
		addDocument = masterService.addDocument(jsonObject, request, response);
		return addDocument;
	}
	
	@RequestMapping(value = "/getAllDocument", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDocument(@RequestBody Map<String, Object> docPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDocument = "";
		JSONObject jsonObject = new JSONObject(docPayload);
		allDocument = masterService.getAllDocument(jsonObject, request, response);
		return allDocument;
	}
	
	@RequestMapping(value = "/updateDocumentStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDocumentStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateDocumentStatus(statusPayload, request, response);
		return status;
	}
	
	@RequestMapping(value = "/updateDocument", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDocument(@RequestBody HashMap<String, Object> payload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDocument = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateDocument = masterService.updateDocument(jsonObject, request, response);
		return updateDocument;
	}
	
	@RequestMapping(value = "/addBank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addBank(@RequestBody Map<String, Object> bankPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addBank = "";
		JSONObject jsonObject = new JSONObject(bankPayload);
		addBank = masterService.addBank(jsonObject, request, response);
		return addBank;
	}
	
	@RequestMapping(value = "/updateBankStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBankStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateBankStatus(statusPayload, request, response);
		return status;
	}
	
	@RequestMapping(value = "/getAllBank", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllBank(@RequestBody Map<String, Object> bankPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allBank = "";
		JSONObject jsonObject = new JSONObject(bankPayload);
		allBank = masterService.getAllBank(jsonObject, request, response);
		return allBank;
	}
	
	
	@RequestMapping(value = "/updateBankDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateBankDetails(@RequestBody HashMap<String, Object> payload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateBank = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateBank = masterService.updateBankDetails(jsonObject, request, response);
		return updateBank;
	}
	
	@RequestMapping(value = "/addAccountType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addAccountType(@RequestBody Map<String, Object> actypePayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addAccountType = "";
		JSONObject jsonObject = new JSONObject(actypePayload);
		addAccountType = masterService.addAccountType(jsonObject, request, response);
		return addAccountType;
	}
	
	@RequestMapping(value = "/updateAccountTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateAccountTypeStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.updateAccountTypeStatus(statusPayload, request, response);
		return status;
	}
	
	@RequestMapping(value = "/getAllAccountType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllAccountType(@RequestBody Map<String, Object> actPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allAccountType = "";
		JSONObject jsonObject = new JSONObject(actPayload);
		allAccountType = masterService.getAllAccountType(jsonObject, request, response);
		return allAccountType;
	}
	
	@RequestMapping(value = "/updateAccountType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateAccountType(@RequestBody HashMap<String, Object> payload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateAccountType = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateAccountType = masterService.updateAccountType(jsonObject, request, response);
		return updateAccountType;
	}
	
	/*********************************************
	 * MAS ICD_Diagnosis
	 ********************************************************/
	@RequestMapping(value = "/getAllDiagnosis", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDiagnosis(@RequestBody Map<String, Object> diagnosisPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String allDiagnosis = "";
		JSONObject jsonObject = new JSONObject(diagnosisPayload);
		allDiagnosis = masterService.getAllDiagnosis(jsonObject, request, response);
		return allDiagnosis;
	}
	
	@RequestMapping(value = "/addDiagnosis", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDiagnosis(@RequestBody Map<String, Object> diagnosisPayload, HttpServletRequest request,
			HttpServletResponse response) {
		String addDiagnosis = "";
		JSONObject jsonObject = new JSONObject(diagnosisPayload);
		addDiagnosis = masterService.addDiagnosis(jsonObject, request, response);
		return addDiagnosis;
	}

	@RequestMapping(value = "/updateDiagnosis", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDiagnosis(@RequestBody HashMap<String, Object> diagnosisPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDiagnosis = "";
		JSONObject jsonObject = new JSONObject(diagnosisPayload);
		updateDiagnosis = masterService.updateDiagnosis(jsonObject, request, response);
		return updateDiagnosis;
	}

	@RequestMapping(value = "/updateDiagnosisStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDiagnosisStatus(@RequestBody HashMap<String, Object> diagnosisPayload,
			HttpServletRequest request, HttpServletResponse response) {
		String updateDiagnosisStatus = "";
		JSONObject jsonObject = new JSONObject(diagnosisPayload);
		updateDiagnosisStatus = masterService.updateDiagnosisStatus(jsonObject, request, response);
		return updateDiagnosisStatus;
	}
	
	/***********************************
	 * MedicalExamSchedule Master
	 *************************************/
	/**
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/addMedicalExamSchedule", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addMedicalExamSchedule(@RequestBody HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,
			HttpServletResponse response) {
		String addMedicalExamSchedule = "";
		JSONObject json = new JSONObject(medicalExamSchedule);
		addMedicalExamSchedule = masterService.addMedicalExamSchedule(json, request, response);
		return addMedicalExamSchedule;
	}

	@RequestMapping(value = "/getAllMedicalExamSchedule", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllMedicalExamSchedule(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String medicalExamSchedule = "";
		JSONObject jsonObj = new JSONObject(payload);
		medicalExamSchedule = masterService.getAllMedicalExamSchedule(jsonObj, request, response);
		return medicalExamSchedule;
	}

	@RequestMapping(value = "/getMedicalExamSchedule", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getMedicalExamSchedule(@RequestBody HashMap<String, Object> medicalExamSchedule, HttpServletRequest request) {
		String medicalExamSchedule1 = "";
		medicalExamSchedule1 = masterService.getMedicalExamSchedule(medicalExamSchedule, request);
		return medicalExamSchedule1;
	}

	@RequestMapping(value = "/updateMedicalExamSchedule", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateMedicalExamSchedule(@RequestBody HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,
			HttpServletResponse response) {
		String updateMedicalExamSchedule = "";
		updateMedicalExamSchedule = masterService.updateMedicalExamSchedule(medicalExamSchedule, request, response);
		return updateMedicalExamSchedule;
	}

	@RequestMapping(value = "/medicalExamScheduleStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String statusMedicalExamSchedule(@RequestBody HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.medicalExamScheduleStatus(medicalExamSchedule, request, response);
		return status;
	}
	
	@RequestMapping(value = "/getRankCategoryList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getEmpCategoryList(HttpServletRequest request, HttpServletResponse response) {
		String empCategoryList = "";
		empCategoryList = masterService.getRankCategoryList(request, response);
		return empCategoryList;
	}
	
	/***********************************
	 * FWC Master
	 *************************************/
	/**
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/addFWC", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addFWC(@RequestBody HashMap<String, Object> fwc, HttpServletRequest request,
			HttpServletResponse response) {
		String addFWC = "";
		JSONObject json = new JSONObject(fwc);
		addFWC = masterService.addFWC(json, request, response);
		return addFWC;
	}

	@RequestMapping(value = "/getAllFWC", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllFWC(@RequestBody Map<String, Object> payload, HttpServletRequest request,
			HttpServletResponse response) {
		String fwc = "";
		JSONObject jsonObj = new JSONObject(payload);
		fwc = masterService.getAllFWC(jsonObj, request, response);
		return fwc;
	}

	@RequestMapping(value = "/updateFWC", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateFWC(@RequestBody HashMap<String, Object> fwc, HttpServletRequest request,
			HttpServletResponse response) {
		String updateFWC = "";
		updateFWC = masterService.updateFWC(fwc, request, response);
		return updateFWC;
	}

	@RequestMapping(value = "/statusFWC", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String statusFWC(@RequestBody HashMap<String, Object> fwc, HttpServletRequest request,
			HttpServletResponse response) {
		String status = "";
		status = masterService.statusFWC(fwc, request, response);
		return status;
	}

	
	  @RequestMapping(value = "/getMIRoomList", method = RequestMethod.POST,
	  produces = "application/json", consumes = "application/json") 
	  public String getMIRoomList(HttpServletRequest request, 
			  HttpServletResponse response) { 
	  String addMIRoom = "";
	  addMIRoom = masterService.getMIRoomList(request, response); 
	  return addMIRoom;
	  }
	  
	  /***********************************
			 * Disease Type Master
			 *************************************/
			/**
			 * @param request
			 * @param response
			 * @return
			 */
		  
		  @RequestMapping(value = "/addDiseaseType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addDiseaseType(@RequestBody Map<String, Object> diseasePayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addDiseaseType = "";
				JSONObject jsonObject = new JSONObject(diseasePayload);
				addDiseaseType = masterService.addDiseaseType(jsonObject, request, response);
				return addDiseaseType;
			}
		  
		  @RequestMapping(value = "/getAllDiseaseType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllDiseaseType(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllDiseaseType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllDiseaseType = masterService.getAllDiseaseType(jsonObject, request, response);
				return getAllDiseaseType;
			}
		  
		  @RequestMapping(value = "/updateDiseaseTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDiseaseTypeStatus(@RequestBody HashMap<String, Object> dis, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateDiseaseTypeStatus(dis, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateDiseaseType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDiseaseType(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateDisease = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateDisease = masterService.updateDiseaseType(jsonObject, request, response);
				return updateDisease;
			}
		  @RequestMapping(value = "/addDiseaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addDiseaseMapping(@RequestBody Map<String, Object> diseasePayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addDiseaseMapping = "";
				JSONObject jsonObject = new JSONObject(diseasePayload);
				addDiseaseMapping = masterService.addDiseaseMapping(jsonObject, request, response);
				return addDiseaseMapping;
			}
		  
		  @RequestMapping(value = "/getAllDiseaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllDiseaseMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllDiseaseMapping = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllDiseaseMapping = masterService.getAllDiseaseMapping(jsonObject, request, response);
				return getAllDiseaseMapping;
			}
		  
		  @RequestMapping(value = "/updateDiseaseMappingStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDiseaseMappingStatus(@RequestBody HashMap<String, Object> dis, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateDiseaseMappingStatus(dis, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateDiseaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDiseaseMapping(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateDisease = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateDisease = masterService.updateDiseaseMapping(jsonObject, request, response);
				return updateDisease;
			}
		  
		 
		   /**************************************
			 *MMU Master   dated 21-08-2021
			 **************************************************/
		  
		  @RequestMapping(value = "/addMMU", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addMMU(@RequestBody Map<String, Object> mmuPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String responseMMU = "";
				JSONObject jsonObject = new JSONObject(mmuPayload);
				responseMMU = masterService.addMMU(jsonObject, request, response);
				return responseMMU;
			}
		  
		  @RequestMapping(value = "/updateMMUStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateMMUStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateMMUStatus(statusPayload, request, response);
				return status;
			}
		  @RequestMapping(value = "/getAllMMU", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllMMU(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMU = masterService.getAllMMU(jsonObject, request, response);
				return getAllMMU;
			} 
		  
		  
		  @RequestMapping(value = "/updateMMU", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateMMU(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateMMU = masterService.updateMMU(jsonObject, request, response);
				return updateMMU;
			}
		  
		  @RequestMapping(value = "/getAllCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllCity(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllCity(jsonObject, request, response);
				return getAllCity;
			}
		  @RequestMapping(value = "/getIndendeCityList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getIndendeCityList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getIndendeCityList(jsonObject, request, response);
				return getAllCity;
			}
		  @RequestMapping(value = "/getAllMMUVendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllMMUVendor(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMUVendor = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMUVendor = masterService.getAllMMUVendor(jsonObject, request, response);
				return getAllMMUVendor;
			}
		  
		  @RequestMapping(value = "/getAllMMUType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllMMUType(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMUType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMUType = masterService.getAllMMUType(jsonObject, request, response);
				return getAllMMUType;
			}
		  
		  @RequestMapping(value = "/validateRegNo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String validateRegNo(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String validateRegNo = "";
				JSONObject jsonObject = new JSONObject(payload);
				validateRegNo = masterService.validateRegNo(jsonObject, request, response);
				return validateRegNo;
			}
		  
		  @RequestMapping(value = "/addUserType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addUserType(@RequestBody Map<String, Object> mmuPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addUserType = "";
				JSONObject jsonObject = new JSONObject(mmuPayload);
				addUserType = masterService.addUserType(jsonObject, request, response);
				return addUserType;
			}
		  
		  @RequestMapping(value = "/getAllUserType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllUserType(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllUserType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllUserType = masterService.getAllUserType(jsonObject, request, response);
				return getAllUserType;
			}
		  
		  @RequestMapping(value = "/updateUserTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUserTypeStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateUserTypeStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateUserType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUserType(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateUserType = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateUserType = masterService.updateUserType(jsonObject, request, response);
				return updateUserType;
			}
		  
		  @RequestMapping(value = "/addCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addCity(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addCity = "";
				JSONObject jsonObject = new JSONObject(cityPayload);
				addCity = masterService.addCity(jsonObject, request, response);
				return addCity;
			}
		  
		  @RequestMapping(value = "/updateCityStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateCityStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateCityStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateCity(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updateCity(jsonObject, request, response);
				return updateCity;
			}
		  
		  @RequestMapping(value = "/getAllDistrict", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllDistrict(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllDistrict = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllDistrict = masterService.getAllDistrict(jsonObject, request, response);
				return getAllDistrict;
			}
		  
		  @RequestMapping(value = "/addZone", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addZone(@RequestBody Map<String, Object> zonePayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addZone = "";
				JSONObject jsonObject = new JSONObject(zonePayload);
				addZone = masterService.addZone(jsonObject, request, response);
				return addZone;
			}
		  
		  @RequestMapping(value = "/getAllZone", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllZone(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllZone = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllZone = masterService.getAllZone(jsonObject, request, response);
				return getAllZone;
			}
		  
		  @RequestMapping(value = "/updateZoneStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateZoneStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateZoneStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateZone", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateZone(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateZone = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateZone = masterService.updateZone(jsonObject, request, response);
				return updateZone;
			}
		  
		  @RequestMapping(value = "/getAllSymptoms", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllSymptoms(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllZone = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllZone = masterService.getAllSymptoms(jsonObject, request, response);
				return getAllZone;
			}
		  
		  @RequestMapping(value = "/addWard", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addWard(@RequestBody Map<String, Object> wardPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addWard = "";
				JSONObject jsonObject = new JSONObject(wardPayload);
				addWard = masterService.addWard(jsonObject, request, response);
				return addWard;
			}
		  
		  @RequestMapping(value = "/getAllWard", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllWard(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllWard = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllWard = masterService.getAllWard(jsonObject, request, response);
				return getAllWard;
			}
		  
		  @RequestMapping(value = "/updateWardStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateWardStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateWardStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateWard", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateWard(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateWard = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateWard = masterService.updateWard(jsonObject, request, response);
				return updateWard;
			}
		  
		  @RequestMapping(value = "/addDistrict", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addDistrict(@RequestBody Map<String, Object> distPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addDistrict = "";
				JSONObject jsonObject = new JSONObject(distPayload);
				addDistrict = masterService.addDistrict(jsonObject, request, response);
				return addDistrict;
			}
		  
		  @RequestMapping(value = "/updateDistrictStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDistrictStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateDistrictStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateDistrict", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDistrict(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateWard = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateWard = masterService.updateDistrict(jsonObject, request, response);
				return updateWard;
			}
		  
		  @RequestMapping(value = "/addTreatmentInstructions", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addTreatmentInstructions(@RequestBody Map<String, Object> wardPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addTreatmentInstructions = "";
				JSONObject jsonObject = new JSONObject(wardPayload);
				addTreatmentInstructions = masterService.addTreatmentInstructions(jsonObject, request, response);
				return addTreatmentInstructions;
			}
		  
		  @RequestMapping(value = "/getAllTreatmentInstructions", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllTreatmentInstructions(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllTreatmentInstructions = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllTreatmentInstructions = masterService.getAllTreatmentInstructions(jsonObject, request, response);
				return getAllTreatmentInstructions;
			}
		  
		  @RequestMapping(value = "/updateTreatmentInstructionsStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateTreatmentInstructionsStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateTreatmentInstructionsStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateTreatmentInstructions", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateTreatmentInstructions(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateTreatmentInstructions = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateTreatmentInstructions = masterService.updateTreatmentInstructions(jsonObject, request, response);
				return updateTreatmentInstructions;
			}
		  
		  @RequestMapping(value = "/addSignSymtoms", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addSignSymtoms(@RequestBody Map<String, Object> signPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addSignSymtoms = "";
				JSONObject jsonObject = new JSONObject(signPayload);
				addSignSymtoms = masterService.addSignSymtoms(jsonObject, request, response);
				return addSignSymtoms;
			}
		  
		  @RequestMapping(value = "/getAllSignSymtoms", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllSignSymtoms(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllSignSymtoms = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllSignSymtoms = masterService.getAllSignSymtoms(jsonObject, request, response);
				return getAllSignSymtoms;
			}
		  
		  @RequestMapping(value = "/updateSignSymtomsStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateSignSymtomsStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateSignSymtomsStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateSignSymtoms", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateSignSymtoms(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateSignSymtoms = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateSignSymtoms = masterService.updateSignSymtoms(jsonObject, request, response);
				return updateSignSymtoms;
			}
		  
		  @RequestMapping(value = "/addLabour", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addLabour(@RequestBody Map<String, Object> wardPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addLabour = "";
				JSONObject jsonObject = new JSONObject(wardPayload);
				addLabour = masterService.addLabour(jsonObject, request, response);
				return addLabour;
			}
		  
		  @RequestMapping(value = "/getAllLabour", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllLabour(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllLabour = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllLabour = masterService.getAllLabour(jsonObject, request, response);
				return getAllLabour;
			}
		  
		  @RequestMapping(value = "/updateLabourStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateLabourStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateLabourStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateLabour", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateLabour(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateLabour = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateLabour = masterService.updateLabour(jsonObject, request, response);
				return updateLabour;
			}

			@RequestMapping(value = "/getAllPenalty", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllPenalty(@RequestBody Map<String, Object> payload) {
				return masterService.getAllPenalty(new JSONObject(payload));
			}

			@RequestMapping(value = "/addPenalty", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addPenalty(@RequestBody Map<String, Object> penaltyPayload) {
				return masterService.addPenalty(new JSONObject(penaltyPayload));
			}

			@RequestMapping(value = "/updatePenaltyStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updatePenaltyStatus(@RequestBody HashMap<String, Object> statusPayload) {
				return masterService.updatePenaltyStatus(statusPayload);
			}

			@RequestMapping(value = "/updatePenalty", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updatePenalty(@RequestBody HashMap<String, Object> payload) {

				return masterService.updatePenalty(new JSONObject(payload));
			}

			@RequestMapping(value = "/getAllEquipmentChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllEquipmentChecklist(@RequestBody Map<String, Object> payload) {
				return masterService.getAllEquipmentChecklist(new JSONObject(payload));
			}

			@RequestMapping(value = "/addEquipmentChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addEquipmentChecklist(@RequestBody Map<String, Object> payload) {
				return masterService.addEquipmentChecklist(new JSONObject(payload));
			}

			@RequestMapping(value = "/updateEquipmentChecklistStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateEquipmentChecklistStatus(@RequestBody HashMap<String, Object> statusPayload) {
				return masterService.updateEquipmentChecklistStatus(statusPayload);
			}

			@RequestMapping(value = "/updateEquipmentChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateEquipmentChecklist(@RequestBody HashMap<String, Object> payload) {
				return masterService.updateEquipmentChecklist(new JSONObject(payload));
			}

			@RequestMapping(value = "/getAllInspectionChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllInspectionChecklist(@RequestBody Map<String, Object> payload) {
				return masterService.getAllInspectionChecklist(new JSONObject(payload));
			}

			@RequestMapping(value = "/addInspectionChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addInspectionChecklist(@RequestBody Map<String, Object> payload) {
				return masterService.addInspectionChecklist(new JSONObject(payload));
			}

			@RequestMapping(value = "/updateInspectionChecklistStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateInspectionChecklistStatus(@RequestBody HashMap<String, Object> statusPayload) {
				return masterService.updateInspectionChecklistStatus(statusPayload);
			}

			@RequestMapping(value = "/updateInspectionChecklist", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateInspectionChecklist(@RequestBody HashMap<String, Object> payload) {
				return masterService.updateInspectionChecklist(new JSONObject(payload));
			}

		  @RequestMapping(value="/getMMUHierarchicalList", method=RequestMethod.POST, produces="application/json", consumes="application/json")
			public String getMMUHierarchicalList(@RequestBody HashMap<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
				return masterService.getMMUHierarchicalList(map,request,response);
			}
		  
		  @RequestMapping(value = "/getFrequentlyUsedSymptomsList", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getFrequentlyUsedSymptomsList(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String symptomsList = "";
				JSONObject jsonObject = new JSONObject(payload);
				symptomsList = masterService.getFrequentlyUsedSymptomsList(jsonObject, request, response);
				return symptomsList;
			}
		  
		  @RequestMapping(value = "/addDeptMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addDeptMapping(@RequestBody Map<String, Object> wardPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addDeptMapping = "";
				JSONObject jsonObject = new JSONObject(wardPayload);
				addDeptMapping = masterService.addDeptMapping(jsonObject, request, response);
				return addDeptMapping;
			}
		  
		  @RequestMapping(value = "/getAllDeptMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllDeptMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllDeptMapping = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllDeptMapping = masterService.getAllDeptMapping(jsonObject, request, response);
				return getAllDeptMapping;
			}
		  
		  @RequestMapping(value = "/updateDeptMappingStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDeptMappingStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateDeptMappingStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateDeptMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateDeptMapping(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateDeptMapping = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateDeptMapping = masterService.updateDeptMapping(jsonObject, request, response);
				return updateDeptMapping;
			}
		  
		  
		  /*********************************************
			 *Supplier Type Master
			 ********************************************************/
			@RequestMapping(value = "/getAllSupplierType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllSupplierType(@RequestBody Map<String, Object> supplierTypePayload, HttpServletRequest request,
					HttpServletResponse response) {
				String allSupplierType = "";
				JSONObject jsonObject = new JSONObject(supplierTypePayload);
				allSupplierType = masterService.getAllSupplierType(jsonObject, request, response);
				return allSupplierType;
			}
			
			@RequestMapping(value = "/addSupplierType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addSupplierType(@RequestBody Map<String, Object> supplierTypePayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addSupplierType = "";
				JSONObject jsonObject = new JSONObject(supplierTypePayload);
				addSupplierType = masterService.addSupplierType(jsonObject, request, response);
				return addSupplierType;
			}

			@RequestMapping(value = "/updateSupplierType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateSupplierType(@RequestBody HashMap<String, Object> supplierTypePayload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateSupplierType = "";
				JSONObject jsonObject = new JSONObject(supplierTypePayload);
				updateSupplierType = masterService.updateSupplierType(jsonObject, request, response);
				return updateSupplierType;
			}

			@RequestMapping(value = "/updateSupplierTypeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateSupplierTypeStatus(@RequestBody HashMap<String, Object> supplierTypePayload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateSupplierTypeStatus = "";
				JSONObject jsonObject = new JSONObject(supplierTypePayload);
				updateSupplierTypeStatus = masterService.updateSupplierTypeStatus(jsonObject, request, response);
				return updateSupplierTypeStatus;
			}
			
			@RequestMapping(value = "/getAllAuditorName", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllAuditorName(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String AllAuditorName = "";
				JSONObject jsonObject = new JSONObject(payload);
				AllAuditorName = masterService.getAllAuditorName(jsonObject, request, response);
				return AllAuditorName;
			}	
			
			/*********************************************
			* Treatment Advice Master
			********************************************************/
			@RequestMapping(value = "/getAllTreatmentAdvice", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllTreatmentAdvice(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String AllTreatmentAdvice = "";
				JSONObject jsonObject = new JSONObject(payload);
				AllTreatmentAdvice = masterService.getAllTreatmentAdvice(jsonObject, request, response);
				return AllTreatmentAdvice;
			}

			@RequestMapping(value = "/addTreatmentAdvice", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addTreatmentAdvice(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String addTreatmentAdvice = "";
				JSONObject jsonObject = new JSONObject(payload);
				addTreatmentAdvice = masterService.addTreatmentAdvice(jsonObject, request, response);
				return addTreatmentAdvice;
			}

			@RequestMapping(value = "/updateTreatmentAdvice", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateTreatmentAdvice(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateTreatmentAdvice = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateTreatmentAdvice = masterService.updateTreatmentAdvice(jsonObject, request, response);
				return updateTreatmentAdvice;
			}

			@RequestMapping(value = "/updateTreatmentAdviceStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateTreatmentAdviceStatus(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateTreatmentAdviceStatus = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateTreatmentAdviceStatus = masterService.updateTreatmentAdviceStatus(jsonObject, request, response);
				return updateTreatmentAdviceStatus;
			}
			
			/**************************************
			 * Manufacturer Master
			 **************************************************/
			
			@RequestMapping(value = "/addManufacturer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addManufacturer(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String addManufacturer = "";
				JSONObject jsonObject = new JSONObject(payload);
				addManufacturer = masterService.addManufacturer(jsonObject, request, response);
				return addManufacturer;
			}
		  
		  @RequestMapping(value = "/getAllManufacturer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllManufacturer(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllManufacturer = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllManufacturer = masterService.getAllManufacturer(jsonObject, request, response);
				return getAllManufacturer;
			}
		  
		  @RequestMapping(value = "/updateManufacturerStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateManufacturerStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateManufacturerStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateManufacturer", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateManufacturer(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateManufacturer = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateManufacturer = masterService.updateManufacturer(jsonObject, request, response);
				return updateManufacturer;
			}
		  
		  @RequestMapping(value = "/getLegacyCityMasterData", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getLegacyCityMasterData(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCityCluster = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCityCluster = masterService.getLegacyCityMasterData(jsonObject, request, response);
				return getAllCityCluster;
			}
		  
			////////////////////////////saveOrUpdateLgacyData ////////////////////////
						
			@RequestMapping(value="/saveOrUpdateLgacyData", method = RequestMethod.POST,produces="application/json",consumes="application/json")
			public String saveOrUpdateLgacyData(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response)
			{
				return masterService.saveOrUpdateLgacyData(jsondata, request, response);
			}
	@RequestMapping(value = "/addCityMmuMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addCityMmuMapping(@RequestBody Map<String, Object> penaltyPayload) {
		return masterService.addCityMmuMapping(new JSONObject(penaltyPayload));
	}

	@RequestMapping(value = "/getAllCityMmuMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCityMmuMapping(@RequestBody Map<String, Object> payload) {
		return masterService.getAllCityMmuMapping(new JSONObject(payload));
	}

	@RequestMapping(value = "/addCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addCluster(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
							 HttpServletResponse response) {
		String addCluster = "";
		JSONObject jsonObject = new JSONObject(cityPayload);
		addCluster = masterService.addCluster(jsonObject, request, response);
		return addCluster;
	}

	@RequestMapping(value = "/getAllCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCluster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
								HttpServletResponse response) {
		String getAllCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllCluster = masterService.getAllCluster(jsonObject, request, response);
		return getAllCluster;
	}

	@RequestMapping(value = "/addCityCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addCityCluster(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
								 HttpServletResponse response) {
		String addCityCluster = "";
		JSONObject jsonObject = new JSONObject(cityPayload);
		addCityCluster = masterService.addCityCluster(jsonObject, request, response);
		return addCityCluster;
	}

	@RequestMapping(value = "/getAllCityCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCityCluster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
									HttpServletResponse response) {
		String getAllCityCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllCityCluster = masterService.getAllCityCluster(jsonObject, request, response);
		return getAllCityCluster;
	}

	@RequestMapping(value = "/addDistrictCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addDistrictCluster(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
									 HttpServletResponse response) {
		String addDistrictCluster = "";
		JSONObject jsonObject = new JSONObject(cityPayload);
		addDistrictCluster = masterService.addDistrictCluster(jsonObject, request, response);
		return addDistrictCluster;
	}

	@RequestMapping(value = "/getAllDistrictCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllDistrictCluster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
										HttpServletResponse response) {
		String getAllDistrictCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllDistrictCluster = masterService.getAllDistrictCluster(jsonObject, request, response);
		return getAllDistrictCluster;
	}

	@RequestMapping(value = "/getClusterByDistrict", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getClusterByDistrict(@RequestBody Map<String, Object> payload, HttpServletRequest request,
									   HttpServletResponse response) {
		String getClusterByDistrict = "";
		JSONObject jsonObject = new JSONObject(payload);
		getClusterByDistrict = masterService.getClusterByDistrict(jsonObject, request, response);
		return getClusterByDistrict;
	}

	@RequestMapping(value = "/getCityByCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getCityByCluster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
								   HttpServletResponse response) {
		String getCityByCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getCityByCluster = masterService.getCityByCluster(jsonObject, request, response);
		return getCityByCluster;
	}

	@RequestMapping(value = "/updateClusterStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateClusterStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
									  HttpServletResponse response) {
		String status = "";
		status = masterService.updateClusterStatus(statusPayload, request, response);
		return status;
	}

	@RequestMapping(value = "/updateDistrictClusterStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateDistrictClusterStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
											  HttpServletResponse response) {
		String status = "";
		status = masterService.updateDistrictClusterStatus(statusPayload, request, response);
		return status;
	}
	@RequestMapping(value = "/updateCityClusterStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateCityClusterStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
										  HttpServletResponse response) {
		String status = "";
		status = masterService.updateCityClusterStatus(statusPayload, request, response);
		return status;
	}


	//==========================================
	//             Society Master
	//==========================================

	@RequestMapping(value = "/getAllSociety", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllSociety(@RequestBody Map<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		String getAllSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllSociety = masterService.getAllSociety(jsonObject, request, response);
		return getAllSociety;
	}

	@RequestMapping(value = "/addSociety", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addSociety(@RequestBody Map<String, Object> societyPayload, HttpServletRequest request, HttpServletResponse response) {
		String addSociety = "";
		JSONObject jsonObject = new JSONObject(societyPayload);
		addSociety = masterService.addSociety(jsonObject, request, response);
		return addSociety;
	}

	@RequestMapping(value = "/updateSocietyStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSocietyStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		String status = "";
		status = masterService.updateSocietyStatus(statusPayload, request, response);
		return status;
	}

	@RequestMapping(value = "/updateSociety", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSociety(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		String updateSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateSociety = masterService.updateSociety(jsonObject, request, response);
		return updateSociety;
	}

	//==========================================
	//            Society-City Mapping
	//==========================================

	@RequestMapping(value = "/getCityList", method = RequestMethod.POST,produces = "application/json", consumes = "application/json")
	public String getCityList(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		return masterService.getCityList(payload,request, response);
	}

	@RequestMapping(value = "/getSocietyList", method = RequestMethod.POST,produces = "application/json", consumes = "application/json")
	public String getSocietyList(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		return masterService.getSocietyList(payload,request, response);
	}

	@RequestMapping(value = "/getAllCitySociety", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllCitySociety(@RequestBody Map<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		String getAllSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllSociety = masterService.getAllCitySociety(jsonObject, request, response);
		return getAllSociety;
	}

	@RequestMapping(value = "/addSocietyCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String mapSocietyCity(@RequestBody Map<String, Object> societyPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(societyPayload);
		return  masterService.addSocietyCity(jsonObject, request, response);
	}

	@RequestMapping(value = "/updateSocietyCityStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSocietyCityStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		return masterService.updateSocietyCityStatus(statusPayload, request, response);
	}

	@RequestMapping(value = "/updateSocietyCity", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateSocietyCity(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		String updateSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateSociety = masterService.addSocietyCity(jsonObject, request, response);
		return updateSociety;
	}

	//==========================================
	//            Fund Scheme Master
	//==========================================

	@RequestMapping(value = "/getAllFundScheme", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getAllFundScheme(@RequestBody Map<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		String getAllSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		getAllSociety = masterService.getAllFundScheme(jsonObject, request, response);
		return getAllSociety;
	}

	@RequestMapping(value = "/addFundSchemeMaster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String addFundSchemeMaster(@RequestBody Map<String, Object> societyPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(societyPayload);
		return  masterService.addFundSchemeMaster(jsonObject, request, response);
	}

	@RequestMapping(value = "/updateFundSchemeMaster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateFundSchemeMaster(@RequestBody HashMap<String, Object> payload,HttpServletRequest request, HttpServletResponse response) {
		String updateSociety = "";
		JSONObject jsonObject = new JSONObject(payload);
		updateSociety = masterService.addFundSchemeMaster(jsonObject, request, response);
		return updateSociety;
	}

	@RequestMapping(value = "/updateFundSchemeStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String updateFundSchemeStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		return masterService.updateFundSchemeStatus(statusPayload, request, response);
	}

	@RequestMapping(value = "/getMMUByCityCluster", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getMMUByCityCluster(@RequestBody Map<String, Object> payload, HttpServletRequest request,
									  HttpServletResponse response) {
		String getMMUByCityCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getMMUByCityCluster = masterService.getMMUByCityCluster(jsonObject, request, response);
		return getMMUByCityCluster;
	}



	//================================================================
	//            GET  CITY FROM MMU CITY MAPPING
	//================================================================

	@RequestMapping(value = "/getMmuByCityMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String getMmuByCityMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		String getCityByCluster = "";
		JSONObject jsonObject = new JSONObject(payload);
		getCityByCluster = masterService.getMmuByCityMapping(jsonObject, request, response);
		return getCityByCluster;
	}
	
	 @RequestMapping(value = "/addAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String addAuthority(@RequestBody Map<String, Object> wardPayload, HttpServletRequest request,
				HttpServletResponse response) {
			String addWard = "";
			JSONObject jsonObject = new JSONObject(wardPayload);
			addWard = masterService.addAuthority(jsonObject, request, response);
			return addWard;
		}
	  
	  @RequestMapping(value = "/getAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String getAuthority(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
				HttpServletResponse response) {
			String getAllAuthority = "";
			getAllAuthority = masterService.getAuthority(payload, request);
			return getAllAuthority;
		}
	  
	  @RequestMapping(value = "/updateAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String updateAuthority(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
				HttpServletResponse response) {
			String status = "";
			status = masterService.updateAuthority(statusPayload, request, response);
			return status;
		}
	  
	  @RequestMapping(value = "/updateCityMMuStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String updateCityMMuStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
				HttpServletResponse response) {
			String status = "";
			status = masterService.updateCityMMuStatus(statusPayload, request, response);
			return status;
		}
	  
	  @RequestMapping(value = "/updateCityMMUMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String updateCityMMUMapping(@RequestBody HashMap<String, Object> payload,
				HttpServletRequest request, HttpServletResponse response) {
			String updateWard = "";
			JSONObject jsonObject = new JSONObject(payload);
			updateWard = masterService.updateCityMMUMapping(jsonObject, request, response);
			return updateWard;
		}
	  
		@RequestMapping(value = "/getMmuByDistrictId", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String getMmuByDistrictId(@RequestBody Map<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
			String getCityByCluster = "";
			JSONObject jsonObject = new JSONObject(payload);
			getCityByCluster = masterService.getMmuByDistrictId(jsonObject, request, response);
			return getCityByCluster;
		}
		
		 @RequestMapping(value = "/getStoreFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getStoreFinancialYear(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getStoreFinancialYear = "";
				getStoreFinancialYear = masterService.getStoreFinancialYear(payload, request);
				return getStoreFinancialYear;
			}
		 
		 @RequestMapping(value = "/getMasHeadType", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getMasHeadType(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getMasHeadType = "";
				getMasHeadType = masterService.getMasHeadType(payload, request);
				return getMasHeadType;
			}
		 @RequestMapping(value = "/getAllMasHead", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllMasHead(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllMasHead(jsonObject, request, response);
				return getAllCity;
			}
		 
		  
		  @RequestMapping(value = "/updateHead", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateHead(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updateHead(jsonObject, request, response);
				return updateCity;
			}
		  
		  
		  
		  @RequestMapping(value = "/updateHeadStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateHeadStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateHeadStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/addHead", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addHead(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addCity = "";
				JSONObject jsonObject = new JSONObject(cityPayload);
				addCity = masterService.addHead(jsonObject, request, response);
				return addCity;
			}
		  
		  @RequestMapping(value = "/addApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addApprovalAuthority(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addCity = "";
				JSONObject jsonObject = new JSONObject(cityPayload);
				addCity = masterService.addApprovalAuthority(jsonObject, request, response);
				return addCity;
			}
		  
		  @RequestMapping(value = "/getAllApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllApprovalAuthority(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllApprovalAuthority(jsonObject, request, response);
				return getAllCity;
			}
		  
		  @RequestMapping(value = "/getAllOrderNumber", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllOrderNumber(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllUserType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllUserType = masterService.getAllOrderNumber(jsonObject, request, response);
				return getAllUserType;
			}
		  
		  
		  @RequestMapping(value = "/updateApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateApprovalAuthority(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updateApprovalAuthority(jsonObject, request, response);
				return updateCity;
			}
		  
		  
		  
		  @RequestMapping(value = "/updateApprovalAuthorityStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateApprovalAuthorityStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateApprovalAuthorityStatus(statusPayload, request, response);
				return status;
			}

		  @RequestMapping(value = "/checkFinalApproval", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String checkFinalApproval(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllUserType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllUserType = masterService.checkFinalApproval(jsonObject, request, response);
				return getAllUserType;
			}
		  
		  @RequestMapping(value = "/addApprovalAuthorityMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addApprovalAuthorityMapping(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addCity = "";
				JSONObject jsonObject = new JSONObject(cityPayload);
				addCity = masterService.addApprovalAuthorityMapping(jsonObject, request, response);
				return addCity;
			}
		  
		  @RequestMapping(value = "/getAllApprovalAuthorityMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllApprovalAuthorityMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllApprovalAuthorityMapping(jsonObject, request, response);
				return getAllCity;
			}
		  @RequestMapping(value = "/updateApprovalAuthorityMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateApprovalAuthorityMapping(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updateApprovalAuthorityMapping(jsonObject, request, response);
				return updateCity;
			}
		  
		  
		  
		  @RequestMapping(value = "/updateApprovalAuthorityMappingStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateApprovalAuthorityMappingStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateApprovalAuthorityMappingStatus(statusPayload, request, response);
				return status;
			}
		  
		  	@RequestMapping(value = "/getAllFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllFinancialYear(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllFinancialYear(jsonObject, request, response);
				return getAllCity;
			}
		  	 @RequestMapping(value = "/addFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
				public String addFinancialYear(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
						HttpServletResponse response) {
					String addCity = "";
					JSONObject jsonObject = new JSONObject(cityPayload);
					addCity = masterService.addFinancialYear(jsonObject, request, response);
					return addCity;
				}
		  	@RequestMapping(value = "/updateFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateFinancialYear(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updateFinancialYear(jsonObject, request, response);
				return updateCity;
			}
		  
		  
		  
		  @RequestMapping(value = "/updateFinancialYearStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateFinancialYearStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateFinancialYearStatus(statusPayload, request, response);
				return status;
			}
		  	

		  @RequestMapping(value = "/checkFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String checkFinancialYear(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllUserType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllUserType = masterService.checkFinancialYear(jsonObject, request, response);
				return getAllUserType;
			}
		
		  @RequestMapping(value = "/addPenalityApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addPenalityApprovalAuthority(@RequestBody Map<String, Object> cityPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String addCity = "";
				JSONObject jsonObject = new JSONObject(cityPayload);
				addCity = masterService.addPenalityApprovalAuthority(jsonObject, request, response);
				return addCity;
			}
		
		  @RequestMapping(value = "/getAllPenalityApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllPenalityApprovalAuthority(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllCity = masterService.getAllPenalityApprovalAuthority(jsonObject, request, response);
				return getAllCity;
			}

		  @RequestMapping(value = "/updatePenalityApprovalAuthority", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updatePenalityApprovalAuthority(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateCity = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateCity = masterService.updatePenalityApprovalAuthority(jsonObject, request, response);
				return updateCity;
			}
		  
		  
		  
		  @RequestMapping(value = "/updatePenalityApprovalAuthorityStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updatePenalityApprovalAuthorityStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updatePenalityApprovalAuthorityStatus(statusPayload, request, response);
				return status;
			}

		  @RequestMapping(value = "/getAllMasStoreSupplier", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllMasStoreSupplier(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMUType = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMUType = masterService.getAllMasStoreSupplier(jsonObject, request, response);
				return getAllMMUType;
			}
				  
		  @RequestMapping(value = "/addMMUManufac", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addMMUManufac(@RequestBody Map<String, Object> mmuPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String responseMMU = "";
				JSONObject jsonObject = new JSONObject(mmuPayload);
				responseMMU = masterService.addMMUManufac(jsonObject, request, response);
				return responseMMU;
			}

		  @RequestMapping(value = "/updateUpssManuStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUpssManuStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateUpssManuStatus(statusPayload, request, response);
				return status;
			}
		
		  @RequestMapping(value = "/updateUpssManu", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUpssManu(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateMMU = masterService.updateUpssManu(jsonObject, request, response);
				return updateMMU;
			}

		  @RequestMapping(value = "/getAllUpssManu", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllUpssManu(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMU = masterService.getAllUpssManu(jsonObject, request, response);
				return getAllMMU;
			} 
		  
		  @RequestMapping(value = "/getMasPhase", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		  public String getMasPhase(@RequestBody Map<String, Object> payload, HttpServletRequest request,
		  		HttpServletResponse response) {
		  	String rank = "";
		  	JSONObject jsonObj = new JSONObject(payload);
		  	rank = masterService.getMasPhase(jsonObj, request, response);
		  	return rank;
		  }
		  
		  @RequestMapping(value = "/addUpssPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addUpssPhaseMapping(@RequestBody Map<String, Object> mmuPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String responseMMU = "";
				JSONObject jsonObject = new JSONObject(mmuPayload);
				responseMMU = masterService.addUpssPhaseMapping(jsonObject, request, response);
				return responseMMU;
			}
		  
		  @RequestMapping(value = "/getAllUpssPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllUpssPhaseMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMU = masterService.getAllUpssPhaseMapping(jsonObject, request, response);
				return getAllMMU;
			} 
		  
		  @RequestMapping(value = "/updateUpssPhaseStatus", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUpssPhaseStatus(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
					HttpServletResponse response) {
				String status = "";
				status = masterService.updateUpssPhaseStatus(statusPayload, request, response);
				return status;
			}
		  
		  @RequestMapping(value = "/updateUpssPhase", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String updateUpssPhase(@RequestBody HashMap<String, Object> payload,
					HttpServletRequest request, HttpServletResponse response) {
				String updateMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				updateMMU = masterService.updateUpssPhase(jsonObject, request, response);
				return updateMMU;
			}
		  
		  @RequestMapping(value = "/getStoreFutureFinancialYear", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getStoreFutureFinancialYear(@RequestBody HashMap<String, String> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getStoreFutureFinancialYear = "";
				getStoreFutureFinancialYear = masterService.getStoreFutureFinancialYear(payload, request);
				return getStoreFutureFinancialYear;
			}
		  
		  @RequestMapping(value = "/getAllUpssManufactureMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllUpssManufactureMapping(@RequestBody Map<String, Object> payload, HttpServletRequest request,
					HttpServletResponse response) {
				String getAllMMU = "";
				JSONObject jsonObject = new JSONObject(payload);
				getAllMMU = masterService.getAllUpssManufactureMapping(jsonObject, request, response);
				return getAllMMU;
			} 
		  
		  @RequestMapping(value = "/addCityMmuPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String addCityMmuPhaseMapping(@RequestBody Map<String, Object> penaltyPayload) {
				return masterService.addCityMmuPhaseMapping(new JSONObject(penaltyPayload));
			}

			@RequestMapping(value = "/getAllCityMmuPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
			public String getAllCityMmuPhaseMapping(@RequestBody Map<String, Object> payload) {
				return masterService.getAllCityMmuPhaseMapping(new JSONObject(payload));
			}
			
			 @RequestMapping(value = "/updateCityMMuStatusPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
				public String updateCityMMuStatusPhaseMapping(@RequestBody HashMap<String, Object> statusPayload, HttpServletRequest request,
						HttpServletResponse response) {
					String status = "";
					status = masterService.updateCityMMuStatusPhaseMapping(statusPayload, request, response);
					return status;
				}
			  
			  @RequestMapping(value = "/updateCityMMUPhaseMapping", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
				public String updateCityMMUPhaseMapping(@RequestBody HashMap<String, Object> payload,
						HttpServletRequest request, HttpServletResponse response) {
					String updateWard = "";
					JSONObject jsonObject = new JSONObject(payload);
					updateWard = masterService.updateCityMMUPhaseMapping(jsonObject, request, response);
					return updateWard;
				}
		
}
