package com.mmu.services.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mmu.services.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MasterDao;
import com.mmu.services.service.MasterService;
import com.mmu.services.utils.HMSUtil;

import javassist.CtBehavior;

@Repository
public class MasterServiceImpl implements MasterService {
	@Autowired
	MasterDao md;
	/*
	 * @Autowired ShoDao shoDao;
	 */

	/////////////////////// MasDepartmrent get Method /////////////////////////
	@Override
	public String departmentList(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if (jsondata.get("EMPLOYEE_ID") == null || jsondata.get("EMPLOYEE_ID").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID as a  key or value or it is null\"}";
			} else {
				MasEmployee checkEmp = md.checkEmp(Long.parseLong(jsondata.get("EMPLOYEE_ID").toString()));
				if (checkEmp != null) {
					List<MasDepartment> mst_depart = md.getDepartmentList();
					if (mst_depart.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						json.put("departmentList", checkEmp);
						json.put("departmentList", mst_depart);
						json.put("msg", "department List  get  sucessfull... ");
						json.put("status", "1");

					}

				} else {
					return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";

				}

				return json.toString();
			}
		} finally {
			//System.out.println("Exception Occured");
		}
	}

	//////////////////////// Get Master ICD Business Logic //////////////////////

	@Override
	public String getICD(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
			if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals("")) {
				return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
			} else {
				MasEmployee checkEmp = md.checkEmp(Long.parseLong(jsondata.get("employeeId").toString()));
				if (checkEmp != null) {
					List<MasIcd> mst_icd = md.getIcd();
					if (mst_icd.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						json.put("ICDList", checkEmp);
						json.put("ICDList", mst_icd);
						json.put("msg", "ICD List  get  sucessfull... ");
						json.put("status", "1");

					}

				} else {
					return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE_ID Not Found\"}";

				}

				return json.toString();
			}
		} finally {
			//("Exception Occured");
		}
	}

	@Override
	public String getAllStates(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasState> sList = new ArrayList<MasState>();
		Map<String, List<MasState>> stateMap = md.getAllStates(jsonObject);
		if (stateMap.get("stateList") != null) {
			sList = stateMap.get("stateList");

			if (sList != null && sList.size() > 0) {
				json.put("data", sList);
				json.put("count", sList.size());
				json.put("status", 1);
			} else {
				json.put("data", sList);
				json.put("count", sList.size());
				json.put("msg", "State Not Found");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "Data Not Found... ");
			json.put("status", 0);
		}

		return json.toString();

	}

	@Override
	public String addCommand(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		if (json != null) {

			MasCommand masCommand = new MasCommand();

			masCommand.setCommandCode(json.get("commandCode").toString().toUpperCase());
			masCommand.setCommandName(json.get("commandName").toString().toUpperCase());

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			//masCommand.setLastChgDate(date);

			long userId = Long.parseLong(json.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);
			//masCommand.setUser(user);

			//masCommand.setStatus("Y");

			MasCommandType commandType = new MasCommandType();
			commandType.setCommandtypeId(Long.parseLong(json.get("commandtypeId").toString()));

			//masCommand.setMasCommandType(commandType);

			List<MasCommand> masCmd = md.validateMasCommand(masCommand.getCommandCode().toString(),
					masCommand.getCommandName().toString());
			if (masCmd.size() != 0) {
				if (masCmd != null && masCmd.size() > 0) {
					if (masCmd.get(0).getCommandCode().equalsIgnoreCase(json.get("commandCode").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Command Code already Exists\"}";
					}
					if (masCmd.get(0).getCommandName().equalsIgnoreCase(json.get("commandName").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Command Name already Exists\"}";
					}
				}
			} else {
				String masCmdObj = md.addMasCommand(masCommand);

				if (masCmdObj != null && masCmdObj.equalsIgnoreCase("200")) {
					jsonObj.put("status", 1);
					jsonObj.put("msg", "Record Added Successfully");

				} else if (masCmdObj != null && masCmdObj.equalsIgnoreCase("403")) {
					jsonObj.put("status", 0);
					jsonObj.put("msg", "Record Not Added");

				} else {
					jsonObj.put("msg", masCmdObj);
					jsonObj.put("status", 0);
				}
			}

		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();
	}

	@Override
	public String getCommand(HashMap<String, Object> command, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (command.get("commandName") == null && command.get("commandName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"Command Name is not available !!!\"}";
		} else {
			MasCommand chkCommand = md.chkCommand(command.get("commandName").toString());
			if (chkCommand != null) {
				List<MasCommand> masCmdLst = md.getCommand(command.get("commandName").toString());
				if (masCmdLst != null && masCmdLst.size() > 0) {

					jsonObject.put("masCmdLst", masCmdLst);
					jsonObject.put("msg", "List of command successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String getAllCommand(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasCommand> cList = new ArrayList<MasCommand>();

		Map<String, List<MasCommand>> cmdMap = md.getAllCommand(jsonObj);
		List cListObj = new ArrayList();
		List totalMatches = new ArrayList();
		if (cmdMap.get("masCmdList") != null) {
			cList = cmdMap.get("masCmdList");
			totalMatches = cmdMap.get("totalMatches");

			for (MasCommand mCommand : cList) {
				HashMap<String, Object> mapObj = new HashMap<String, Object>();

				mapObj.put("cmdId", mCommand.getCommandId());
				mapObj.put("cmdCode", mCommand.getCommandCode());
				mapObj.put("cmdName", mCommand.getCommandName());
				
			}

			if (cListObj != null && cListObj.size() > 0) {
				json.put("data", cListObj);

				json.put("count", totalMatches.size());
				json.put("status", 1);
			} else {
				json.put("data", cListObj);
				json.put("count", totalMatches.size());
				json.put("msg", "Data not found");
				json.put("status", 0);
			}
		}

		return json.toString();
	}

	@Override
	public String updateCommand(HashMap<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (command.get("commandCode") != null && !command.get("commandCode").toString().equalsIgnoreCase("")) {

			String cmdUpdate = md.updateCommand(Long.parseLong(command.get("commandId").toString()),
					command.get("commandCode").toString().toString(), command.get("commandName").toString(),
					Long.parseLong(command.get("commandtypeId").toString()),
					Long.parseLong(command.get("userId").toString()));

			if (cmdUpdate != null && !cmdUpdate.equalsIgnoreCase("")) {
				json.put("cmdUpdate", cmdUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (cmdUpdate == null && cmdUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated!!!");
				json.put("status", 0);
			}

			else {
				json.put("msg", "Command Code is not available");
				json.put("status", 0);

			}

		} else {
			json.put("msg", "Command Code is not available");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String statusCommand(HashMap<String, Object> command, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (command.get("commandCode") != null && !command.get("commandCode").toString().equalsIgnoreCase("")) {
			MasCommand chkCommand = md.chkCommand(command.get("commandCode").toString());

			if (chkCommand != null) {

				String masCmdStatus = md.updateCommandStatus(Long.parseLong(command.get("commandId").toString()),
						command.get("commandCode").toString(), command.get("status").toString(),
						Long.parseLong(command.get("userId").toString()));

				if (masCmdStatus != null && masCmdStatus.equalsIgnoreCase("200")) {
					json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Command Code is not available\"}";
		}

		return json.toString();
	}

	/********************************
	 * UNIT MASTER
	 ***********************************************/
	@Override
	public String getAllUnit(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasUnit> uList = new ArrayList<MasUnit>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUnit>> mapUnit = md.getAllUnit(jsondata);
			List totalMatches = new ArrayList();
			if (mapUnit.get("masUnitList") != null) {
				uList = mapUnit.get("masUnitList");
				totalMatches = mapUnit.get("totalMatches");
				for (MasUnit unit : uList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (unit != null) {
						mapObj.put("unitId", unit.getUnitId());
						mapObj.put("unitName", unit.getUnitName());
						
						mapObj.put("unitType", unit.getMasUnittype().getUnitTypeName());
						
						mapObj.put("unitTypeId", unit.getMasUnittype().getUnitTypeId());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addUnits(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasUnit masUnit = new MasUnit();
			MasCommand command = new MasCommand();

			masUnit.setUnitName(jsondata.get("unitName").toString().toUpperCase());
			command.setCommandId(Long.parseLong(jsondata.get("commandId").toString()));


			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users users = new Users();
			users.setUserId(userId);			

			MasUnitType unitType = new MasUnitType();
			unitType.setUnitTypeId(Long.parseLong(jsondata.get("unitTypeId").toString()));
			masUnit.setMasUnittype(unitType);

			List<MasUnit> chkUnitLst = md.validateUnit(masUnit.getUnitName());
			if (chkUnitLst != null && chkUnitLst.size() > 0) {
				if (chkUnitLst.get(0).getUnitName().equalsIgnoreCase(jsondata.get("unitName").toString())) {

					return "{\"status\":\"2\",\"msg\":\"Unit Name already Exists\"}";
				}

			} else {
				String addUnit = md.addMasUnit(masUnit);
				if (addUnit != null && addUnit.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String getCommandList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasCommand> cList = md.getCommandList();
		if (cList != null && cList.size() > 0) {
			json.put("data", cList);
			json.put("count", cList.size());
			json.put("status", 1);
		} else {
			json.put("data", cList);
			json.put("count", cList.size());
			json.put("msg", "No Record Found");
			json.put("status", 0);

		}
		return json.toString();
	}

	@Override
	public String getUnitTypeList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();

		List<MasUnitType> utList = md.getUnitTypeList();
		if (utList != null && utList.size() > 0) {
			json.put("data", utList);
			json.put("count", utList.size());
			json.put("status", 1);
		} else {
			json.put("data", utList);
			json.put("count", utList.size());
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateUnit(HashMap<String, Object> unitPayload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (unitPayload != null) {

			if (unitPayload.get("unitName") != null && !unitPayload.get("unitName").toString().equalsIgnoreCase("")
					|| unitPayload.get("commandId").toString() != null
					|| unitPayload.get("unitTypeId").toString() != null) {
				String updateUnit = md.updateUnit(Long.parseLong(unitPayload.get("unitId").toString()),
						unitPayload.get("unitName").toString(), Long.parseLong(unitPayload.get("commandId").toString()),
						unitPayload.get("unitAddress").toString(),
						Long.parseLong(unitPayload.get("unitTypeId").toString()),
						Long.parseLong(unitPayload.get("userId").toString()));
				if (updateUnit != null && !updateUnit.equalsIgnoreCase("")) {
					json.put("updateUnit", updateUnit);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("updateUnit", updateUnit);
					json.put("msg", "Record Not Updated");
					json.put("status", 0);
				}

			} else {
				json.put("msg", "Invalid Input Paramter");
				json.put("status", 0);
			}
		} else {
			json.put("msg", "Data Not Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getCommandTypeList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasCommandType> cmdTypList = md.getCommandTypeList();
		if (cmdTypList != null && cmdTypList.size() > 0) {

			jsonObj.put("data", cmdTypList);
			jsonObj.put("count", cmdTypList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", cmdTypList);
			jsonObj.put("count", cmdTypList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	/********************************************
	 * MAS HOSPITAL
	 *******************************************************/
	@Override
	public String addMasHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			MasHospital hospitalObj = new MasHospital();
			MasUnit unitObj = new MasUnit();
			unitObj.setUnitName(jsonObject.get("unitName").toString().toUpperCase());
			hospitalObj.setMasUnit(unitObj);
			hospitalObj.setHospitalName(jsonObject.get("hospitalName").toString().toUpperCase());
			hospitalObj.setStatus("Y");
			long userId = Long.parseLong(jsonObject.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);
			hospitalObj.setUser(user);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			hospitalObj.setLastChgDate(date);

			MasUnit masUnit = new MasUnit();
			masUnit.setUnitId(Long.parseLong(jsonObject.get("unitId").toString()));

			hospitalObj.setMasUnit(masUnit);

			List<MasHospital> hospitalList = md.validateMasHospital(hospitalObj.getHospitalCode(),
					hospitalObj.getHospitalName());
			if (hospitalList.size() != 0) {
				if (hospitalList != null && hospitalList.size() > 0) {

					if (hospitalList.get(0).getHospitalCode()
							.equalsIgnoreCase(jsonObject.get("hospitalCode").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Hospital Code already Exists\"}";
					}

					if (hospitalList.get(0).getHospitalName()
							.equalsIgnoreCase(jsonObject.get("hospitalName").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Hospital Name already Exists\"}";
					}
				}
			} else {
				String addHospital = md.addMasHospital(hospitalObj);

				if (addHospital != null && addHospital.equalsIgnoreCase("200")) {
					json.put("msg", "Record Added Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Added");
					json.put("status", 0);
				}
			}

		} else {
			json.put("msg", "Data Not Found Error");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String getUnitNameList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();

		List<MasUnit> mUnitList = md.getUnitNameList();
		if (mUnitList != null && mUnitList.size() > 0) {

			json.put("data", mUnitList);
			json.put("count", mUnitList.size());
			json.put("status", 1);
		} else {
			json.put("status", 0);
			json.put("count", mUnitList.size());
			json.put("msg", "No Record Found");
		}

		return json.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAllHospital(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			List<MasHospital> mHospitalList = new ArrayList<MasHospital>();
			List<MasUnit> mUnitList = new ArrayList<MasUnit>();
			List totalMatches = new ArrayList();
			List hList = new ArrayList();
			List<Object[]> objectList = new ArrayList<Object[]>();
			List<Object[]> objectList1 = new ArrayList<Object[]>();
			Map<String, Object> map = md.getAllHospital(jsonObject);
			objectList = (List<Object[]>) map.get("objectList1");
			totalMatches = (List<Object[]>) map.get("totalMatches");
			if (map.get("objectList1") != null) {

				for (Object[] list : objectList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (list != null) {
						mapObj.put("unitId", list[0]);
						mapObj.put("unitName", list[1]);
						mapObj.put("hospitalId", list[2] != null ? list[2] : "0");
						mapObj.put("status", list[3] != null ? list[3] : "0");

						hList.add(mapObj);
					}
				}

				if (hList != null && hList.size() > 0) {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			}

			if (map.get("unitData") != null) {
				if (map.get("unitData") != null) {
					json.put("data", map.get("unitData"));
					json.put("count", map.get("count"));
					json.put("msg", "Get Record Sucessfully.");
					json.put("status", 1);

				} else {
					json.put("data", map.get("unitData").toString());
					json.put("count", map.get("count"));
					json.put("msg", "Data not found");
					json.put("status", 0);
				}
			}
		} else {
			json.put("status", 0);
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}

	@Override
	public String updateHospitalMasterStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();

		if (jsonObject != null) {

			if (jsonObject.get("hospitalId") != null) {
				MasHospital mHospital = md.checkMiRoom(Long.parseLong(jsonObject.get("unitId").toString()),
						jsonObject.get("unitName").toString(), jsonObject.get("status").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (mHospital != null) {
					String masHospStatus = md.updateHospitalMasterStatus(
							Long.parseLong(jsonObject.get("hospitalId").toString()),
							jsonObject.get("status").toString(), Long.parseLong(jsonObject.get("userId").toString()));

					if (masHospStatus != null && masHospStatus.equalsIgnoreCase("200")) {
						json.put("masHospStatus", masHospStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	@Override
	public String updateHospitalDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("unitId").toString() != null
					&& !jsonObject.get("unitId").toString().equalsIgnoreCase("")) {
				MasHospital mHospital = md.checkMiRoom(Long.parseLong(jsonObject.get("unitId").toString()),
						jsonObject.get("unitName").toString(), jsonObject.get("status").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));
				
				if (mHospital != null) {
					json.put("mHospital", mHospital);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Data has not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}

		return json.toString();
	}

	/************************************************
	 * MAS RELATION
	 *****************************************************************/
	@Override
	public String getAllRelation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			List<MasRelation> mRelationList = new ArrayList<MasRelation>();
			List totalMatches = new ArrayList();
			List relList = new ArrayList();

			Map<String, List<MasRelation>> map = md.getAllRelation(jsonObject);
			if (map.get("mRelationList") != null) {
				mRelationList = map.get("mRelationList");
				totalMatches = map.get("totalMatches");

				for (MasRelation relation : mRelationList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (relation != null) {
						mapObj.put("relationId", relation.getRelationId());
						mapObj.put("relationCode", relation.getRelationCode());
						mapObj.put("relationName", relation.getRelationName());
						relList.add(mapObj);
					}
				}

				if (relList != null && relList.size() > 0) {
					json.put("data", relList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", relList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("status", 0);
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}

	@Override
	public String updateRelationDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (!(jsonObject.get("relationCode").toString()).isEmpty()) {

				String updateRelationDetail = md.updateRelationDetails(
						Long.parseLong(jsonObject.get("relationId").toString()),
						jsonObject.get("relationCode").toString(), jsonObject.get("relationName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateRelationDetail != null && updateRelationDetail.equalsIgnoreCase("200")) {
					json.put("updateRelationDetail", updateRelationDetail);
					json.put("msg", "Record Updated Successfully.");
					json.put("status", 1);
				} else {
					json.put("msg", "Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateRelationStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {

			if (Long.parseLong(jsonObject.get("relationCode").toString()) != 0
					&& Long.parseLong(jsonObject.get("relationCode").toString()) != -1) {
				MasRelation mRelation = md.checkMasRelation(Long.parseLong(jsonObject.get("relationCode").toString()));

				if (mRelation != null) {
					String masRelStatus = md.updateRelationStatus(
							Long.parseLong(jsonObject.get("relationId").toString()),
							Long.parseLong(jsonObject.get("relationCode").toString()),
							jsonObject.get("status").toString(), Long.parseLong(jsonObject.get("userId").toString()));

					if (masRelStatus != null && masRelStatus.equalsIgnoreCase("200")) {
						json.put("masRelStatus", masRelStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	@Override
	public String addRelation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsonObject.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);

			MasRelation masRelation = new MasRelation();
			masRelation.setRelationCode(jsonObject.get("relationCode").toString());
			masRelation.setRelationName(jsonObject.get("relationName").toString().toUpperCase());
			List<MasRelation> checkRel = md.validateRelation(Long.parseLong(jsonObject.get("relationCode").toString()),
					jsonObject.get("relationName").toString());

			if (checkRel != null && checkRel.size() > 0) {
				if (checkRel.get(0).getRelationCode()
						.equals(Long.parseLong(jsonObject.get("relationCode").toString()))) {

					json.put("status", 2);
					json.put("msg", "Relation Code already Exists");
				}

				if (checkRel.get(0).getRelationName().equalsIgnoreCase(jsonObject.get("relationName").toString())) {

					json.put("status", 2);
					json.put("msg", "Relation Name already Exists");
				}

			} else {

				String addRelationObj = md.addRelation(masRelation);

				if (addRelationObj != null && addRelationObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	/****************************************************
	 * MAS DISPOSAL
	 *********************************************************************/
	@Override
	public String addDisposal(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		return json.toString();
	}

	@Override
	public String getAllDisposal(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		

		return json.toString();
	}

	@Override
	public String updateDisposalDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		return json.toString();
	}

	@Override
	public String updateDisposalStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();


		return json.toString();
	}

	/*********************************
	 * MAS APPOINTMENT TYPE
	 *****************************************************************/
	@Override
	public String addAppointmentType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasAppointmentType masAppointmentType = new MasAppointmentType();
			masAppointmentType.setAppointmentTypeCode(jsonObject.get("appointmentTypeCode").toString().toUpperCase());
			masAppointmentType.setAppointmentTypeName(jsonObject.get("appointmentTypeName").toString().toUpperCase());
			masAppointmentType.setStatus("Y");
			long userId = Long.parseLong(jsonObject.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			masAppointmentType.setUser(users);
			masAppointmentType.setLastChgDate(date);

			List<MasAppointmentType> checkAppointment = md.validateAppointmentType(
					masAppointmentType.getAppointmentTypeCode(), masAppointmentType.getAppointmentTypeName());

			if (checkAppointment != null && checkAppointment.size() > 0) {
				if (checkAppointment.get(0).getAppointmentTypeCode()
						.equalsIgnoreCase(jsonObject.get("appointmentTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Appointment Type Code already Exists");
				}

				if (checkAppointment.get(0).getAppointmentTypeName()
						.equalsIgnoreCase(jsonObject.get("appointmentTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Appointment Type Name already Exists");
				}

			} else {

				String addppointmentTypeObj = md.addAppointmentType(masAppointmentType);

				if (addppointmentTypeObj != null && addppointmentTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String getAllAppointmentType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			List<MasAppointmentType> mAppointmentTypeList = new ArrayList<MasAppointmentType>();
			List totalMatches = new ArrayList();
			List appointmentTypeList = new ArrayList();

			Map<String, List<MasAppointmentType>> map = md.getAllAppointmentType(jsonObject);
			if (map.get("mAppointmentTypeList") != null) {
				mAppointmentTypeList = map.get("mAppointmentTypeList");
				totalMatches = map.get("totalMatches");

				for (MasAppointmentType appointmentType : mAppointmentTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (appointmentType != null) {
						mapObj.put("appointmentTypeId", appointmentType.getAppointmentTypeId());
						mapObj.put("appointmentTypeCode", appointmentType.getAppointmentTypeCode());
						mapObj.put("appointmentTypeName", appointmentType.getAppointmentTypeName());
						mapObj.put("status", appointmentType.getStatus());

						appointmentTypeList.add(mapObj);
					}
				}

				if (appointmentTypeList != null && appointmentTypeList.size() > 0) {
					json.put("data", appointmentTypeList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", appointmentTypeList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("status", 0);
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}

	@Override
	public String updateAppointmentTypeDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("appointmentTypeCode").toString() != null
					&& !jsonObject.get("appointmentTypeCode").toString().trim().equalsIgnoreCase("")) {
				List<MasAppointmentType> checkAppointment = md.validateAppointmentTypeUpdate(jsonObject.get("appointmentTypeName").toString());

				if (checkAppointment != null && checkAppointment.size() > 0) {
					if (checkAppointment.get(0).getAppointmentTypeName()
							.equalsIgnoreCase(jsonObject.get("appointmentTypeName").toString())) {

						json.put("status", 2);
						json.put("msg", "Appointment Type Name already Exists");
					}

				} else {

				String updateAppointmentTypeDetail = md.updateAppointmentTypeDetails(
						Long.parseLong(jsonObject.get("appointmentTypeId").toString()),
						jsonObject.get("appointmentTypeCode").toString(),
						jsonObject.get("appointmentTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateAppointmentTypeDetail != null && updateAppointmentTypeDetail.equalsIgnoreCase("200")) {
					json.put("updateAppointmentTypeDetail", updateAppointmentTypeDetail);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateAppointmentTypeStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {

			if (jsonObject.get("appointmentTypeCode").toString() != null
					&& !jsonObject.get("appointmentTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasAppointmentType mAppointmentType = md
						.checkMasAppointmentType(jsonObject.get("appointmentTypeCode").toString());

				if (mAppointmentType != null) {
					String masAppointmentTypeStatus = md.updateAppointmentTypeStatus(
							Long.parseLong(jsonObject.get("appointmentTypeId").toString()),
							jsonObject.get("appointmentTypeCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (masAppointmentTypeStatus != null && masAppointmentTypeStatus.equalsIgnoreCase("200")) {
						json.put("masAppointmentTypeStatus", masAppointmentTypeStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	/*****************************
	 * MAS DEPARTMENT
	 ****************************************************/
	@Override
	public String getAllDepartment(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDepartment> deptList = new ArrayList<MasDepartment>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDepartment>> mapDepart = md.getAllDepartment(jsondata);
			List totalMatches = new ArrayList();
			if (mapDepart.get("departmentList") != null) {
				deptList = mapDepart.get("departmentList");
				totalMatches = mapDepart.get("totalMatches");
				for (MasDepartment department : deptList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (department != null) {
						mapObj.put("departmentId", department.getDepartmentId());
						mapObj.put("departmentCode", department.getDepartmentCode());
						mapObj.put("departmentName", department.getDepartmentName());
						mapObj.put("status", department.getStatus());
						mapObj.put("departmentTypeId", department.getMasDepartmentType()!= null
								? department.getMasDepartmentType().getDepartmentTypeId()
								: "0");
						mapObj.put("departmentTypeName", department.getMasDepartmentType()!= null
								? department.getMasDepartmentType().getDepartmentTypeName()
								: "");

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getDepartmentTypeList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDepartmentType> depTypeList = md.getDepartmentTypeList();
		if (depTypeList != null && depTypeList.size() > 0) {
			json.put("data", depTypeList);
			json.put("count", depTypeList.size());
			json.put("status", 1);
		} else {
			json.put("data", depTypeList);
			json.put("count", depTypeList.size());
			json.put("msg", "No Record Found");
			json.put("status", 0);

		}
		return json.toString();
	}

	@Override
	public String addDepartment(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasDepartment masDepartment = new MasDepartment();
			MasDepartmentType masDepartmentType = new MasDepartmentType();

			masDepartment.setDepartmentCode(jsondata.get("departmentCode").toString().toUpperCase());
			masDepartment.setDepartmentName(jsondata.get("departmentName").toString().toUpperCase());

			masDepartmentType.setDepartmentTypeId(Long.parseLong(jsondata.get("departmentTypeId").toString()));

			masDepartment.setMasDepartmentType(masDepartmentType);
			masDepartment.setStatus("Y");
			masDepartment.setLastChgDate(date);

			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);
			masDepartment.setUser(user);

			List<MasDepartment> checkDeptList = md.validateDepartment(masDepartment.getDepartmentCode(),
					masDepartment.getDepartmentName(),masDepartment.getMasDepartmentType().getDepartmentTypeId());
			if (checkDeptList != null && checkDeptList.size() > 0) {
				if (checkDeptList.get(0).getDepartmentCode()
						.equalsIgnoreCase(jsondata.get("departmentCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Department Code is already Exists");
				}
				else {
					json.put("status", 2);
					json.put("msg", "Combination of Department Name and Deparype already Exists");
				}

			} else {
				String addDepartObj = md.addDepartment(masDepartment);
				if (addDepartObj != null && addDepartObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDepartmentDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("departmentCode").toString() != null
					&& !jsonObject.get("departmentCode").toString().trim().equalsIgnoreCase("")) {
				
				List<MasDepartment> msDepartmentList = md.validateDepartmentUpdate(Long.parseLong(jsonObject.get("departmentTypeId").toString()),
						jsonObject.get("departmentName").toString());
				if (msDepartmentList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Combination of Department Name and Department Type already exists\"}";
				}

				String updateDeptDetail = md.updateDepartmentDetails(
						Long.parseLong(jsonObject.get("departmentId").toString()),
						jsonObject.get("departmentCode").toString(), jsonObject.get("departmentName").toString(),
						Long.parseLong(jsonObject.get("departmentTypeId").toString()),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateDeptDetail != null && updateDeptDetail.equalsIgnoreCase("200")) {
					json.put("updateDeptDetail", updateDeptDetail);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDepartmentStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {

			if (jsonObject.get("departmentCode").toString() != null
					&& !jsonObject.get("departmentCode").toString().trim().equalsIgnoreCase("")) {

				MasDepartment mDepartment = md.checkDepartment(jsonObject.get("departmentCode").toString());

				if (mDepartment != null) {
					String deptStatus = md.updateDepartmentStatus(
							Long.parseLong(jsonObject.get("departmentId").toString()),
							jsonObject.get("departmentCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (deptStatus != null && deptStatus.equalsIgnoreCase("200")) {
						json.put("deptStatus", deptStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	/***************************************
	 * MAS FREQUENCY
	 ***********************************************************************/

	@Override
	public String getAllOpdFrequency(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasFrequency> freqList = new ArrayList<MasFrequency>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasFrequency>> mapFreq = md.getAllOpdFrequency(jsondata);
			List totalMatches = new ArrayList();
			if (mapFreq.get("frequencyList") != null) {
				freqList = mapFreq.get("frequencyList");
				totalMatches = mapFreq.get("totalMatches");
				for (MasFrequency frequency : freqList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (frequency != null) {
						mapObj.put("frequencyId", frequency.getFrequencyId());
						mapObj.put("frequencyCode", frequency.getFrequencyCode());
						mapObj.put("frequencyName", frequency.getFrequencyName());
						mapObj.put("frequencyHinName", frequency.getFrequencyHinName() !=null ? frequency.getFrequencyHinName() :"" );
						mapObj.put("feq", frequency.getFeq()!= null
								? frequency.getFeq(): "");
						mapObj.put("status", frequency.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
	
		return json.toString();
	}
	

	@Override
	public String addOpdFrequency(Map<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			MasFrequency masFrequency = new MasFrequency();

			masFrequency.setFrequencyCode(jsondata.get("frequencyCode").toString().toUpperCase());
			masFrequency.setFrequencyName(jsondata.get("frequencyName").toString().toUpperCase());
			masFrequency.setFrequencyHinName(jsondata.get("frequencyHinName").toString());
			masFrequency.setFeq(Double.parseDouble(jsondata.get("feq").toString()));
			masFrequency.setUser(users);
			masFrequency.setStatus("Y");
			Timestamp timestamp = new Timestamp(new Date().getTime());
			masFrequency.setLastChgDate(timestamp);

			List<MasFrequency> checkFreqtList = md.validateFrequency(masFrequency.getFrequencyCode(),
					masFrequency.getFrequencyName());
			if (checkFreqtList != null && checkFreqtList.size() > 0) {
				if (checkFreqtList.get(0).getFrequencyCode()
						.equalsIgnoreCase(jsondata.get("frequencyCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Frequency Code already Exists");
				}
				if (checkFreqtList.get(0).getFrequencyName()
						.equalsIgnoreCase(jsondata.get("frequencyName").toString())) {

					json.put("status", 2);
					json.put("msg", "Frequency Name already Exists");
				}

			} else {
				String addFreqObj = md.addOpdFrequency(masFrequency);
				if (addFreqObj != null && addFreqObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateOpdFrequencyDetails(Map<String, Object> jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String frequencyUpdate="";
		if (jsonObject.get("frequencyId") != null && !jsonObject.get("frequencyId").toString().equals("")) {
			List<MasFrequency> checkFreqtList = md.validateFrequencyUpdate(jsonObject.get("frequencyName").toString(),
					Double.parseDouble(jsonObject.get("feq").toString()));
			if (checkFreqtList.size() != 0) {
				if (checkFreqtList != null && checkFreqtList.size() > 0) {
					return "{\"status\":\"2\",\"msg\":\"Combination of Frequency Name and Feq already Exists\"}";
					}
				} else {
					frequencyUpdate = md.updateFrequencyDetails(Long.parseLong(jsonObject.get("frequencyId").toString()),
							jsonObject.get("frequencyCode").toString(), jsonObject.get("frequencyName").toString(),
							jsonObject.get("frequencyHinName").toString(),Double.parseDouble(jsonObject.get("feq").toString()),
							Long.parseLong(jsonObject.get("userId").toString()));
				if (frequencyUpdate != null && frequencyUpdate.equalsIgnoreCase("200")) {
					json.put("frequencyUpdate", frequencyUpdate);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else if (frequencyUpdate == null && frequencyUpdate.equals("")) {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);
				}
				}
		}
		 else {
			return "{\"status\":\"0\",\"msg\":\"Frequency id is not available !!!\"}";
		}

		return json.toString();

	}
	
	@Override
	public String updateOpdFrequencyStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("frequencyCode").toString() != null
					&& !jsonObject.get("frequencyCode").toString().trim().equalsIgnoreCase("")) {

				MasFrequency mFrequency = md.checkFrequency(jsonObject.get("frequencyCode").toString());

				if (mFrequency != null) {
					String freqStatus = md.updateOpdFrequencyStatus(
							Long.parseLong(jsonObject.get("frequencyId").toString()),
							jsonObject.get("frequencyCode").toString(), jsonObject.get("status").toString()
							,Long.parseLong(jsonObject.get("userId").toString()));

					if (freqStatus != null && freqStatus.equalsIgnoreCase("200")) {
						json.put("freqStatus", freqStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Record Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	@Override
	public String updateStatus(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("unitName").toString() != null
					&& !jsonObject.get("unitName").toString().trim().equalsIgnoreCase("")) {

				MasUnit mUnit = md.checkUnit(jsonObject.get("unitName").toString());

				if (mUnit != null) {
					String uStatus = md.updateUnitStatus(Long.parseLong(jsonObject.get("unitId").toString()),
							jsonObject.get("unitName").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (uStatus != null && uStatus.equalsIgnoreCase("200")) {
						json.put("uStatus", uStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	/*************************
	 * MAS EMPANELLED HOSPITAL
	 ************************************************/
	@Override
	public String getAllEmpanelledHospital(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasEmpanelledHospital> empanelledHospitalList = new ArrayList<MasEmpanelledHospital>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasEmpanelledHospital>> mapEmpanelledHosp = md.getAllEmpanelledHospital(jsondata);
			List totalMatches = new ArrayList();
			if (mapEmpanelledHosp.get("mImpanneledHospitalList") != null) {
				empanelledHospitalList = mapEmpanelledHosp.get("mImpanneledHospitalList");
				totalMatches = mapEmpanelledHosp.get("totalMatches");
				for (MasEmpanelledHospital empanelledHospital : empanelledHospitalList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (empanelledHospital != null) {
						mapObj.put("empanelledHospitalId", empanelledHospital.getEmpanelledHospitalId());
						mapObj.put("empanelledHospitalCode", empanelledHospital.getEmpanelledHospitalCode());
						mapObj.put("empanelledHospitalName", empanelledHospital.getEmpanelledHospitalName() != null
										? empanelledHospital.getEmpanelledHospitalName()
										: "");
						mapObj.put("city", empanelledHospital.getMasCity().getCityName());
						mapObj.put("cityId", empanelledHospital.getMasCity().getCityId());
						mapObj.put("empanelledHospitalAddress",
								empanelledHospital.getEmpanelledHospitalAddress() != null
										? empanelledHospital.getEmpanelledHospitalAddress()
										: "");
						mapObj.put("phoneNo",
								empanelledHospital.getPhoneNo() != null ? empanelledHospital.getPhoneNo() : "");
						mapObj.put("status", empanelledHospital.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	
	@Override
	public String addEmpanelledHospital(String impanneledHospPayload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONObject jsonObject = new JSONObject(impanneledHospPayload);

		if (jsonObject != null) {

			MasEmpanelledHospital masEmpanelledHospital = new MasEmpanelledHospital();	
			masEmpanelledHospital.setEmpanelledHospitalCode(jsonObject.get("empanelledHospitalCode").toString());
			masEmpanelledHospital.setEmpanelledHospitalName(jsonObject.get("empanelledHospitalName").toString());
			masEmpanelledHospital.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
			masEmpanelledHospital.setEmpanelledHospitalAddress(jsonObject.get("empanelledHospitalAddress").toString());
			masEmpanelledHospital.setPhoneNo(jsonObject.get("phoneNo").toString());
			masEmpanelledHospital.setStatus("y");
			long userId = Long.parseLong(jsonObject.get("userId").toString());
			masEmpanelledHospital.setLastchgBy(userId);
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masEmpanelledHospital.setLastChgDate(date);
			List<MasEmpanelledHospital> empanelledHospitalList = md.validateEmpanelledHospital(jsonObject.get("empanelledHospitalName").toString(),jsonObject.get("empanelledHospitalCode").toString());
			if (empanelledHospitalList.size() != 0) {
				if (empanelledHospitalList != null && empanelledHospitalList.size() > 0) {
					if (empanelledHospitalList.get(0).getEmpanelledHospitalCode()
							.equalsIgnoreCase(jsonObject.get("empanelledHospitalCode").toString())) {
						json.put("status", 1);
						json.put("msg", "Empanelled Hospital Code already Exists");
					}
					else if (empanelledHospitalList.get(0).getEmpanelledHospitalName()
							.equalsIgnoreCase(jsonObject.get("empanelledHospitalCode").toString())) {
						json.put("status", 1);
						json.put("msg", "Empanelled Hospital Name already Exists");
					}
				}
			} else {
				String addempaneledHospital = md.addEmpanelledHospital(masEmpanelledHospital);
				if (addempaneledHospital != null && addempaneledHospital.equalsIgnoreCase("200")) {
					json.put("msg", "Record Added Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Added");
					json.put("status", 0);
				}

			}

		}

		return json.toString();
	}

	@Override
	public String updateEmpanelledHospital(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			String updateImpanneled = md.updateEmpanelledHospital(jsonObject);		

				if (updateImpanneled != null && updateImpanneled.equalsIgnoreCase("200")) {
					json.put("updateImpanneled", updateImpanneled);

					if (jsonObject.get("status").toString().equalsIgnoreCase("update")) {
						json.put("msg", "Record Updated Successfully");
					}
					if (jsonObject.get("status").toString().equalsIgnoreCase("active")) {
						json.put("msg", "Status Updated Successfully");
					}
					if (jsonObject.get("status").toString().equalsIgnoreCase("inactive")) {
						json.put("msg", "Status Updated Successfully");
					}
					json.put("status", 1);
				}
			
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getHospitalListByRegion(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasHospital> hosptList = md.getHospitalListByRegion();
		if (hosptList != null && hosptList.size() > 0) {

			jsonObj.put("data", hosptList);
			jsonObj.put("count", hosptList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", hosptList);
			jsonObj.put("count", hosptList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	/*************************************
	 * MAS IDEAL WEIGHT
	 **********************************************************/
	@Override
	public String getAllIdealWeight(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasIdealWeight> idealWeightList = new ArrayList<MasIdealWeight>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasIdealWeight>> mapIdealWeight = md.getAllIdealWeight(jsondata);
			List totalMatches = new ArrayList();
			if (mapIdealWeight.get("idealWeightsList") != null) {
				idealWeightList = mapIdealWeight.get("idealWeightsList");
				totalMatches = mapIdealWeight.get("totalMatches");
				for (MasIdealWeight idealWeight : idealWeightList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (idealWeight != null) {
						mapObj.put("idealWeightId", idealWeight.getIdealWeightId());
						mapObj.put("genderId", idealWeight.getMasAdministrativeSex().getAdministrativeSexId());
						mapObj.put("rangeFlag", idealWeight.getMasRange2().getRangeFlag());
						if (idealWeight.getMasRange2().getRangeFlag().equalsIgnoreCase("A")) {
							mapObj.put("fromAge", idealWeight.getMasRange2().getFromRange()!= null
									? idealWeight.getMasRange2().getFromRange(): "");
							mapObj.put("toAge", idealWeight.getMasRange2().getToRange()!= null
									? idealWeight.getMasRange2().getToRange(): "");
							mapObj.put("ageRangeId", idealWeight.getMasRange2().getRangeId());
						}
						if (idealWeight.getMasRange1().getRangeFlag().equalsIgnoreCase("H")) {
							mapObj.put("fromHeight", idealWeight.getMasRange1().getFromRange()!= null
									? idealWeight.getMasRange1().getFromRange(): "");
							mapObj.put("toHeight", idealWeight.getMasRange1().getToRange()!= null
									? idealWeight.getMasRange1().getToRange(): "");
							mapObj.put("heightRangeId", idealWeight.getMasRange1().getRangeId());
						}
						mapObj.put("sd", idealWeight.getSd());
						mapObj.put("weight", idealWeight.getWeight());
						mapObj.put("status", idealWeight.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getAllPhsiotherapy(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			List<MasNursingCare> mNursingList = new ArrayList<MasNursingCare>();
			List totalMatches = new ArrayList();
			List hList = new ArrayList();

			Map<String, List<MasNursingCare>> map = md.getAllmNursingData(jsonObject);
			if (map.get("mHospitalList") != null) {
				mNursingList = map.get("mHospitalList");
				totalMatches = map.get("totalMatches");

				for (MasNursingCare nursing : mNursingList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (nursing != null) {
						mapObj.put("nursingId", nursing.getNursingId());
						mapObj.put("nursingCode", nursing.getNursingCode());
						mapObj.put("nursingName", nursing.getNursingName());
						mapObj.put("nursingType", nursing.getNursingType());
						mapObj.put("status", nursing.getStatus());
						hList.add(mapObj);
					}
				}

				if (hList != null && hList.size() > 0) {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("status", 0);
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}

	@Override
	public String addPhsiotherapy(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			MasNursingCare nursingObj = new MasNursingCare();

			nursingObj.setNursingCode(jsonObject.get("nursingCode").toString().toUpperCase());
			nursingObj.setNursingName(jsonObject.get("nursingName").toString().toUpperCase());
			nursingObj.setNursingType(jsonObject.get("nursingType").toString().toUpperCase());
			nursingObj.setDefaultstatus("y");
			nursingObj.setStatus("Y");

			long userId = Long.parseLong(jsonObject.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			nursingObj.setUser(users);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			nursingObj.setLastChgDate(date);

			List<MasNursingCare> nursinglList = md.validateMasNursing(nursingObj.getNursingCode(),
					nursingObj.getNursingName(), nursingObj.getNursingType());
			if (nursinglList.size() != 0) {
				if (nursinglList != null && nursinglList.size() > 0) {

					if (nursinglList.get(0).getNursingCode()
							.equalsIgnoreCase(jsonObject.get("nursingCode").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Nursing Code already Exists\"}";
					}

					if (nursinglList.get(0).getNursingName()
							.equalsIgnoreCase(jsonObject.get("nursingName").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Nursing Name already Exists\"}";
					}

					if (nursinglList.get(0).getNursingType()
							.equalsIgnoreCase(jsonObject.get("nursingType").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Nursing Type already Exists\"}";
					}
				}
			} else {
				String addHospital = md.addMasNursing(nursingObj);

				if (addHospital != null && addHospital.equalsIgnoreCase("200")) {
					json.put("msg", "Record Added Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Added");
					json.put("status", 0);
				}
			}

		} else {
			json.put("msg", "Data Not Found Error");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updatePhysiotherapyDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			String updateMasNursing = md.updateMasNursing(jsonObject);
					
			if (jsonObject.get("status") == "update") {
								
				List<MasNursingCare> nursinglList = md.validateMasNursingUpdate(
						jsonObject.get("nursingName").toString(), jsonObject.get("nursingType").toString());
				if (nursinglList.size() != 0) {
					if (nursinglList != null && nursinglList.size() > 0) {
						return "{\"status\":\"2\",\"msg\":\"Combination of Nursing Name and Nursing Type already Exists\"}";
					}
				}
				
			}
				
			else {

				if (updateMasNursing != null && updateMasNursing.equalsIgnoreCase("200")) {
					json.put("updateMasNursing", updateMasNursing);
					if (jsonObject.get("status").toString().equalsIgnoreCase("update")) {
					json.put("msg", "Record Updated Successfully");
					}
					if (jsonObject.get("status").toString().equalsIgnoreCase("active") || jsonObject.get("status").toString().equalsIgnoreCase("inactive"))  {
						json.put("msg", "Status Updated Successfully");
					}
					json.put("status", 1);
				}				
			}
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getAge(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasIdealWeight> idealWtList = new ArrayList<MasIdealWeight>();
		if (jsonObject != null) {

			idealWtList = md.getAge(jsonObject);

			json.put("data", idealWtList);
			json.put("count", idealWtList.size());

		} else {
			json.put("status", 0);
			json.put("count", idealWtList.size());
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}

	/***************************************
	 * SERVICE TYPE
	 ***********************************************************************/

	@Override
	public String getAllServiceType(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasServiceType> stList = new ArrayList<MasServiceType>();

		Map<String, List<MasServiceType>> stMap = md.getAllServiceType(jsonObj);
		List stListObj = new ArrayList();
		List totalMatches = new ArrayList();
		if (stMap.get("masServiceTypeList") != null) {
			stList = stMap.get("masServiceTypeList");
			totalMatches = stMap.get("totalMatches");

			for (MasServiceType serviceType : stList) {
				HashMap<String, Object> mapObj = new HashMap<String, Object>();

				mapObj.put("serviceTypeId", serviceType.getServiceTypeId());
				mapObj.put("serviceTypeCode", serviceType.getServiceTypeCode());
				mapObj.put("serviceTypeName", serviceType.getServiceTypeName());
				mapObj.put("status", serviceType.getStatus());
				stListObj.add(mapObj);
			}

			if (stListObj != null && stListObj.size() > 0) {
				json.put("data", stListObj);

				json.put("count", totalMatches.size());
				json.put("status", 1);
			} else {
				json.put("data", stListObj);
				json.put("count", totalMatches.size());
				json.put("msg", "Data not found");
				json.put("status", 0);
			}
		}

		return json.toString();
	}

	@SuppressWarnings("null")
	@Override
	public String updateServiceType(HashMap<String, Object> serviceType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (serviceType.get("serviceTypeName") != null
				&& !serviceType.get("serviceTypeName").toString().equalsIgnoreCase("")) {

			String serviceTypeUpdate = md.updateServiceType(Long.parseLong(serviceType.get("serviceTypeId").toString()),
					serviceType.get("serviceTypeName").toString()
					,Long.parseLong(serviceType.get("userId").toString()));

			if (serviceTypeUpdate != null && serviceTypeUpdate.equalsIgnoreCase("200")) {
				json.put("serviceTypeUpdate", serviceTypeUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (serviceTypeUpdate == null &&  serviceTypeUpdate.equals("")) {
				json.put("msg", "Record Not Updated!!!");
				json.put("status", 0);
			}
			 else if (serviceTypeUpdate != null && serviceTypeUpdate.equalsIgnoreCase("serviceTypeNameExist")) {
				json.put("msg", "Service Type Name Already Exists");
				json.put("status", 2);
			}
			 else {
				 json.put("msg", "Service type id is not available");
					json.put("status", 0);
			 }
		}
	
		
		return json.toString();
	}

	@Override
	public String addServiceType(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		if (json != null) {

			MasServiceType masServiceType = new MasServiceType();
			masServiceType.setServiceTypeCode(json.getString("serviceTypeCode").toUpperCase());
			masServiceType.setServiceTypeName(json.getString("serviceTypeName").toUpperCase());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			masServiceType.setLastChgDate(date);

			long userId = Long.parseLong(json.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			masServiceType.setUser(users);
			masServiceType.setStatus("Y");

			String masStTypeObj = md.addServiceType(masServiceType);

			if (masStTypeObj != null && masStTypeObj.equalsIgnoreCase("200")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Record Added Successfully");

			} else if (masStTypeObj != null && masStTypeObj.equalsIgnoreCase("500")) {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Record Not Added");

			} else if (masStTypeObj != null && masStTypeObj.equalsIgnoreCase("serviceTypeCodeExist")) {
				jsonObj.put("status", 2);
				jsonObj.put("msg", "Service Type Code Already Exists");

			} else if (masStTypeObj != null && masStTypeObj.equalsIgnoreCase("serviceTypeNameExist")) {
				jsonObj.put("status", 2);
				jsonObj.put("msg", "Service Type Name Already Exists");

			} else {
				jsonObj.put("msg", masStTypeObj);
				jsonObj.put("status", 0);
			}
		}
		return jsonObj.toString();
	}

	@Override
	public String statusServiceType(HashMap<String, Object> serviceType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (serviceType.get("serviceTypeCode") != null
				&& !serviceType.get("serviceTypeCode").toString().equalsIgnoreCase("")) {
			String masStStatus = md.updateServiceTypeStatus(Long.parseLong(serviceType.get("serviceTypeId").toString()),
					serviceType.get("serviceTypeCode").toString(), serviceType.get("status").toString());

			if (masStStatus != null && masStStatus.equalsIgnoreCase("200")) {
				json.put("masStStatus", masStStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {
				// json.put("masCmdStatus", masCmdStatus);
				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Service Type Code is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateIdealWeight(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			
			String updateidealWt = md.updateIdealWeight(jsonObject);

				if (updateidealWt != null && updateidealWt.equalsIgnoreCase("recordUpdated")) {
					json.put("updateidealWt", updateidealWt);
					json.put("msg", "Record Updated Successfully.");
					json.put("status", 1);
				} 
				else if (updateidealWt != null && updateidealWt.equalsIgnoreCase("statusUpdated")) {
					json.put("updateidealWt", updateidealWt);
					json.put("msg", "Status Updated Successfully.");
					json.put("status", 1);
				}
				else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			

		
		return json.toString();
	}

	@Override
	public String addIdealWeight(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			if (jsonObject != null) {
				MasIdealWeight masIdealWeight = new MasIdealWeight();
				MasAdministrativeSex masAdministrativeSex = new MasAdministrativeSex();
				masAdministrativeSex
						.setAdministrativeSexId(Long.parseLong(jsonObject.get("administrativeSexId").toString()));
				masIdealWeight.setMasAdministrativeSex(masAdministrativeSex);
				MasRange masRange2 = new MasRange();
				MasRange masRange1 = new MasRange();
				masRange2.setRangeId(Long.parseLong(jsonObject.get("masRange2").toString()));
				masRange1.setRangeId(Long.parseLong(jsonObject.get("masRange1").toString()));
				masIdealWeight.setMasRange1(masRange1);
				masIdealWeight.setMasRange2(masRange2);
				masIdealWeight.setWeight(Double.parseDouble(jsonObject.get("weight").toString()));
				masIdealWeight.setStatus("Y");
				Users users=new Users();
				users.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
				masIdealWeight.setUser(users);
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masIdealWeight.setLastChgDate(date);			
				
				List<MasIdealWeight> validIdealWeight = md.validateIdealWeight(Long.parseLong(jsonObject.get("administrativeSexId").toString()),
						Long.parseLong(jsonObject.get("masRange1").toString()), Long.parseLong(jsonObject.get("masRange2").toString()));
				if (validIdealWeight != null && validIdealWeight.size() > 0) {					

						json.put("status", 2);
						json.put("msg", "Ideal Weight already exists against the selected Gender, Height and Weight");
					
				}
				else {
					
					String savedObject = md.addIdealWeight(masIdealWeight);
					if (savedObject != null && savedObject.equalsIgnoreCase("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}
				

				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/*-------------------MAS RANK----------------------*/
	@Override
	public String addRank(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasRank masRank = new MasRank();

		if (!json.equals(null)) {

			if (json.get("rankCode") == null) {
				return "{\"status\":\"0\",\"msg\":\"rankCode is not contain in json data or it will be null or blank please check\"}";
			}
			if (json.get("rankName") == null) {
				return "{\"status\":\"0\",\"msg\":\"rankName is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				masRank.setRankCode(json.get("rankCode").toString().toUpperCase());
				masRank.setRankName(json.get("rankName").toString().toUpperCase());

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				//masRank.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				
				long userId = Long.parseLong(json.get("userId").toString());

				Users users = new Users();
				users.setUserId(userId);
				

				MasEmployeeCategory employeeCategory = new MasEmployeeCategory();
				employeeCategory.setEmployeeCategoryId(Long.parseLong(json.get("employeeCategoryId").toString()));
				masRank.setMasEmployeeCategory(employeeCategory);

				List<MasRank> masRank1 = md.validateMasRank(masRank.getRankCode().toString(),
						masRank.getRankName().toString());
				if (masRank1.size() != 0) {
					if (masRank1 != null && masRank1.size() > 0) {
						if (masRank1.get(0).getRankCode().equalsIgnoreCase(json.get("rankCode").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Rank Code already Exists\"}";
						}
						if (masRank1.get(0).getRankName().equalsIgnoreCase(json.get("rankName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Rank Name already Exists\"}";
						}
					}
				} else {
					String masRankObj = md.addMasRank(masRank);
					if (masRankObj != null && masRankObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masRankObj != null && masRankObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masRankObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllRank(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasRank> rankList = new ArrayList<MasRank>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasRank>> mapRank = md.getAllRank(jsonObj);
			List totalMatches = new ArrayList();
			if (mapRank.get("masRankList") != null) {
				rankList = mapRank.get("masRankList");
				totalMatches = mapRank.get("totalMatches");
				for (MasRank rank : rankList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("rankId", rank.getRankId());
					mapObj.put("rankName", rank.getRankName());
					mapObj.put("rankCode", rank.getRankCode());
					//mapObj.put("status", rank.getStatus());
					mapObj.put("employeeCategoryName",
							rank.getMasEmployeeCategory() != null
									? rank.getMasEmployeeCategory().getEmployeeCategoryName()
									: "");
					mapObj.put("employeeCategoryId",
							rank.getMasEmployeeCategory() != null
									? rank.getMasEmployeeCategory().getEmployeeCategoryId()
									: "0");
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getRank(HashMap<String, Object> rank, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (rank.get("rankName") == null && rank.get("rankName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"rankCode is not available !!!\"}";
		} else {
			MasRank chkRank = md.chkRank(rank.get("rankName").toString());
			if (chkRank != null) {
				List<MasRank> masRankLst = md.getRank(rank.get("rankName").toString());
				if (masRankLst != null && masRankLst.size() > 0) {
					jsonObject.put("masRankLst", masRankLst);
					jsonObject.put("msg", "List of rank successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateRank(HashMap<String, Object> rank, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();

		if (rank.get("rankId") != null && !rank.get("rankId").toString().equalsIgnoreCase("")) {
			List<MasRank> msRankList = md.validateMasRankUpdate(
					Long.parseLong(rank.get("employeeCategoryId").toString()), rank.get("rankName").toString());
			if (msRankList.size() > 0) {
				return "{\"status\":\"0\",\"msg\":\"Rank Code already exists\"}";
			}
			String rankUpdate = md.updateRank(Long.parseLong(rank.get("rankId").toString()),
					rank.get("rankCode").toString(), rank.get("rankName").toString(),
					Long.parseLong(rank.get("employeeCategoryId").toString()),
					Long.parseLong(rank.get("userId").toString()));
			if (rankUpdate != null && !rankUpdate.equalsIgnoreCase("")) {
				json.put("rankUpdate", rankUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (rankUpdate == null && rankUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"Rank Code is not available !!!\"}";
			}

		} else {
			return "{\"status\":\"0\",\"msg\":\"Rank Code is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String statusRank(HashMap<String, Object> rank, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (rank.get("rankCode") != null && !rank.get("rankCode").toString().equalsIgnoreCase("")) {
			MasRank chkRank = md.chkRank(rank.get("rankCode").toString());
			if (chkRank != null) {
				String masRankStatus = md.updateRankStatus(Long.parseLong(rank.get("rankId").toString()),
						rank.get("rankCode").toString(), rank.get("status").toString(),
						Long.parseLong(rank.get("userId").toString()));
				if (masRankStatus != null && masRankStatus.equalsIgnoreCase("200")) {
					json.put("masRankStatus", masRankStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Rank Code is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getEmployeeCategoryList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasEmployeeCategory> employeeCategoryList = md.getEmployeeCategoryList();
		if (employeeCategoryList != null && employeeCategoryList.size() > 0) {

			jsonObj.put("data", employeeCategoryList);
			jsonObj.put("count", employeeCategoryList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", employeeCategoryList);
			jsonObj.put("count", employeeCategoryList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	/********************************************************
	 * TRADE MASTER
	 *********************************************************/

	@Override
	public String addTrade(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasTrade masTrade = new MasTrade();
		if (!json.equals(null)) {

			if (json.get("tradeName") == null) {
				return "{\"status\":\"0\",\"msg\":\"tradeName is not contain in json data or it will be null or blank please check\"}";
			} else {
				masTrade.setTradeName(json.get("tradeName").toString().toUpperCase());

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				//masTrade.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());

				Users users = new Users();
				users.setUserId(userId);
				
				MasServiceType masServiceType = new MasServiceType();
				masServiceType.setServiceTypeId(Long.parseLong(json.get("serviceTypeId").toString()));
				

				List<MasTrade> masTrade1 = md.validateMasTrade(masTrade.getTradeName().toString());
				if (masTrade1.size() != 0) {
					if (masTrade1 != null && masTrade1.size() > 0) {
						if (masTrade1.get(0).getTradeName().equalsIgnoreCase(json.get("tradeName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Trade Name is already Existing.\"}";
						}
					}
				} else {
					String masTradeObj = md.addMasTrade(masTrade);
					if (masTradeObj != null && masTradeObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masTradeObj != null && masTradeObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masTradeObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllTrade(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasTrade> tradeList = new ArrayList<MasTrade>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasTrade>> mapTrade = md.getAllTrade(jsonObj);
			List totalMatches = new ArrayList();
			if (mapTrade.get("masTradeList") != null) {
				tradeList = mapTrade.get("masTradeList");
				totalMatches = mapTrade.get("totalMatches");
				for (MasTrade trade : tradeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("tradeId", trade.getTradeId());
					mapObj.put("tradeName", trade.getTradeName());
										
					list.add(mapObj);

				}
				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getTrade(HashMap<String, Object> trade, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (trade.get("tradeName") == null && trade.get("tradeName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"Trade Name is not available !!!\"}";
		} else {
			MasTrade checkTrade = md.checkTrade(trade.get("tradeName").toString());
			if (checkTrade != null) {
				List<MasTrade> masTradeLst = md.getTrade(trade.get("tradeName").toString());
				if (masTradeLst != null && masTradeLst.size() > 0) {
					jsonObject.put("masTradeLst", masTradeLst);
					jsonObject.put("msg", "List of Trade successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateTrade(HashMap<String, Object> trade, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();

		if (trade.get("tradeId") != null && !trade.get("tradeId").toString().equalsIgnoreCase("")) {			

			String tradeUpdate = md.updateTrade(Long.parseLong(trade.get("tradeId").toString()),
					trade.get("tradeName").toString(), Long.parseLong(trade.get("serviceTypeId").toString()),
					Long.parseLong(trade.get("userId").toString()));
			if (tradeUpdate != null && !tradeUpdate.equalsIgnoreCase("")) {
				json.put("tradeUpdate", tradeUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (tradeUpdate == null && tradeUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"trade Name is not available !!!\"}";
			}
		}

		return json.toString();

	}

	@Override
	public String statusTrade(HashMap<String, Object> trade, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (trade.get("tradeName") != null && !trade.get("tradeName").toString().equalsIgnoreCase("")) {
			MasTrade checkTrade = md.checkTrade(trade.get("tradeName").toString());
			if (checkTrade != null) {
				String masTradeStatus = md.updateTradeStatus(Long.parseLong(trade.get("tradeId").toString()),
						trade.get("tradeName").toString(), trade.get("status").toString(),
						Long.parseLong(trade.get("userId").toString()));
				if (masTradeStatus != null && masTradeStatus.equalsIgnoreCase("200")) {
					json.put("masTradeStatus", masTradeStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"trade Name is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getServiceTypeList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasServiceType> serviceTypeList = md.getServiceTypeList();
		if (serviceTypeList != null && serviceTypeList.size() > 0) {

			jsonObj.put("data", serviceTypeList);
			jsonObj.put("count", serviceTypeList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", serviceTypeList);
			jsonObj.put("count", serviceTypeList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	/***************************************
	 * MAS RELIGION
	 ***********************************************************************/

	@Override
	public String getAllReligion(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasReligion> reliList = new ArrayList<MasReligion>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasReligion>> mapReli = md.getAllReligion(jsondata);
			List totalMatches = new ArrayList();
			if (mapReli.get("religionList") != null) {
				reliList = mapReli.get("religionList");
				totalMatches = mapReli.get("totalMatches");
				for (MasReligion religion : reliList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (religion != null) {
						mapObj.put("religionId", religion.getReligionId());
						mapObj.put("religionCode", religion.getReligionCode());
						mapObj.put("religionName", religion.getReligionName());
						mapObj.put("status", religion.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addReligion(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasReligion masReligion = new MasReligion();

			masReligion.setReligionCode(jsondata.get("religionCode").toString().toUpperCase());
			masReligion.setReligionName(jsondata.get("religionName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			masReligion.setUser(users);
			masReligion.setStatus("Y");
			masReligion.setLastChgDate(date);

			List<MasReligion> checkReliList = md.validateReligion(masReligion.getReligionCode(),
					masReligion.getReligionName());
			if (checkReliList != null && checkReliList.size() > 0) {
				if (checkReliList.get(0).getReligionCode().equalsIgnoreCase(jsondata.get("religionCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Religion Code already Exists");
				}
				if (checkReliList.get(0).getReligionName().equalsIgnoreCase(jsondata.get("religionName").toString())) {

					json.put("status", 2);
					json.put("msg", "Religion Name already Exists");
				}

			} else {
				String addReliObj = md.addReligion(masReligion);
				if (addReliObj != null && addReliObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateReligionDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("religionCode").toString() != null
					&& !jsonObject.get("religionCode").toString().trim().equalsIgnoreCase("")) {

				List<MasReligion> msReligionList = md.validateReligionUpdate(jsonObject.get("religionCode").toString(),
						jsonObject.get("religionName").toString());
				if (msReligionList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Religion Name already exists\"}";
				}

				String updatereligion = md.updateReligionDetails(
						Long.parseLong(jsonObject.get("religionId").toString()),
						jsonObject.get("religionCode").toString(), jsonObject.get("religionName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updatereligion != null && updatereligion.equalsIgnoreCase("200")) {
					json.put("updatefreq", updatereligion);
					json.put("msg", "Successfully Updated.");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateReligionStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("religionCode").toString() != null
					&& !jsonObject.get("religionCode").toString().trim().equalsIgnoreCase("")) {

				MasReligion mReligion = md.checkReligion(jsonObject.get("religionCode").toString());

				if (mReligion != null) {
					String reliStatus = md.updateReligionStatus(Long.parseLong(jsonObject.get("religionId").toString()),
							jsonObject.get("religionCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (reliStatus != null && reliStatus.equalsIgnoreCase("200")) {
						json.put("reliStatus", reliStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}

	/***************************************
	 * MAS Identification Master
	 ***********************************************************************/

	@Override
	public String getAllIdentificationType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasIdentificationType> identificationList = new ArrayList<MasIdentificationType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasIdentificationType>> mapIdentification = md.getAllIdentification(jsondata);
			List totalMatches = new ArrayList();
			if (mapIdentification.get("identificationList") != null) {
				identificationList = mapIdentification.get("identificationList");
				totalMatches = mapIdentification.get("totalMatches");
				for (MasIdentificationType identificationType : identificationList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (identificationType != null) {
						mapObj.put("identificationTypeId", identificationType.getIdentificationTypeId());
						mapObj.put("identificationCode", identificationType.getIdentificationCode()
								!=null ? identificationType.getIdentificationCode() : "");
						mapObj.put("identificationName", identificationType.getIdentificationName()
								!=null ? identificationType.getIdentificationName() : "");
						mapObj.put("status", identificationType.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addIdentificationType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasIdentificationType masIdentification = new MasIdentificationType();

			masIdentification.setIdentificationCode(jsondata.get("identificationCode").toString().toUpperCase());
			masIdentification.setIdentificationName(jsondata.get("identificationName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());

			 masIdentification.setLastChgBy(userId);
			 
			masIdentification.setStatus("Y");
			masIdentification.setLastChgDate(date);

			List<MasIdentificationType> checkIdentificationList = md.validateIdentification(
					masIdentification.getIdentificationCode(), masIdentification.getIdentificationName());
			if (checkIdentificationList != null && checkIdentificationList.size() > 0) {
				if (checkIdentificationList.get(0).getIdentificationCode()
						.equalsIgnoreCase(jsondata.get("identificationCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Identification Type Code already Exists");
				}
				if (checkIdentificationList.get(0).getIdentificationName()
						.equalsIgnoreCase(jsondata.get("iIdentificationName").toString())) {

					json.put("status", 2);
					json.put("msg", "Identification Type Name already Exists");
				}

			} else {
				String addIdentificationObj = md.addIdentification(masIdentification);
				if (addIdentificationObj != null && addIdentificationObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateIdentificationType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("identificationCode").toString() != null
					&& !jsonObject.get("identificationCode").toString().trim().equalsIgnoreCase("")) {

				List<MasIdentificationType> msIdentificationList = md
						.validateIdentificationUpdate(jsonObject.get("identificationName").toString());
				if (msIdentificationList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Identification Type Name already exists\"}";
				}

				String updateIdentification = md.updateIdentification(
						Long.parseLong(jsonObject.get("identificationTypeId").toString()),
						jsonObject.get("identificationCode").toString(), jsonObject.get("identificationName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateIdentification != null && updateIdentification.equalsIgnoreCase("200")) {
					json.put("updateIdentification", updateIdentification);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateIdentificationTypeStatus(JSONObject Identification, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (Identification.get("identificationCode").toString() != null
					&& !Identification.get("identificationCode").toString().trim().equalsIgnoreCase("")) {

				MasIdentificationType mIdentification = md
						.checkIdentification(Identification.get("identificationCode").toString());

				if (mIdentification != null) {
					String identificationType = md.updateIdentificationStatus(
							Long.parseLong(Identification.get("identificationTypeId").toString()),
							Identification.get("identificationCode").toString(), Identification.get("status").toString(),
							Long.parseLong(Identification.get("userId").toString()));

					if (identificationType != null && identificationType.equalsIgnoreCase("200")) {
						jsonObject.put("identificationType", identificationType);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS EMPLOYEE CATEGORY
	 ***********************************************************************/

	@Override
	public String getAllEmployeeCategory(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasEmployeeCategory> employeeCategoryList = new ArrayList<MasEmployeeCategory>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasEmployeeCategory>> mapEmployeeCategory = md.getAllEmployeeCategory(jsondata);
			List totalMatches = new ArrayList();
			if (mapEmployeeCategory.get("employeeCategoryList") != null) {
				employeeCategoryList = mapEmployeeCategory.get("employeeCategoryList");
				totalMatches = mapEmployeeCategory.get("totalMatches");
				for (MasEmployeeCategory employeeCategory : employeeCategoryList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (employeeCategory != null) {
						mapObj.put("employeeCategoryId", employeeCategory.getEmployeeCategoryId());
						mapObj.put("employeeCategoryCode", employeeCategory.getEmployeeCategoryCode());
						mapObj.put("employeeCategoryName", employeeCategory.getEmployeeCategoryName());
						//mapObj.put("status", employeeCategory.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addEmployeeCategory(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasEmployeeCategory masEmployeeCategory = new MasEmployeeCategory();

			masEmployeeCategory
					.setEmployeeCategoryCode((jsondata.get("employeeCategoryCode")).toString().toUpperCase());
			masEmployeeCategory.setEmployeeCategoryName(jsondata.get("employeeCategoryName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users users = new Users();
			users.setUserId(userId);		

			List<MasEmployeeCategory> checkEmployeeCategoryList = md.validateEmployeeCategory(
					masEmployeeCategory.getEmployeeCategoryCode(), masEmployeeCategory.getEmployeeCategoryName());
			if (checkEmployeeCategoryList != null && checkEmployeeCategoryList.size() > 0) {
				if (checkEmployeeCategoryList.get(0).getEmployeeCategoryCode().toString()
						.equalsIgnoreCase(jsondata.get("employeeCategoryCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Employee Category Code is already Existing");
				}
				if (checkEmployeeCategoryList.get(0).getEmployeeCategoryName()
						.equalsIgnoreCase(jsondata.get("employeeCategoryName").toString())) {

					json.put("status", 2);
					json.put("msg", "Employee Category Name already Exists");
				}

			} else {
				String addEmployeeCategoryObj = md.addEmployeeCategory(masEmployeeCategory);
				if (addEmployeeCategoryObj != null && addEmployeeCategoryObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateEmployeeCategoryDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("employeeCategoryCode").toString() != null
					&& !jsonObject.get("employeeCategoryCode").toString().trim().equalsIgnoreCase("")) {

				List<MasEmployeeCategory> msEmployeeCategoryList = md.validateEmployeeCategoryUpdate(
						jsonObject.get("employeeCategoryCode").toString(),
						jsonObject.get("employeeCategoryName").toString());
				if (msEmployeeCategoryList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Employee Category Name already exists\"}";
				}

				String updateEmployeeCategory = md.updateEmployeeCategoryDetails(
						Long.parseLong(jsonObject.get("employeeCategoryId").toString()),
						jsonObject.get("employeeCategoryCode").toString(),
						jsonObject.get("employeeCategoryName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateEmployeeCategory != null && updateEmployeeCategory.equalsIgnoreCase("200")) {
					json.put("updateEmployeeCategory", updateEmployeeCategory);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);
			}
		}
		return json.toString();
	}

	@Override
	public String updateEmployeeCategoryStatus(JSONObject EmployeeCategory, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (EmployeeCategory.get("employeeCategoryCode").toString() != null
					&& !EmployeeCategory.get("employeeCategoryCode").toString().trim().equalsIgnoreCase("")) {

				MasEmployeeCategory mEmployeeCategory = md
						.checkEmployeeCategory(EmployeeCategory.get("employeeCategoryCode").toString());

				if (mEmployeeCategory != null) {
					String employeeCategoryStatus = md.updateEmployeeCategoryStatus(
							Long.parseLong(EmployeeCategory.get("employeeCategoryId").toString()),
							EmployeeCategory.get("employeeCategoryCode").toString(),
							EmployeeCategory.get("status").toString(),
							Long.parseLong(EmployeeCategory.get("userId").toString()));

					if (employeeCategoryStatus != null && employeeCategoryStatus.equalsIgnoreCase("200")) {
						jsonObject.put("employeeCategoryStatus", employeeCategoryStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS Administrative Sex
	 ***********************************************************************/

	@Override
	public String getAllAdministrativeSex(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasAdministrativeSex> administrativeSexList = new ArrayList<MasAdministrativeSex>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasAdministrativeSex>> mapAdministrativeSex = md.getAllAdministrativeSex(jsondata);
			List totalMatches = new ArrayList();
			if (mapAdministrativeSex.get("administrativeSexList") != null) {
				administrativeSexList = mapAdministrativeSex.get("administrativeSexList");
				totalMatches = mapAdministrativeSex.get("totalMatches");
				for (MasAdministrativeSex administrativeSex : administrativeSexList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (administrativeSex != null) {
						mapObj.put("administrativeSexId", administrativeSex.getAdministrativeSexId());
						mapObj.put("administrativeSexCode", administrativeSex.getAdministrativeSexCode());
						mapObj.put("administrativeSexName", administrativeSex.getAdministrativeSexName());
						mapObj.put("status", administrativeSex.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addAdministrativeSex(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasAdministrativeSex masAdministrativeSex = new MasAdministrativeSex();

			masAdministrativeSex
					.setAdministrativeSexCode(jsondata.get("administrativeSexCode").toString().toUpperCase());
			masAdministrativeSex
					.setAdministrativeSexName(jsondata.get("administrativeSexName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masAdministrativeSex.setUser(user);
			masAdministrativeSex.setStatus("Y");
			masAdministrativeSex.setLastChgDate(date);

			List<MasAdministrativeSex> checkAdministrativeSexList = md.validateAdministrativeSex(
					masAdministrativeSex.getAdministrativeSexCode(), masAdministrativeSex.getAdministrativeSexName());
			if (checkAdministrativeSexList != null && checkAdministrativeSexList.size() > 0) {
				if (checkAdministrativeSexList.get(0).getAdministrativeSexCode()
						.equalsIgnoreCase(jsondata.get("administrativeSexCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Gender Code already Exists");
				}
				if (checkAdministrativeSexList.get(0).getAdministrativeSexName()
						.equalsIgnoreCase(jsondata.get("administrativeSexName").toString())) {

					json.put("status", 2);
					json.put("msg", "Gender Name already Exists");
				}

			} else {
				String addAdministrativeSexObj = md.addAdministrativeSex(masAdministrativeSex);
				if (addAdministrativeSexObj != null && addAdministrativeSexObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateAdministrativeSexDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("administrativeSexCode").toString() != null
					&& !jsonObject.get("administrativeSexCode").toString().trim().equalsIgnoreCase("")) {

				List<MasAdministrativeSex> msAdministrativeSexList = md.validateAdministrativeSexUpdate(
						jsonObject.get("administrativeSexCode").toString(),
						jsonObject.get("administrativeSexName").toString());
				if (msAdministrativeSexList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Gender Name already exists\"}";
				}

				String updateAdministrativeSex = md.updateAdministrativeSexDetails(
						Long.parseLong(jsonObject.get("administrativeSexId").toString()),
						jsonObject.get("administrativeSexCode").toString(),
						jsonObject.get("administrativeSexName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateAdministrativeSex != null && updateAdministrativeSex.equalsIgnoreCase("200")) {
					json.put("updateAdministrativeSex", updateAdministrativeSex);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateAdministrativeSexStatus(JSONObject AdministrativeSex, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (AdministrativeSex.get("administrativeSexCode").toString() != null
					&& !AdministrativeSex.get("administrativeSexCode").toString().trim().equalsIgnoreCase("")) {

				MasAdministrativeSex mAdministrativeSex = md
						.checkAdministrativeSex(AdministrativeSex.get("administrativeSexCode").toString());

				if (mAdministrativeSex != null) {
					String administrativeSexStatus = md.updateAdministrativeSexStatus(
							Long.parseLong(AdministrativeSex.get("administrativeSexId").toString()),
							AdministrativeSex.get("administrativeSexCode").toString(),
							AdministrativeSex.get("status").toString(),
							Long.parseLong(AdministrativeSex.get("userId").toString()));

					if (administrativeSexStatus != null && administrativeSexStatus.equalsIgnoreCase("200")) {
						jsonObject.put("administrativeSexStatus", administrativeSexStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS Medical Category
	 ***********************************************************************/

	@Override
	public String getAllMedicalCategory(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMedicalCategory> medicalCategoryList = new ArrayList<MasMedicalCategory>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasMedicalCategory>> mapMedicalCategory = md.getAllMedicalCategory(jsondata);
			List totalMatches = new ArrayList();
			if (mapMedicalCategory.get("medicalCategoryList") != null) {
				medicalCategoryList = mapMedicalCategory.get("medicalCategoryList");
				totalMatches = mapMedicalCategory.get("totalMatches");
				for (MasMedicalCategory medicalCategory : medicalCategoryList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (medicalCategory != null) {
						mapObj.put("medicalCategoryId", medicalCategory.getMedicalCategoryId());
						mapObj.put("medicalCategoryCode", medicalCategory.getMedicalCategoryCode());
						mapObj.put("medicalCategoryName", medicalCategory.getMedicalCategoryName());
						mapObj.put("fitFlag", medicalCategory.getFitFlag() !=null ? medicalCategory.getFitFlag() : "");
						mapObj.put("status", medicalCategory.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addMedicalCategory(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);

			MasMedicalCategory masMedicalCategory = new MasMedicalCategory();
			masMedicalCategory.setMedicalCategoryCode(Long.parseLong(jsondata.get("medicalCategoryCode").toString()));
			masMedicalCategory.setMedicalCategoryName(jsondata.get("medicalCategoryName").toString().toUpperCase());
			masMedicalCategory.setFitFlag(jsondata.get("fitFlag").toString());
			masMedicalCategory.setUser(user);
			masMedicalCategory.setStatus("Y");
			masMedicalCategory.setLastChgDate(date);
			List<MasMedicalCategory> fitFlagList=null;
			List<MasMedicalCategory> checkMedicalCategoryList = md.validateMedicalCategory(
					masMedicalCategory.getMedicalCategoryCode(), masMedicalCategory.getMedicalCategoryName());
			if(jsondata.get("fitFlag").toString().equals("F")) {
			   fitFlagList=md.validateFitFlag();
			
			if(fitFlagList !=null && fitFlagList.size()>0) {
				json.put("status", 2);
				json.put("msg", "Fit Medical Category "+fitFlagList.get(0).getMedicalCategoryName()+" already exists");
			 }	
			else  {
				if (checkMedicalCategoryList != null && checkMedicalCategoryList.size() > 0) {
					if ((checkMedicalCategoryList.get(0).getMedicalCategoryCode() + "")
							.equalsIgnoreCase((String) jsondata.get("medicalCategoryCode"))) {

						json.put("status", 2);
						json.put("msg", "Medical Category Code already Exists");
					}
					else if (checkMedicalCategoryList.get(0).getMedicalCategoryName()
							.equalsIgnoreCase(jsondata.get("medicalCategoryName").toString())) {

						json.put("status", 2);
						json.put("msg", "Medical Category Name already Exists");
					}
				}
				 else {
					String addmedicalCategoryObj = md.addMedicalCategory(masMedicalCategory);
					if (addmedicalCategoryObj != null && addmedicalCategoryObj.equalsIgnoreCase("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}

			}
			}
			
			else  {
				if (checkMedicalCategoryList != null && checkMedicalCategoryList.size() > 0) {
					if ((checkMedicalCategoryList.get(0).getMedicalCategoryCode() + "")
							.equalsIgnoreCase((String) jsondata.get("medicalCategoryCode"))) {

						json.put("status", 2);
						json.put("msg", "Medical Category Code already Exists");
					}
					else if (checkMedicalCategoryList.get(0).getMedicalCategoryName()
							.equalsIgnoreCase(jsondata.get("medicalCategoryName").toString())) {

						json.put("status", 2);
						json.put("msg", "Medical Category Name already Exists");
					}
				}
				 else {
					String addmedicalCategoryObj = md.addMedicalCategory(masMedicalCategory);
					if (addmedicalCategoryObj != null && addmedicalCategoryObj.equalsIgnoreCase("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}

			}
		}
						
		
		return json.toString();
	}

	@Override
	public String updateMedicalCategoryDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			
			List<MasMedicalCategory> fitFlagList=null;
			if(jsonObject.get("fitFlag").toString().equals("F")) {
				fitFlagList=md.validateFitFlag();
				
				if(fitFlagList !=null && fitFlagList.size()>0) {
					json.put("status", 2);					
					json.put("msg", "Fit Medical Category "+fitFlagList.get(0).getMedicalCategoryName()+" already exists");
				 }
				else {
					if (jsonObject.get("medicalCategoryCode").toString() != null
							&& !jsonObject.get("medicalCategoryCode").toString().trim().equalsIgnoreCase("")) {				
						String updateMedicalCategory = md.updateMedicalCategoryDetails(
								Long.parseLong(jsonObject.get("medicalCategoryId").toString()),
								(Long.parseLong(jsonObject.get("medicalCategoryCode").toString())),
								jsonObject.get("medicalCategoryName").toString()
								,Long.parseLong(jsonObject.get("userId").toString()),jsonObject.get("fitFlag").toString());
						 if (updateMedicalCategory != null && updateMedicalCategory.equalsIgnoreCase("200")) {
							json.put("updateMedicalCategory", updateMedicalCategory);
							json.put("msg", "Record Updated Successfully");
							json.put("status", 1);
						} else {
							json.put("msg", "Record Not Updated.");
							json.put("status", 0);

						}
					}
				}
			}
			
			else if (jsonObject.get("medicalCategoryCode").toString() != null
					&& !jsonObject.get("medicalCategoryCode").toString().trim().equalsIgnoreCase("")) {				
				String updateMedicalCategory = md.updateMedicalCategoryDetails(
						Long.parseLong(jsonObject.get("medicalCategoryId").toString()),
						(Long.parseLong(jsonObject.get("medicalCategoryCode").toString())),
						jsonObject.get("medicalCategoryName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()),jsonObject.get("fitFlag").toString());
				 if (updateMedicalCategory != null && updateMedicalCategory.equalsIgnoreCase("200")) {
					json.put("updateMedicalCategory", updateMedicalCategory);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}
		
		
		return json.toString();
	}

	@Override
	public String updateMedicalCategoryStatus(JSONObject MedicalCategory, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (MedicalCategory.get("medicalCategoryCode").toString() != null
					&& !MedicalCategory.get("medicalCategoryCode").toString().trim().equalsIgnoreCase("")) {

				MasMedicalCategory mMedicalCategory = md
						.checkMedicalCategory(Long.parseLong((MedicalCategory.get("medicalCategoryCode").toString())));

				if (mMedicalCategory != null) {
					String medicalCategoryStatus = md.updateMedicalCategoryStatus(
							Long.parseLong(MedicalCategory.get("medicalCategoryId").toString()),
							(Long.parseLong(MedicalCategory.get("medicalCategoryCode").toString())),
							MedicalCategory.get("status").toString()
							,Long.parseLong(MedicalCategory.get("userId").toString()));

					if (medicalCategoryStatus != null && medicalCategoryStatus.equalsIgnoreCase("200")) {
						jsonObject.put("medicalCategoryStatus", medicalCategoryStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS Blood Group
	 ***********************************************************************/

	@Override
	public String getAllBloodGroup(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasBloodGroup> bloodGroupList = new ArrayList<MasBloodGroup>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasBloodGroup>> mapBloodGroup = md.getAllBloodGroup(jsondata);
			List totalMatches = new ArrayList();
			if (mapBloodGroup.get("bloodGroupList") != null) {
				bloodGroupList = mapBloodGroup.get("bloodGroupList");
				totalMatches = mapBloodGroup.get("totalMatches");
				for (MasBloodGroup bloodGroup : bloodGroupList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (bloodGroup != null) {
						mapObj.put("bloodGroupId", bloodGroup.getBloodGroupId());
						mapObj.put("bloodGroupCode", bloodGroup.getBloodGroupCode());
						mapObj.put("bloodGroupName", bloodGroup.getBloodGroupName());
						mapObj.put("status", bloodGroup.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addBloodGroup(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);

			MasBloodGroup masBloodGroup = new MasBloodGroup();

			masBloodGroup.setBloodGroupCode(Long.valueOf(jsondata.get("bloodGroupCode").toString()));
			masBloodGroup.setBloodGroupName(jsondata.get("bloodGroupName").toString().toUpperCase());
			masBloodGroup.setUser(user);
			masBloodGroup.setStatus("Y");
			masBloodGroup.setLastChgDate(date);

			List<MasBloodGroup> checkBloodGroupList = md.validateBloodGroup(masBloodGroup.getBloodGroupCode(),
					masBloodGroup.getBloodGroupName());
			if (checkBloodGroupList != null && checkBloodGroupList.size() > 0) {
				if ((checkBloodGroupList.get(0).getBloodGroupCode() + "").equalsIgnoreCase(jsondata.get("bloodGroupCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Blood Group Code already Exists");
				}
				if (checkBloodGroupList.get(0).getBloodGroupName()
						.equalsIgnoreCase(jsondata.get("bloodGroupName").toString())) {

					json.put("status", 2);
					json.put("msg", "Blood Group Name already Exists");
				}

			} else {
				String addBloodGroupObj = md.addBloodGroup(masBloodGroup);
				if (addBloodGroupObj != null && addBloodGroupObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateBloodGroupDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("bloodGroupCode").toString() != null
					&& !jsonObject.get("bloodGroupCode").toString().trim().equalsIgnoreCase("")) {

				List<MasBloodGroup> msBloodGroupList = md.validateBloodGroupUpdate(
						Long.parseLong(jsonObject.get("bloodGroupCode").toString()), jsonObject.get("bloodGroupName").toString());
				if (msBloodGroupList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Blood Group Name already exists\"}";
				}

				String updateBloodGroup = md.updateBloodGroupDetails(
						Long.parseLong(jsonObject.get("bloodGroupId").toString()),
						Long.parseLong(jsonObject.get("bloodGroupCode").toString()), jsonObject.get("bloodGroupName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()));

				if (updateBloodGroup != null && updateBloodGroup.equalsIgnoreCase("200")) {
					json.put("updateBloodGroup", updateBloodGroup);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateBloodGroupStatus(JSONObject BloodGroup, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (BloodGroup.get("bloodGroupCode").toString() != null
					&& !BloodGroup.get("bloodGroupCode").toString().trim().equalsIgnoreCase("")) {

				MasBloodGroup mBloodGroup = md.checkBloodGroup(BloodGroup.get("bloodGroupCode").toString());

				if (mBloodGroup != null) {
					String bloodGroupStatus = md.updateBloodGroupStatus(
							Long.parseLong(BloodGroup.get("bloodGroupId").toString()),
							Long.parseLong(BloodGroup.get("bloodGroupCode").toString()), BloodGroup.get("status").toString()
							,Long.parseLong(BloodGroup.get("userId").toString()));

					if (bloodGroupStatus != null && bloodGroupStatus.equalsIgnoreCase("200")) {
						jsonObject.put("bloodGroupStatus", bloodGroupStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}


	/***************************************
	 * MAS Sample
	 ***********************************************************************/

	@Override
	public String getAllSample(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasSample> sampleList = new ArrayList<MasSample>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasSample>> mapSample = md.getAllSample(jsondata);
			List totalMatches = new ArrayList();
			if (mapSample.get("sampleList") != null) {
				sampleList = mapSample.get("sampleList");
				totalMatches = mapSample.get("totalMatches");
				for (MasSample sample : sampleList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (sample != null) {
						mapObj.put("sampleId", sample.getSampleId());
						mapObj.put("sampleCode", sample.getSampleCode());
						mapObj.put("sampleDescription", sample.getSampleDescription());
						mapObj.put("status", sample.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addSample(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			// Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);

			MasSample masSample = new MasSample();

			masSample.setSampleCode(jsondata.get("sampleCode").toString().toUpperCase());
			masSample.setSampleDescription(jsondata.get("sampleDescription").toString().toUpperCase());
			 masSample.setLastChgDate(date);
			masSample.setStatus("Y");
			masSample.setUser(users);	

			List<MasSample> checkSampleList = md.validateSample(masSample.getSampleCode(),
					masSample.getSampleDescription());
			if (checkSampleList != null && checkSampleList.size() > 0) {
				if (checkSampleList.get(0).getSampleCode().equalsIgnoreCase(jsondata.get("sampleCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Sample Code already Exists");
				}
				if (checkSampleList.get(0).getSampleDescription()
						.equalsIgnoreCase(jsondata.get("sampleDescription").toString())) {

					json.put("status", 2);
					json.put("msg", "Sample Name already Exists");
				}

			} else {
				String addSampleObj = md.addSample(masSample);
				if (addSampleObj != null && addSampleObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateSampleDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("sampleCode").toString() != null
					&& !jsonObject.get("sampleCode").toString().trim().equalsIgnoreCase("")) {

				List<MasSample> msSampleList = md.validateSampleUpdate(jsonObject.get("sampleDescription").toString());
				if (msSampleList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Sample Name already exists\"}";
				}

				String updateSample = md.updateSampleDetails(Long.parseLong(jsonObject.get("sampleId").toString()),
						jsonObject.get("sampleCode").toString(), jsonObject.get("sampleDescription").toString()
						,Long.parseLong(jsonObject.get("userId").toString()));

				if (updateSample != null && updateSample.equalsIgnoreCase("200")) {
					json.put("updateSample", updateSample);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateSampleStatus(JSONObject Sample, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (Sample.get("sampleCode").toString() != null
					&& !Sample.get("sampleCode").toString().trim().equalsIgnoreCase("")) {

				//MasSample mSample = md.checkSample(Sample.get("sampleCode").toString());
				Long sid=Long.parseLong(Sample.get("sampleId").toString());

				if (sid != null) {
					String sampleStatus = md.updateSampleStatus(Long.parseLong(Sample.get("sampleId").toString()),
							Sample.get("sampleCode").toString(), Sample.get("status").toString()
							,Long.parseLong(Sample.get("userId").toString()));

					if (sampleStatus != null && sampleStatus.equalsIgnoreCase("200")) {
						jsonObject.put("sampleStatus", sampleStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS UOM
	 ***********************************************************************/

	@Override
	public String getAllUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasUOM> UOMList = new ArrayList<MasUOM>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUOM>> mapUOM = md.getAllUOM(jsondata);
			List totalMatches = new ArrayList();
			if (mapUOM.get("UOMList") != null) {
				UOMList = mapUOM.get("UOMList");
				totalMatches = mapUOM.get("totalMatches");
				for (MasUOM UOM : UOMList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (UOM != null) {
						mapObj.put("UOMId", UOM.getUOMId());
						mapObj.put("UOMCode", UOM.getUOMCode());
						mapObj.put("UOMName", UOM.getUOMName());
						mapObj.put("status", UOM.getUOMStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			//Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			MasUOM masUOM = new MasUOM();

			masUOM.setUOMCode(jsondata.get("UOMCode").toString().toUpperCase());
			masUOM.setUOMName(jsondata.get("UOMName").toString().toUpperCase());
			masUOM.setUser(users);
			masUOM.setUOMStatus("Y");
			masUOM.setLastChgDate(date);

			List<MasUOM> checkUOMList = md.validateUOM(masUOM.getUOMCode(), masUOM.getUOMName());
			if (checkUOMList != null && checkUOMList.size() > 0) {
				if (checkUOMList.get(0).getUOMCode().equalsIgnoreCase(jsondata.get("UOMCode").toString())) {

					json.put("status", 2);
					json.put("msg", "UOM Code already Exists");
				}
				if (checkUOMList.get(0).getUOMName().equalsIgnoreCase(jsondata.get("UOMName").toString())) {

					json.put("status", 2);
					json.put("msg", "UOM Name already Exists");
				}

			} else {
				String addUOMObj = md.addUOM(masUOM);
				if (addUOMObj != null && addUOMObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateUOMDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("UOMCode").toString() != null
					&& !jsonObject.get("UOMCode").toString().trim().equalsIgnoreCase("")) {

				List<MasUOM> msUOMList = md.validateUOMUpdate(jsonObject.get("UOMCode").toString(),
						jsonObject.get("UOMName").toString());
				if (msUOMList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"UOM Name already exists\"}";
				}

				String updateUOM = md.updateUOMDetails(Long.parseLong(jsonObject.get("UOMId").toString()),
						jsonObject.get("UOMCode").toString(), jsonObject.get("UOMName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()));

				if (updateUOM != null && updateUOM.equalsIgnoreCase("200")) {
					json.put("updateUOM", updateUOM);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateUOMStatus(JSONObject UOM, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		if (UOM != null) {
			if (UOM.get("UOMCode").toString() != null && !UOM.get("UOMCode").toString().trim().equalsIgnoreCase("")) {

				MasUOM mUOM = md.checkUOM(UOM.get("UOMCode").toString());

				if (mUOM != null) {
					String UOMStatus = md.updateUOMStatus(Long.parseLong(UOM.get("UOMId").toString()),
							UOM.get("UOMCode").toString(), UOM.get("status").toString()
							,Long.parseLong(UOM.get("userId").toString()));

					if (UOMStatus != null && UOMStatus.equalsIgnoreCase("200")) {
						jsonObject.put("UOMStatus", UOMStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS ItemUnit
	 ***********************************************************************/

	@Override
	public String getAllItemUnit(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreUnit> ItemUnitList = new ArrayList<MasStoreUnit>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasStoreUnit>> mapItemUnit = md.getAllItemUnit(jsondata);
			List totalMatches = new ArrayList();
			if (mapItemUnit.get("ItemUnitList") != null) {
				ItemUnitList = mapItemUnit.get("ItemUnitList");
				totalMatches = mapItemUnit.get("totalMatches");
				for (MasStoreUnit ItemUnit : ItemUnitList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (ItemUnit != null) {
						mapObj.put("storeUnitId", ItemUnit.getStoreUnitId());
						mapObj.put("storeUnitName", ItemUnit.getStoreUnitName());
						mapObj.put("status", ItemUnit.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addItemUnit(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			//Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			MasStoreUnit masStoreUnit = new MasStoreUnit();

			masStoreUnit.setStoreUnitName(jsondata.get("storeUnitName").toString().toUpperCase());
			masStoreUnit.setUser(users);
			masStoreUnit.setStatus("Y");
			masStoreUnit.setLastChgDate(date);

			List<MasStoreUnit> checkItemUnitList = md.validateItemUnit(masStoreUnit.getStoreUnitName());
			if (checkItemUnitList != null && checkItemUnitList.size() > 0) {

				if (checkItemUnitList.get(0).getStoreUnitName()
						.equalsIgnoreCase(jsondata.get("storeUnitName").toString())) {

					json.put("status", 2);
					json.put("msg", "Item Unit Name already Exists");
				}

			} else {
				String addItemUnitObj = md.addItemUnit(masStoreUnit);
				if (addItemUnitObj != null && addItemUnitObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateItemUnitDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("storeUnitName").toString() != null
					&& !jsonObject.get("storeUnitName").toString().trim().equalsIgnoreCase("")) {

				List<MasStoreUnit> msStoreUnitList = md
						.validateItemUnitUpdate(jsonObject.get("storeUnitName").toString());
				if (msStoreUnitList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Item Unit Name already exists\"}";
				}

				String updateItemUnitStatus = md.updateItemUnitDetails(
						Long.parseLong(jsonObject.get("storeUnitId").toString()),
						jsonObject.get("storeUnitName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()));

				if (updateItemUnitStatus != null && updateItemUnitStatus.equalsIgnoreCase("200")) {
					json.put("updateItemUnitStatus", updateItemUnitStatus);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateItemUnitStatus(JSONObject ItemUnit, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObject = new JSONObject();		
		
				if (Long.parseLong(ItemUnit.get("storeUnitId").toString()) !=0) {
					String itemUnitStatus = md.updateItemUnitStatus(
							Long.parseLong(ItemUnit.get("storeUnitId").toString()),
							ItemUnit.get("storeUnitName").toString(), ItemUnit.get("status").toString()
							,Long.parseLong(ItemUnit.get("userId").toString()));

					if (itemUnitStatus != null && itemUnitStatus.equalsIgnoreCase("200")) {
						jsonObject.put("itemUnitStatus", itemUnitStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Item unit id Not Found");
				

			
		}

		return jsonObject.toString();
	}

	/********************************************************
	 * MAINCHARGECODE MASTER
	 *********************************************************/

	@Override
	public String addMainChargecode(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasMainChargecode masMainChargecode = new MasMainChargecode();
		if (!json.equals(null)) {

			if (json.get("mainChargecodeCode") == null) {
				return "{\"status\":\"0\",\"msg\":\"Main Chargecode Code is not contain in json data or it will be null or blank please check\"}";
			}

			if (json.get("mainChargecodeName") == null) {
				return "{\"status\":\"0\",\"msg\":\"Main Chargecode Name is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				masMainChargecode.setMainChargecodeCode(json.get("mainChargecodeCode").toString().toUpperCase());
				masMainChargecode.setMainChargecodeName(json.get("mainChargecodeName").toString().toUpperCase());
				
				 long d = System.currentTimeMillis();
				 Timestamp date = new Timestamp(d);
				 masMainChargecode.setLastChgDate(date);
				 long userId = Long.parseLong(json.get("userId").toString());
					Users users = new Users();
					users.setUserId(userId);
				masMainChargecode.setStatus("y");
				masMainChargecode.setUser(users);
				MasDepartment masDepartment = new MasDepartment();
				masDepartment.setDepartmentId(Long.parseLong(json.get("departmentId").toString()));
				masMainChargecode.setMasDepartment(masDepartment);				

				List<MasMainChargecode> masMainChargecode1 = md.validateMainChargecode(
						masMainChargecode.getMainChargecodeCode().toString(),
						masMainChargecode.getMainChargecodeName().toString());
				if (masMainChargecode1.size() != 0) {
					if (masMainChargecode1 != null && masMainChargecode1.size() > 0) {
						if (masMainChargecode1.get(0).getMainChargecodeCode()
								.equalsIgnoreCase(json.get("mainChargecodeCode").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Main Type Code already Exists\"}";
						}

						else if (masMainChargecode1.get(0).getMainChargecodeName()
								.equalsIgnoreCase(json.get("mainChargecodeName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Main Type Name already Exists\"}";
						}
					}
				} else {
					String masMainChargecodeObj = md.addMainChargecode(masMainChargecode);
					if (masMainChargecodeObj != null && masMainChargecodeObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masMainChargecodeObj != null && masMainChargecodeObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masMainChargecodeObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllMainChargecode(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMainChargecode> mainChargecodeList = new ArrayList<MasMainChargecode>();
		List list = new ArrayList();
		if (jsonObj != null) {
			Map<String, List<MasMainChargecode>> mapMainChargecode = md.getAllMainChargecode(jsonObj);
			List totalMatches = new ArrayList();
			if (mapMainChargecode.get("mainChargecodeList") != null) {
				mainChargecodeList = mapMainChargecode.get("mainChargecodeList");
				totalMatches = mapMainChargecode.get("totalMatches");
				for (MasMainChargecode mainChargecode : mainChargecodeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();					
						mapObj.put("mainChargecodeId", mainChargecode.getMainChargecodeId());
						mapObj.put("mainChargecodeCode", mainChargecode.getMainChargecodeCode());
						mapObj.put("mainChargecodeName", mainChargecode.getMainChargecodeName());
						mapObj.put("status", mainChargecode.getStatus());
						mapObj.put("departmentName", mainChargecode.getMasDepartment() !=null ? mainChargecode.getMasDepartment().getDepartmentName() :"");
						mapObj.put("departmentId", mainChargecode.getMasDepartment() !=null ? mainChargecode.getMasDepartment().getDepartmentId() :"");
						list.add(mapObj);
					
				}
				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String updateMainChargecodeDetails(HashMap<String, Object> mainChargecode, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (mainChargecode.get("mainChargecodeId") != null
				&& !mainChargecode.get("mainChargecodeId").toString().equalsIgnoreCase("")) {
				
				List<MasMainChargecode> mainChargecodeList = md.validateMainChargecodeUpdate(
						mainChargecode.get("mainChargecodeName").toString(),
						Long.parseLong(mainChargecode.get("departmentId").toString()));
				if (mainChargecodeList.size() > 0) {
					return "{\"status\":\"2\",\"msg\":\"Combination of Main Type Name and Department Name already Exists\"}";
				}
				else {
			String mainChargecodeUpdate = md.updateMainChargecode(
					Long.parseLong(mainChargecode.get("mainChargecodeId").toString()),
					mainChargecode.get("mainChargecodeCode").toString(),
					mainChargecode.get("mainChargecodeName").toString(),
					Long.parseLong(mainChargecode.get("departmentId").toString())
					,Long.parseLong(mainChargecode.get("userId").toString())
					);
			if (mainChargecodeUpdate != null && !mainChargecodeUpdate.equalsIgnoreCase("")) {
				json.put("mainChargecodeUpdate", mainChargecodeUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		}

	} else {
		json.put("msg", "No Record Found");
		json.put("status", 0);
	}

	return json.toString();
 }

	@Override
	public String updateMainChargecodeStatus(HashMap<String, Object> mainChargecode, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (mainChargecode.get("mainChargecodeId") != null
				&& !mainChargecode.get("mainChargecodeId").toString().equalsIgnoreCase("")) {						
				String mainChargecodeStatus = md.updateMainChargecodeStatus(Long.parseLong(mainChargecode.get("mainChargecodeId").toString())
						,mainChargecode.get("status").toString());
				if (mainChargecodeStatus != null && mainChargecodeStatus.equalsIgnoreCase("200")) {
					json.put("mainChargecodeStatus", mainChargecodeStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Main Chargecode code is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getDepartmentList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasDepartment> departmentList = md.getDepartmentsList();
		if (departmentList != null && departmentList.size() > 0) {

			jsonObj.put("data", departmentList);
			jsonObj.put("count", departmentList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", departmentList);
			jsonObj.put("count", departmentList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	/********************************************************
	 * USERS MASTER
	 *********************************************************/

	@Override
	public String addUsers(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		Users users = new Users();
		if (!json.equals(null)) {

			if (json.get("loginName") == null) {
				return "{\"status\":\"0\",\"msg\":\"Login Name is not contain in json data or it will be null or blank please check\"}";
			}

			if (json.get("firstName") == null) {
				return "{\"status\":\"0\",\"msg\":\"First Name is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				users.setLoginName(json.get("loginName").toString().toUpperCase());
				users.setUserName(json.get("loginName").toString().toUpperCase());
				users.setPassword(json.get("loginName").toString().toUpperCase());
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				users.setLastChgDate(date);				
					String masUsersObj = md.addUsers(users);
					if (masUsersObj != null && masUsersObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masUsersObj != null && masUsersObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masUsersObj);
						jsonObj.put("status", 0);
					}
				//}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllUsers(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Users> userList = new ArrayList<Users>();
		List list = new ArrayList();
		if (jsonObj != null) {
			Map<String, List<Users>> mapUsers = md.getAllUsers(jsonObj);
			List totalMatches = new ArrayList();
			if (mapUsers.get("usersList") != null) {
				userList = mapUsers.get("usersList");
				totalMatches = mapUsers.get("totalMatches");
				for (Users users : userList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("userId", users.getUserId());
					mapObj.put("loginName", users.getLoginName());
					
					list.add(mapObj);

				}
				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String updateUsersDetails(HashMap<String, Object> users, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (users.get("userId") != null && !users.get("userId").toString().equalsIgnoreCase("")) {

			List<Users> usersList = md.validateUsersUpdate(users.get("loginName").toString(),
					(Long.parseLong(users.get("hospitalId").toString())));
			if (usersList.size() > 0) {
				return "{\"status\":\"0\",\"msg\":\"User Name already exists\"}";
			}

			String usersUpdate = md.updateUsers(Long.parseLong(users.get("userId").toString()),
					users.get("loginName").toString(), users.get("firstName").toString(),
					Long.parseLong(users.get("hospitalId").toString()));
			if (usersUpdate != null && !usersUpdate.equalsIgnoreCase("")) {
				json.put("usersUpdate", usersUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (usersUpdate == null && usersUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"Login Name is not available !!!\"}";
			}
		}

		return json.toString();

	}

	@Override
	public String updateUsersStatus(HashMap<String, Object> users, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (users.get("loginName") != null && !users.get("loginName").toString().equalsIgnoreCase("")) {
			Users checkUsers = md.checkUsers(users.get("loginName").toString());
			if (checkUsers != null) {
				String usersStatus = md.updateUsersStatus(Long.parseLong(users.get("userId").toString()),
						users.get("loginName").toString(), users.get("status").toString());
				if (usersStatus != null && usersStatus.equalsIgnoreCase("200")) {
					json.put("usersStatus", usersStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"loginName is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getHospitalList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasHospital> hospitalList = md.getHospitalList();
		if (hospitalList != null && hospitalList.size() > 0) {

			jsonObj.put("data", hospitalList);
			jsonObj.put("count", hospitalList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", hospitalList);
			jsonObj.put("count", hospitalList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	/***************************************
	 * MAS ROLE
	 ***********************************************************************/

	@Override
	public String getAllRole(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasRole> roleList = new ArrayList<MasRole>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasRole>> mapRole = md.getAllRole(jsondata);
			List totalMatches = new ArrayList();
			if (mapRole.get("roleList") != null) {
				roleList = mapRole.get("roleList");
				totalMatches = mapRole.get("totalMatches");
				for (MasRole role : roleList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (role != null) {
						mapObj.put("roleId", role.getRoleId());
						mapObj.put("roleCode", role.getRoleCode());
						mapObj.put("roleName", role.getRoleName());
						mapObj.put("status", role.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addRole(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			MasRole masRole = new MasRole();

			masRole.setRoleCode((jsondata.get("roleCode")).toString().toUpperCase());
			masRole.setRoleName(jsondata.get("roleName").toString().toUpperCase());
			masRole.setUser(users);
			masRole.setStatus("Y");
			masRole.setLastChgDate(date);

			List<MasRole> checkRoleList = md.validateRole(masRole.getRoleCode(), masRole.getRoleName());
			if (checkRoleList != null && checkRoleList.size() > 0) {
				if (checkRoleList.get(0).getRoleCode().toString()
						.equalsIgnoreCase(jsondata.get("roleCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Role Code is already Existing");
				}
				if (checkRoleList.get(0).getRoleName().equalsIgnoreCase(jsondata.get("roleName").toString())) {

					json.put("status", 2);
					json.put("msg", "Role Name is already Existing");
				}

			}

			else {
				String addRoleObj = md.addRole(masRole);
				if (addRoleObj != null && addRoleObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		}

		else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateRoleDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("roleCode").toString() != null
					&& !jsonObject.get("roleCode").toString().trim().equalsIgnoreCase("")) {

				List<MasRole> msRoleList = md.validateRoleUpdate(
						jsonObject.get("roleName").toString());
				if (msRoleList.size() > 0) {
					
					return "{\"status\":\"0\",\"msg\":\"Role Name already exists\"}";
				}

				String updateRole = md.updateRoleDetails(Long.parseLong(jsonObject.get("roleId").toString()),
						jsonObject.get("roleCode").toString(), jsonObject.get("roleName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()));

				if (updateRole != null && updateRole.equalsIgnoreCase("200")) {
					json.put("updateRole", updateRole);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);
			}
		}
		return json.toString();
	}

	@Override
	public String updateRoleStatus(JSONObject role, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (role.get("roleCode").toString() != null
					&& !role.get("roleCode").toString().trim().equalsIgnoreCase("")) {

				MasRole mRole = md.checkRole(role.get("roleCode").toString());

				if (mRole != null) {
					String roleStatus = md.updateRoleStatus(Long.parseLong(role.get("roleId").toString()),
							role.get("roleCode").toString(), role.get("status").toString()
							,Long.parseLong(role.get("userId").toString()));

					if (roleStatus != null && roleStatus.equalsIgnoreCase("200")) {
						jsonObject.put("roleStatus", roleStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/********************************************************
	 * RANGE MASTER
	 *********************************************************/

	@Override
	public String addRange(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasRange range = new MasRange();

		if (!json.equals(null)) {

			if (json.get("fromRange") == null) {
				return "{\"status\":\"0\",\"msg\":\"From Range is not contain in json data or it will be null or blank please check\"}";
			}

			if (json.get("toRange") == null) {
				return "{\"status\":\"0\",\"msg\":\"To Range is not contain in json data or it will be null or blank please check\"}";
			}
			if (json.get("rangeFlag") == null) {
				return "{\"status\":\"0\",\"msg\":\"Range Type is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				range.setFromRange(Long.parseLong(json.get("fromRange").toString().toUpperCase()));
				range.setToRange(Long.parseLong(json.get("toRange").toString().toUpperCase()));
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				range.setRangeFlag(json.get("rangeFlag").toString().toUpperCase());
				range.setStatus("y");
				range.setLastChgDate(date);
				

				MasAdministrativeSex masAdministrativeSex = new MasAdministrativeSex();
				masAdministrativeSex.setAdministrativeSexId(Long.parseLong(json.get("administrativeSexId").toString()));
				range.setMasAdministrativeSex(masAdministrativeSex);

				List<MasRange> range1 = md.validateRange(Long.parseLong(range.getFromRange().toString()),
						(Long.parseLong(range.getToRange().toString())), range.getRangeFlag().toString());
				if (range1.size() != 0) {
					if (range1 != null && range1.size() > 0) {
						if (range1.get(0).getFromRange().equals(json.get("fromRange").toString())) {

							return "{\"status\":\"2\",\"msg\":\" From Range already Exists\"}";
						}
						if (range1.get(0).getToRange().equals(json.get("toRange").toString())) {

							return "{\"status\":\"2\",\"msg\":\"To Range already Exists\"}";
						}
					}
				} else {
					String masRangeObj = md.addRange(range);
					if (masRangeObj != null && masRangeObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masRangeObj != null && masRangeObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masRangeObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();
	}

	@Override
	public String getAllRange(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasRange> rangeList = new ArrayList<MasRange>();
		List list = new ArrayList();
		if (jsonObj != null) {
			Map<String, List<MasRange>> mapRange = md.getAllRange(jsonObj);
			List totalMatches = new ArrayList();
			if (mapRange.get("rangeList") != null) {
				rangeList = mapRange.get("rangeList");
				totalMatches = mapRange.get("totalMatches");
				for (MasRange range : rangeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("rangeId", range.getRangeId());
					mapObj.put("fromRange", range.getFromRange());
					mapObj.put("toRange", range.getToRange());
					mapObj.put("status", range.getStatus());
					mapObj.put("rangeFlag", range.getRangeFlag());
					mapObj.put("administrativeSexName",
							range.getMasAdministrativeSex() != null
									? range.getMasAdministrativeSex().getAdministrativeSexName()
									: "");
					mapObj.put("administrativeSexId",
							range.getMasAdministrativeSex() != null
									? range.getMasAdministrativeSex().getAdministrativeSexId()
									: "0");

					list.add(mapObj);

				}
				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String updateRangeDetails(HashMap<String, Object> range, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (range.get("rangeId") != null && !range.get("rangeId").toString().equalsIgnoreCase("")) {
			String rangeUpdate = md.updateRange(Long.parseLong(range.get("rangeId").toString()),
					Long.parseLong(range.get("fromRange").toString()), Long.parseLong(range.get("toRange").toString())
					,Long.parseLong(range.get("userId").toString()));
			if (rangeUpdate != null && !rangeUpdate.equalsIgnoreCase("")) {
				json.put("rangeUpdate", rangeUpdate);
				json.put("msg", "Record  Updated Successfully");
				json.put("status", 1);
			} else if (rangeUpdate == null && rangeUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"From Range is not available !!!\"}";
			}
		}

		return json.toString();

	}

	@Override
	public String updateRangeStatus(HashMap<String, Object> range, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
				String rangeStatus = md.updateRangeStatus(Long.parseLong(range.get("rangeId").toString()),
						Long.parseLong(range.get("fromRange").toString()), range.get("status").toString()
						,Long.parseLong(range.get("userId").toString()));
				if (rangeStatus != null && rangeStatus.equalsIgnoreCase("200")) {
					json.put("rangeStatus", rangeStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}

		return json.toString();

	}

	@Override
	public String getGenderList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasAdministrativeSex> genderList = md.getGenderList();
		if (genderList != null && genderList.size() > 0) {

			jsonObj.put("data", genderList);
			jsonObj.put("count", genderList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", genderList);
			jsonObj.put("count", genderList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	@Override
	public String getMasRange(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object> listObject = new ArrayList<Object>();
		try {
			if (jsonObject != null) {
				List<MasRange> rangeListObject = md.getMasRange();
				if (CollectionUtils.isNotEmpty(rangeListObject)) {
					for (Iterator<?> iterator = rangeListObject.iterator(); iterator.hasNext();) {
						Map<String, Object> mapRange = new HashMap<String, Object>();
						MasRange masRange = (MasRange) iterator.next();
						mapRange.put("rangeId", masRange.getRangeId());
						mapRange.put("genderId", masRange.getMasAdministrativeSex().getAdministrativeSexId());
						mapRange.put("fromRange", masRange.getFromRange());
						mapRange.put("toRange", masRange.getToRange());
						mapRange.put("rangeFlag", masRange.getRangeFlag());
						if (masRange.getRangeFlag().equalsIgnoreCase("A")) {
							String fromAgeToAge = masRange.getFromRange().toString().concat("-")
									.concat(masRange.getToRange().toString());
							mapRange.put("fromAgeToAge", fromAgeToAge);
						}
						if (masRange.getRangeFlag().equalsIgnoreCase("H")) {
							String fromHeightToHeight = masRange.getFromRange().toString().concat("-")
									.concat(masRange.getToRange().toString());
							mapRange.put("fromHeightToHeight", fromHeightToHeight);
						}
						listObject.add(mapRange);
					}
				}

				if (listObject != null && listObject.size() > 0) {
					json.put("data", listObject);
					json.put("count", listObject.size());
					json.put("msg", "Record Fetched Successfully");
					json.put("status", 1);
				} else {
					json.put("data", listObject);
					json.put("count", listObject.size());
					json.put("msg", "Record Not Fetched");
					json.put("status", 0);
				}
			} else {
				json.put("msg", "Invalid Input Parameter");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/***************************************
	 * MAS StoreGroup
	 ***********************************************************************/

	@Override
	public String getAllStoreGroup(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreGroup> storeGroupList = new ArrayList<MasStoreGroup>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasStoreGroup>> mapStoreGroup = md.getAllStoreGroup(jsondata);
			List totalMatches = new ArrayList();
			if (mapStoreGroup.get("storeGroupList") != null) {
				storeGroupList = mapStoreGroup.get("storeGroupList");
				totalMatches = mapStoreGroup.get("totalMatches");
				for (MasStoreGroup storeGroup : storeGroupList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (storeGroup != null) {
						mapObj.put("groupId", storeGroup.getGroupId());
						mapObj.put("groupCode", storeGroup.getGroupCode());
						mapObj.put("groupName", storeGroup.getGroupName());
						mapObj.put("status", storeGroup.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addStoreGroup(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasStoreGroup masStoreGroup = new MasStoreGroup();

			masStoreGroup.setGroupCode((jsondata.get("groupCode")).toString().toUpperCase());
			masStoreGroup.setGroupName(jsondata.get("groupName").toString().toUpperCase());
			
			masStoreGroup.setStatus("Y");
			masStoreGroup.setLastChgDate(date);
			long userId = Long.parseLong(jsondata.get("userId").toString());			
			masStoreGroup.setLastChgBy(userId);
			List<MasStoreGroup> checkStoreGroupList = md.validateStoreGroup(masStoreGroup.getGroupCode(),
					masStoreGroup.getGroupName());
			if (checkStoreGroupList != null && checkStoreGroupList.size() > 0) {
				if (checkStoreGroupList.get(0).getGroupCode().toString()
						.equalsIgnoreCase(jsondata.get("groupCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Group Code is already Existing");
				}
				if (checkStoreGroupList.get(0).getGroupName().equalsIgnoreCase(jsondata.get("groupName").toString())) {

					json.put("status", 2);
					json.put("msg", "Group Name already Exists");
				}

			} else {
				String addStoreGroupObj = md.addStoreGroup(masStoreGroup);
				if (addStoreGroupObj != null && addStoreGroupObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateStoreGroup(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("groupCode").toString() != null
					&& !jsonObject.get("groupCode").toString().trim().equalsIgnoreCase("")) {

				List<MasStoreGroup> msStoreGroupList = md.validateStoreGroupUpdate(jsonObject.get("groupName").toString());
				if (msStoreGroupList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Group Name already exists\"}";
				}
				String updateStoreGroup = md.updateStoreGroup(Long.parseLong(jsonObject.get("groupId").toString()),
						jsonObject.get("groupCode").toString(), jsonObject.get("groupName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateStoreGroup != null && updateStoreGroup.equalsIgnoreCase("200")) {
					json.put("updateStoreGroup", updateStoreGroup);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);
			}
		}
		return json.toString();
	}

	@Override
	public String updateStoreGroupStatus(JSONObject StoreGroup, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (StoreGroup.get("groupCode").toString() != null
					&& !StoreGroup.get("groupCode").toString().trim().equalsIgnoreCase("")) {

				MasStoreGroup mStoreGroup = md.checkStoreGroup(StoreGroup.get("groupCode").toString());

				if (mStoreGroup != null) {
					String storeGroupStatus = md.updateStoreGroupStatus(
							Long.parseLong(StoreGroup.get("groupId").toString()),
							StoreGroup.get("groupCode").toString(), StoreGroup.get("status").toString(),
							Long.parseLong(StoreGroup.get("userId").toString()));

					if (storeGroupStatus != null && storeGroupStatus.equalsIgnoreCase("200")) {
						jsonObject.put("storeGroupStatus", storeGroupStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/***************************************
	 * MAS ItemType
	 ***********************************************************************/

	@Override
	public String getAllItemType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasItemType> itemTypeList = new ArrayList<MasItemType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasItemType>> mapItemType = md.getAllItemType(jsondata);
			List totalMatches = new ArrayList();
			if (mapItemType.get("itemTypeList") != null) {
				itemTypeList = mapItemType.get("itemTypeList");
				totalMatches = mapItemType.get("totalMatches");
				for (MasItemType itemType : itemTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (itemType != null) {
						mapObj.put("itemTypeId", itemType.getItemTypeId());
						mapObj.put("itemTypeCode", itemType.getItemTypeCode());
						mapObj.put("itemTypeName", itemType.getItemTypeName());
						mapObj.put("status", itemType.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addItemType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {		

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasItemType masItemType = new MasItemType();

			masItemType.setItemTypeCode((jsondata.get("itemTypeCode")).toString().toUpperCase());
			masItemType.setItemTypeName(jsondata.get("itemTypeName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			masItemType.setStatus("Y");
			masItemType.setLastChgDate(date);
			masItemType.setLastChgBy(userId);
			List<MasItemType> checkItemTypeList = md.validateItemType(masItemType.getItemTypeCode(),
					masItemType.getItemTypeName());
			if (checkItemTypeList != null && checkItemTypeList.size() > 0) {
				if (checkItemTypeList.get(0).getItemTypeCode().toString()
						.equalsIgnoreCase(jsondata.get("itemTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Item Type Code is already Existing");
				}
				if (checkItemTypeList.get(0).getItemTypeName()
						.equalsIgnoreCase(jsondata.get("itemTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Item Type Name already Exists");
				}

			} else {
				String addItemTypeObj = md.addItemType(masItemType);
				if (addItemTypeObj != null && addItemTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateItemType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("itemTypeCode").toString() != null
					&& !jsonObject.get("itemTypeCode").toString().trim().equalsIgnoreCase("")) {

				List<MasItemType> msItemTypeList = md.validateItemTypeUpdate(jsonObject.get("itemTypeName").toString());
				if (msItemTypeList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Item Type Name already exists\"}";
				}

				String updateItemType = md.updateItemType(Long.parseLong(jsonObject.get("itemTypeId").toString()),
						jsonObject.get("itemTypeCode").toString(), jsonObject.get("itemTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateItemType != null && updateItemType.equalsIgnoreCase("200")) {
					json.put("updateItemType", updateItemType);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);
			}
		}
		return json.toString();
	}

	@Override
	public String updateItemTypeStatus(JSONObject itemType, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (itemType.get("itemTypeCode").toString() != null
					&& !itemType.get("itemTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasItemType mItemType = md.checkItemType(itemType.get("itemTypeCode").toString());

				if (mItemType != null) {
					String itemTypeStatus = md.updateItemTypeStatus(
							Long.parseLong(itemType.get("itemTypeId").toString()),
							itemType.get("itemTypeCode").toString(), itemType.get("status").toString(),
							Long.parseLong(itemType.get("userId").toString()));

					if (itemTypeStatus != null && itemTypeStatus.equalsIgnoreCase("200")) {
						jsonObject.put("itemTypeStatus", itemTypeStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}

	/*-------------------MAS StoreSection----------------------*/
	@Override
	public String addStoreSection(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasStoreSection masStoreSection = new MasStoreSection();

		if (!json.equals(null)) {

			
			if (json.get("sectionName") == null) {
				return "{\"status\":\"0\",\"msg\":\"Section Name is not contain in json data or it will be null or blank please check\"}";
			}
			
			if (json.get("sectionCode") == null) {
				return "{\"status\":\"0\",\"msg\":\"Section Code is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				
				masStoreSection.setSectionName(json.get("sectionName").toString().toUpperCase());
				masStoreSection.setSectionCode(json.get("sectionCode").toString().toUpperCase());
				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masStoreSection.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				masStoreSection.setStatus("Y");
				masStoreSection.setUsers(users);
				List<MasStoreSection> masStoreSection1 = md.validateMasStoreSection(
						 masStoreSection.getSectionName().toString(),masStoreSection.getSectionName().toString());
				if (masStoreSection1.size() != 0) {
					if (masStoreSection1 != null && masStoreSection1.size() > 0) {
						
						if (masStoreSection1.get(0).getSectionCode()
								.equalsIgnoreCase(json.get("sectionCode").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Section Code already Exists\"}";
						}
						
						if (masStoreSection1.get(0).getSectionName()
								.equalsIgnoreCase(json.get("sectionName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Section Name already Exists\"}";
						}
					}
				} else {
					String masStoreSectionObj = md.addMasStoreSection(masStoreSection);
					if (masStoreSectionObj != null && masStoreSectionObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masStoreSectionObj != null && masStoreSectionObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masStoreSectionObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllStoreSection(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreSection> sectionList = new ArrayList<MasStoreSection>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasStoreSection>> mapStoreSection = md.getAllStoreSection(jsonObj);
			List totalMatches = new ArrayList();
			if (mapStoreSection.get("masStoreSectionList") != null) {
				sectionList = mapStoreSection.get("masStoreSectionList");
				totalMatches = mapStoreSection.get("totalMatches");
				for (MasStoreSection StoreSection : sectionList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("sectionId", StoreSection.getSectionId());
					mapObj.put("sectionCode", StoreSection.getSectionCode()!= null
							?StoreSection.getSectionCode():"");
					mapObj.put("sectionName", StoreSection.getSectionName());					
					mapObj.put("status", StoreSection.getStatus());					
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (storeSection.get("sectionName") == null
				&& storeSection.get("sectionName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
		} else {
			MasStoreSection chkStoreSection = md.chkStoreSection(storeSection.get("sectionName").toString());
			if (chkStoreSection != null) {
				List<MasStoreSection> masStoreSectionLst = md
						.getStoreSection(storeSection.get("sectionName").toString());
				if (masStoreSectionLst != null && masStoreSectionLst.size() > 0) {
					
					jsonObject.put("masStoreSectionLst", masStoreSectionLst);
					jsonObject.put("msg", "List of Section successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();

		if (storeSection.get("sectionId") != null && !storeSection.get("sectionId").toString().equalsIgnoreCase("")) {
			List<MasStoreSection> msStoreSectionList = md.validateMasStoreSectionUpdate(storeSection.get("sectionName").toString());
			if (msStoreSectionList.size() > 0) {
				return "{\"status\":\"0\",\"msg\":\"Section name already exists\"}";
			}
			String storeSectionUpdate = md.updateStoreSection(Long.parseLong(storeSection.get("sectionId").toString()),
					storeSection.get("sectionName").toString(),	Long.parseLong(storeSection.get("userId").toString()));
			if (storeSectionUpdate != null && !storeSectionUpdate.equalsIgnoreCase("")) {
				json.put("storeSectionUpdate", storeSectionUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (storeSectionUpdate == null && storeSectionUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
			}

		} else {
			return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String statusStoreSection(HashMap<String, Object> storeSection, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		
			if (storeSection != null) {
				String masStoreSectionStatus = md.updateStoreSectionStatus(
						Long.parseLong(storeSection.get("sectionId").toString()),
						 storeSection.get("status").toString(),
						Long.parseLong(storeSection.get("userId").toString()));
				if (masStoreSectionStatus != null && masStoreSectionStatus.equalsIgnoreCase("200")) {
					json.put("masStoreSectionStatus", masStoreSectionStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		

		return json.toString();

	}

	
	/*-------------------MAS ItemClass----------------------*/
	@Override
	public String addItemClass(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasItemClass masItemClass = new MasItemClass();

		if (!json.equals(null)) {

			if (json.get("itemClassCode") == null) {
				return "{\"status\":\"0\",\"msg\":\"Item Class Code is not contain in json data or it will be null or blank please check\"}";
			}
			if (json.get("itemClassName") == null) {
				return "{\"status\":\"0\",\"msg\":\"Item Class Name is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				masItemClass.setItemClassCode(json.get("itemClassCode").toString().toUpperCase());
				masItemClass.setItemClassName(json.get("itemClassName").toString().toUpperCase());

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masItemClass.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				masItemClass.setStatus("y");
				masItemClass.setUsers(users);
				MasStoreSection storeSection = new MasStoreSection();
				storeSection.setSectionId(Long.parseLong(json.get("sectionId").toString()));
				masItemClass.setMasStoreSection(storeSection);
				masItemClass.setUsers(users);
				List<MasItemClass> masItemClass1 = md.validateMasItemClass(masItemClass.getItemClassCode().toString(),
						masItemClass.getItemClassName().toString());
				if (masItemClass1.size() != 0) {
					if (masItemClass1 != null && masItemClass1.size() > 0) {
						if (masItemClass1.get(0).getItemClassCode()
								.equalsIgnoreCase(json.get("itemClassCode").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Item Class Code already Exists\"}";
						}
						if (masItemClass1.get(0).getItemClassName()
								.equalsIgnoreCase(json.get("itemClassName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Item Class Name already Exists\"}";
						}
					}
				} else {
					String masItemClassObj = md.addMasItemClass(masItemClass);
					if (masItemClassObj != null && masItemClassObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masItemClassObj != null && masItemClassObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masItemClassObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllItemClass(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasItemClass> itemClassList = new ArrayList<MasItemClass>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasItemClass>> mapItemClass = md.getAllItemClass(jsonObj);
			List totalMatches = new ArrayList();
			if (mapItemClass.get("masItemClassList") != null) {
				itemClassList = mapItemClass.get("masItemClassList");
				totalMatches = mapItemClass.get("totalMatches");
				for (MasItemClass itemClass : itemClassList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("itemClassId", itemClass.getItemClassId());
					mapObj.put("itemClassName", itemClass.getItemClassName());
					mapObj.put("itemClassCode", itemClass.getItemClassCode());
					mapObj.put("status", itemClass.getStatus());
					mapObj.put("sectionName",
							itemClass.getMasStoreSection() != null ? itemClass.getMasStoreSection().getSectionName()
									: "");
					mapObj.put("sectionId",
							itemClass.getMasStoreSection() != null ? itemClass.getMasStoreSection().getSectionId()
									: "0");
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getItemClass(HashMap<String, Object> itemClass, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (itemClass.get("itemClassName") == null
				&& itemClass.get("itemClassName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"Item Class Code is not available !!!\"}";
		} else {
			MasItemClass chkItemClass = md.chkItemClass(itemClass.get("itemClassName").toString());
			if (chkItemClass != null) {
				List<MasItemClass> masItemClassLst = md.getItemClass(itemClass.get("itemClassName").toString());
				if (masItemClassLst != null && masItemClassLst.size() > 0) {
					
					jsonObject.put("masItemClassLst", masItemClassLst);
					jsonObject.put("msg", "List of Item Class successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateItemClass(HashMap<String, Object> itemClass, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String itemClassUpdate="";
		if (itemClass.get("itemClassId") != null && !itemClass.get("itemClassId").toString().equals("")) {
			
			List<MasItemClass> msItemClassList = md.validateMasItemClassUpdate(Long.parseLong(itemClass.get("sectionId").toString()),
					itemClass.get("itemClassName").toString());
			if (msItemClassList.size() > 0) {
				return "{\"status\":\"2\",\"msg\":\"Combination of Item Class Name and Section Name already Exists\"}";
			}
			
			itemClassUpdate = md.updateItemClass(Long.parseLong(itemClass.get("itemClassId").toString()),
					 itemClass.get("itemClassName").toString(),
					Long.parseLong(itemClass.get("sectionId").toString()),
					Long.parseLong(itemClass.get("userId").toString()));

			if (itemClassUpdate != null && itemClassUpdate.equalsIgnoreCase("200")) {
				json.put("itemClassUpdate", itemClassUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);

			}
			

		} else {
			json.put("msg", "Data Not Found");
			json.put("status", 0);

		}
	return json.toString();
	}
	
	@Override
	public String statusItemClass(HashMap<String, Object> itemClass, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (itemClass.get("itemClassId").toString() != null && !itemClass.get("itemClassId").toString().equalsIgnoreCase("")) {		
			
				String masItemClassStatus = md.updateItemClassStatus(Long.parseLong(itemClass.get("itemClassId").toString()),
						 itemClass.get("status").toString());
				if (masItemClassStatus != null && masItemClassStatus.equalsIgnoreCase("200")) {
					json.put("masItemClassStatus", masItemClassStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			return "{\"status\":\"0\",\"msg\":\"Item Class id is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getStoreSectionList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasStoreSection> storeSectionList = md.getStoreSectionList();
		if (storeSectionList != null && storeSectionList.size() > 0) {

			jsonObj.put("data", storeSectionList);
			jsonObj.put("count", storeSectionList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", storeSectionList);
			jsonObj.put("count", storeSectionList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();

	}

	/*-------------------MAS Section----------------------*/
	@Override
	public String addSection(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasSection masSection = new MasSection();

		if (!json.equals(null)) {

			if (json.get("sectionCode") == null) {
				return "{\"status\":\"0\",\"msg\":\"Section Code is not contain in json data or it will be null or blank please check\"}";
			}
			if (json.get("sectionName") == null) {
				return "{\"status\":\"0\",\"msg\":\"Section Name is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				masSection.setSectionCode(json.get("sectionCode").toString().toUpperCase());
				masSection.setSectionName(json.get("sectionName").toString().toUpperCase());

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masSection.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				masSection.setStatus("y");

				MasHospital hospital = new MasHospital();
				hospital.setHospitalId(Long.parseLong(json.get("hospitalId").toString()));
				masSection.setMasHospital(hospital);

				List<MasSection> masSection1 = md.validateMasSection(masSection.getSectionCode().toString(),
						masSection.getSectionName().toString());
				if (masSection1 != null && masSection1.size() > 0) {
					if (masSection1.get(0).getSectionCode().toString()
							.equalsIgnoreCase(json.get("sectionCode").toString())) {

						json.put("status", 2);
						json.put("msg", "Section Code already Exists");
					}
					if (masSection1.get(0).getSectionName()
							.equalsIgnoreCase(json.get("sectionName").toString())) {

						json.put("status", 2);
						json.put("msg", "Section Name already Exists");
					}
				} else {
					String masSectionObj = md.addMasSection(masSection);
					if (masSectionObj != null && masSectionObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masSectionObj != null && masSectionObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masSectionObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}

	@Override
	public String getAllSection(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasSection> sectionList = new ArrayList<MasSection>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasSection>> mapSection = md.getAllSection(jsonObj);
			List totalMatches = new ArrayList();
			if (mapSection.get("masSectionList") != null) {
				sectionList = mapSection.get("masSectionList");
				totalMatches = mapSection.get("totalMatches");
				for (MasSection Section : sectionList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("sectionId", Section.getSectionId());
					mapObj.put("sectionName", Section.getSectionName());
					mapObj.put("sectionCode", Section.getSectionCode());
					mapObj.put("status", Section.getStatus());
					mapObj.put("hospitalName",
							Section.getMasHospital() != null ? Section.getMasHospital().getHospitalName() : "");
					mapObj.put("hospitalId",
							Section.getMasHospital() != null ? Section.getMasHospital().getHospitalId() : "0");
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getSection(HashMap<String, Object> section, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (section.get("sectionName") == null && section.get("sectionName").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
		} else {
			MasSection chkSection = md.chkSection(section.get("sectionName").toString());
			if (chkSection != null) {
				List<MasSection> masSectionLst = md.getSection(section.get("sectionName").toString());
				if (masSectionLst != null && masSectionLst.size() > 0) {
					
					jsonObject.put("masSectionLst", masSectionLst);
					jsonObject.put("msg", "List of Section successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateSection(HashMap<String, Object> section, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();

		if (section.get("sectionId") != null && !section.get("sectionId").toString().equalsIgnoreCase("")) {
			List<MasSection> msSectionList = md.validateMasSectionUpdate(section.get("sectionName").toString());
			if (msSectionList != null && msSectionList.size() > 0) {
				return "{\"status\":\"0\",\"msg\":\"Section Code already exists\"}";
			}
			String sectionUpdate = md.updateSection(Long.parseLong(section.get("sectionId").toString()),
					section.get("sectionCode").toString(), section.get("sectionName").toString(),
					Long.parseLong(section.get("hospitalId").toString()),
					Long.parseLong(section.get("userId").toString()));
			if (sectionUpdate != null && !sectionUpdate.equalsIgnoreCase("")) {
				json.put("sectionUpdate", sectionUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (sectionUpdate == null && sectionUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
			}

		} else {
			return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String statusSection(HashMap<String, Object> section, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (section.get("sectionCode") != null && !section.get("sectionCode").toString().equalsIgnoreCase("")) {
			MasSection chkSection = md.chkSection(section.get("sectionCode").toString());
			if (chkSection != null) {
				String masSectionStatus = md.updateSectionStatus(Long.parseLong(section.get("sectionId").toString()),
						section.get("sectionCode").toString(), section.get("status").toString(),
						Long.parseLong(section.get("userId").toString()));
				if (masSectionStatus != null && masSectionStatus.equalsIgnoreCase("200")) {
					json.put("masSectionStatus", masSectionStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Section Code is not available !!!\"}";
		}

		return json.toString();

	}

	/*-------------------MAS Drug Item----------------------*/
	@Override
	public String addItem(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasStoreItem masStoreItem = new MasStoreItem();

		if (json !=null) {		
						
				List<MasStoreItem> msItemList = md.validateMasStoreItemUpdate(
						json.get("pvmsNo").toString(), json.get("nomenclature").toString());
				if (msItemList.size() > 0) {
					if(msItemList.get(0).getPvmsNo().equalsIgnoreCase(json.get("pvmsNo").toString())) {
					return "{\"status\":\"0\",\"msg\":\"Drug Name already exists\"}";
					}
					if(msItemList.get(0).getNomenclature().equalsIgnoreCase(json.get("nomenclature").toString())) {
						return "{\"status\":\"0\",\"msg\":\"Drug Name already exists\"}";
					}
			}
			else {
				masStoreItem.setPvmsNo(json.get("pvmsNo").toString());
				masStoreItem.setNomenclature(json.get("nomenclature").toString().toUpperCase());
				if(!json.get("dispUnitQty").toString().equals("") && json.get("dispUnitQty").toString() !=null) {
				masStoreItem.setDispUnitQty(Long.parseLong(json.get("dispUnitQty").toString()));
				}
				masStoreItem.setRolD(Long.parseLong(json.get("rolD").toString()));
				//masStoreItem.setRolS(Long.parseLong(json.get("rolS").toString().toUpperCase()));				
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masStoreItem.setLastChgDate(date);
				long userId = Long.parseLong(json.get("userId").toString());				
				masStoreItem.setLastChgBy(userId);				
				masStoreItem.setStatus("y");				
				//masStoreItem.setHospitalId(Long.parseLong(json.get("hospitalId").toString()));
				masStoreItem.setItemUnitId(Long.parseLong(json.get("storeUnitId").toString()));
				masStoreItem.setDispUnitId(Long.parseLong(json.get("storeDispUnitId").toString()));				
				masStoreItem.setSectionId(Long.parseLong(json.get("sectionId").toString()));				
				masStoreItem.setItemTypeId(Long.parseLong(json.get("itemTypeId").toString()));				
				masStoreItem.setGroupId(Long.parseLong(json.get("groupId").toString()));				
				masStoreItem.setItemClassId(Long.parseLong(json.get("itemClassId").toString()));				
				masStoreItem.setTypeOfItem(json.get("drugType").toString());
				masStoreItem.setFacilityCode(json.get("facilitycode").toString());
				masStoreItem.setEdl(json.get("edl").toString());
				masStoreItem.setSastiDawai(json.get("sastiDawai").toString());
				masStoreItem.setDosage(json.get("dosage").toString());
				if(json.get("nod").toString() !=null && !json.get("nod").toString().isEmpty()) {
				masStoreItem.setNoOfDays(Long.parseLong(json.get("nod").toString()));
				}
				if(json.get("frequency").toString() !=null && !json.get("frequency").toString().isEmpty()) {
				masStoreItem.setFrequencyId(Long.parseLong(json.get("frequency").toString()));
				}
				if(json.get("drugType").toString().equals("E")) {
					masStoreItem.setDangerousDrug(json.get("dangerousDrug").toString());
				}
				masStoreItem.setInactiveForEntry(json.get("inactiveForEntry").toString());
				String masStoreItemObj = md.addMasStoreItem(masStoreItem);
					if (masStoreItemObj != null && masStoreItemObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masStoreItemObj != null && masStoreItemObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", masStoreItemObj);
						jsonObj.put("status", 0);
					}
				}
			}
		return jsonObj.toString();

	}

	@Override
	public String getAllItem(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreItem> storeItemList = new ArrayList<MasStoreItem>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasStoreItem>> mapStoreItem = md.getAllStoreItem(jsonObj);
			List totalMatches = new ArrayList();
			if (mapStoreItem.get("masStoreItemList") != null) {
				storeItemList = mapStoreItem.get("masStoreItemList");
				totalMatches = mapStoreItem.get("totalMatches");
				for (MasStoreItem storeItem : storeItemList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("itemId", storeItem.getItemId());
					mapObj.put("pvmsNo", storeItem.getPvmsNo());
					mapObj.put("nomenclature", storeItem.getNomenclature() !=null ? storeItem.getNomenclature() :"");
					mapObj.put("status", storeItem.getStatus());
					mapObj.put("dispUnitQty", storeItem.getDispUnitQty() !=null ? storeItem.getDispUnitQty() :"");
					mapObj.put("rolD", storeItem.getRolD() !=null ? storeItem.getRolD() :"");
					//mapObj.put("rolS", storeItem.getRolS() !=null ? storeItem.getRolS() :"");				
					mapObj.put("groupName",
							storeItem.getMasStoreGroup() != null ? storeItem.getMasStoreGroup().getGroupName() : "");
					mapObj.put("groupId",
							storeItem.getMasStoreGroup() != null ? storeItem.getMasStoreGroup().getGroupId() : "0");
					
					mapObj.put("storeUnitName",
							storeItem.getMasStoreUnit() != null ? storeItem.getMasStoreUnit().getStoreUnitName() : "");
					mapObj.put("storeUnitId",
							storeItem.getMasStoreUnit() != null ? storeItem.getMasStoreUnit().getStoreUnitId() : "0");
					
					mapObj.put("sectionName",
							storeItem.getMasStoreSection() != null ? storeItem.getMasStoreSection().getSectionName() : "");
					mapObj.put("sectionId",
							storeItem.getMasStoreSection() != null ? storeItem.getMasStoreSection().getSectionId() : "0");
					
					mapObj.put("itemClassName",
							storeItem.getMasItemClass() != null ? storeItem.getMasItemClass().getItemClassName() : "");
					mapObj.put("itemClassId",
							storeItem.getMasItemClass() != null ? storeItem.getMasItemClass().getItemClassId() : "0");
					
					mapObj.put("itemTypeName",
							storeItem.getMasItemType() != null ? storeItem.getMasItemType().getItemTypeName() : "");
					mapObj.put("itemTypeId",
							storeItem.getMasItemType() != null ? storeItem.getMasItemType().getItemTypeId() : "0");
					
					mapObj.put("hospitalName",
							storeItem.getMasHospital() != null ? storeItem.getMasHospital().getHospitalName() : "");
					//mapObj.put("hospitalId", storeItem.getMasHospital() != null ? storeItem.getMasHospital().getHospitalId() : "0");
					mapObj.put("dangerousDrug", storeItem.getDangerousDrug()!=null ? storeItem.getDangerousDrug() : "N");	
					mapObj.put("edl", storeItem.getEdl()!=null ? storeItem.getEdl() : "N");					
					mapObj.put("sastiDawai", storeItem.getSastiDawai()!=null ? storeItem.getSastiDawai() : "N");
					mapObj.put("facilityCode", storeItem.getFacilityCode() !=null ? storeItem.getFacilityCode() :"");
					
					mapObj.put("dosage", storeItem.getDosage() !=null ? storeItem.getDosage() :"");
					mapObj.put("nod", storeItem.getNoOfDays() !=null ? storeItem.getNoOfDays() :"");
					mapObj.put("frequency", storeItem.getMasFrequency() !=null && storeItem.getMasFrequency().getFrequencyId() !=null ? storeItem.getMasFrequency().getFrequencyId() :"");
					
					mapObj.put("unitAUId", storeItem.getItemUnitId() !=null ? storeItem.getItemUnitId() :"" );
					mapObj.put("dispensingUnitId", storeItem.getMasStoreUnit() != null ? storeItem.getMasStoreUnit().getStoreUnitId() :"");
					mapObj.put("inactiveForEntry", storeItem.getInactiveForEntry()!=null ? storeItem.getInactiveForEntry() : "N");
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getItem(HashMap<String, Object> item, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if (item.get("pvmsNo") == null && item.get("pvmsNo").toString().trim().equalsIgnoreCase("")) {

			return "{\"status\":\"0\",\"msg\":\"PVMS No is not available !!!\"}";
		} else {
			MasStoreItem chkItem = md.chkStoreItem(item.get("pvmsNo").toString(),item.get("pvmsNo").toString());
			if (chkItem != null) {
				List<MasStoreItem> masStoreItemLst = (List<MasStoreItem>) md.chkStoreItem(item.get("pvmsNo").toString(),
						item.get("pvmsNo").toString());
				if (masStoreItemLst != null && masStoreItemLst.size() > 0) {
					jsonObject.put("masStoreItemLst", masStoreItemLst);
					jsonObject.put("msg", "List of Item successfully...");
					jsonObject.put("status", 1);
				} else {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}
			} else {
				return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			}
		}

		return jsonObject.toString();
	}

	@Override
	public String updateItem(HashMap<String, Object> item, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String itemUpdate="";
		
		if (item.get("itemId") != null && !item.get("itemId").toString().equalsIgnoreCase("")) {	
			MasStoreItem masStoreItemobj=md.getMasStoreItemById(Long.parseLong(item.get("itemId").toString()));
			List<MasStoreItem> msItemList = md.validateMasStoreItemUpdate(item.get("pvmsNo").toString(), item.get("nomenclature").toString());			
			
			if (msItemList.size() > 0) {
				
				if(msItemList.get(0).getPvmsNo().equalsIgnoreCase(item.get("pvmsNo").toString())) {
					masStoreItemobj.setPvmsNo(masStoreItemobj.getPvmsNo());
				}
				else {
					masStoreItemobj.setPvmsNo(item.get("pvmsNo").toString());
				}
				if(msItemList.get(0).getNomenclature().equalsIgnoreCase(item.get("nomenclature").toString()))
				{
					masStoreItemobj.setNomenclature(masStoreItemobj.getNomenclature());	
				}
				else {
					masStoreItemobj.setNomenclature(item.get("nomenclature").toString());	
				}
				
			 }		
					
			        masStoreItemobj.setGroupId(Long.parseLong(item.get("groupId").toString()));
			        masStoreItemobj.setSectionId(Long.parseLong(item.get("sectionId").toString()));
			        masStoreItemobj.setItemClassId(Long.parseLong(item.get("itemClassId").toString()));
			        masStoreItemobj.setItemTypeId(Long.parseLong(item.get("itemTypeId").toString()));			
			        masStoreItemobj.setItemUnitId(Long.parseLong(item.get("storeUnitAuId").toString()));
			        masStoreItemobj.setDispUnitId(Long.parseLong(item.get("storeDispUnitId").toString()));
					if(item.get("dispUnitQty").toString()!=null && !item.get("dispUnitQty").toString().equals("") ){
						masStoreItemobj.setDispUnitQty(Long.parseLong(item.get("dispUnitQty").toString()));
					}
					
					masStoreItemobj.setRolD(Long.parseLong(item.get("rolD").toString()));
					// masStoreItemobj.setRolS(Long.parseLong(item.get("rolS").toString()));						
					masStoreItemobj.setLastChgBy(Long.parseLong(item.get("userId").toString()));
					//masStoreItemobj.setHospitalId(Long.parseLong(item.get("hospitalId").toString()));
					masStoreItemobj.setFacilityCode(item.get("facilitycode").toString());
					masStoreItemobj.setEdl(item.get("edl").toString());
					masStoreItemobj.setSastiDawai(item.get("sastiDawai").toString());
					
					masStoreItemobj.setDosage(item.get("dosage").toString());
					if(item.get("nod").toString() !=null && !item.get("nod").toString().isEmpty())
					{
					masStoreItemobj.setNoOfDays(Long.parseLong(item.get("nod").toString()));
					}
					if(item.get("frequency").toString() !=null && !item.get("frequency").toString().isEmpty()) {
					masStoreItemobj.setFrequencyId(Long.parseLong(item.get("frequency").toString()));
					}
					
					if(item.get("drugType").toString().equals("E")) {
						masStoreItemobj.setDangerousDrug(item.get("dangerousDrug").toString());
					}
					
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);				
					masStoreItemobj.setLastChgDate(date);	
					masStoreItemobj.setInactiveForEntry(item.get("inactiveForEntry").toString());
					itemUpdate = md.updateStoreItem(masStoreItemobj);
					if (itemUpdate != null && itemUpdate.equalsIgnoreCase("200")) {
					json.put("itemUpdate", itemUpdate);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
					}else if (itemUpdate == null && itemUpdate.equalsIgnoreCase("")) {
						json.put("msg", "Record Not Updated");
						json.put("status", 0);
					
					}
				
				return json.toString();
				}
				
			
		else {
			json.put("msg", "Item id not found");
			
		}

		return json.toString();

	}

	@Override
	public String statusItem(HashMap<String, Object> item, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (Long.parseLong(item.get("itemId").toString()) != 0 ) {		
			
				String masItemStatus = md.updateStoreItemStatus(Long.parseLong(item.get("itemId").toString()),
						 item.get("status").toString(),
						Long.parseLong(item.get("userId").toString()));
				if (masItemStatus != null && masItemStatus.equalsIgnoreCase("200")) {
					json.put("masItemStatus", masItemStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				
			}
		} else {
			return "{\"status\":\"0\",\"msg\":\"Item id is not available !!!\"}";
		}

		return json.toString();

	}
	
	@Override
	public String addMEMBMaster(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		if (json != null) {			
			MasMedExam masexam=new MasMedExam();
			masexam.setMedicalExamName(json.get("examName").toString().toUpperCase());
			masexam.setMedicalExamCode(json.get("examCode").toString().toUpperCase());
			masexam.setAppointmentTypeId(Long.parseLong(json.get("appointmentTypeId").toString()));
			masexam.setOnlineOffline(json.get("onlineOffline").toString());
			long d = System.currentTimeMillis();			
			Timestamp date = new Timestamp(d);			
			masexam.setLastChgDate(date);
			Users users = new Users();
			users.setUserId(Long.parseLong(json.get("userId").toString()));
			masexam.setUser(users);		
			masexam.setStatus("Y");				
			List<MasMedExam> membExam = md.validateMEMBMaster(json.get("examName").toString().toUpperCase(),
																	json.get("examCode").toString().toUpperCase());			
			
				if (membExam != null && membExam.size() > 0) {
					
					if (membExam.get(0).getMedicalExamName().equalsIgnoreCase(json.get("examName").toString())) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Exam Name already Exists");						
					}
					
					if (membExam.get(0).getMedicalExamCode().equalsIgnoreCase(json.get("examCode").toString())) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Exam Code already Exists");						
					}
				}
			 else {
				String membObj = md.addMEMBMaster(masexam);

				if (membObj != null && membObj.equalsIgnoreCase("200")) {
					jsonObj.put("status", 1);
					jsonObj.put("msg", "Record Added Successfully");

				} else if (membObj != null && membObj.equalsIgnoreCase("403")) {
					jsonObj.put("status", 0);
					jsonObj.put("msg", "Record Not Added");

				} else {
					jsonObj.put("msg", membObj);
					jsonObj.put("status", 0);
				}
			
			}	
		} 

		return jsonObj.toString();
	}
	
	@Override
	public String getAllMEMBMaster(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMedExam> mList = new ArrayList<MasMedExam>();

		Map<String, List<MasMedExam>> membMap = md.getAllMEMBMaster(jsonObj);
		List cListObj = new ArrayList();
		List totalMatches = new ArrayList();
		if (membMap.get("masMEMBList") != null) {
			mList = membMap.get("masMEMBList");
			totalMatches = membMap.get("totalMatches");

			for (MasMedExam membExam : mList) {
				HashMap<String, Object> mapObj = new HashMap<String, Object>();

				mapObj.put("membId",membExam.getMedicalExamId());
				mapObj.put("examCode", membExam.getMedicalExamCode());
				mapObj.put("examName", membExam.getMedicalExamName());
				mapObj.put("examTypeId", membExam.getMasAppointmentType()!=null ? membExam.getAppointmentTypeId():"");
				mapObj.put("examType", membExam.getMasAppointmentType()!=null ? membExam.getMasAppointmentType().getAppointmentTypeName():"");
				mapObj.put("status", membExam.getStatus());
				mapObj.put("onlineOffline", membExam.getOnlineOffline() !=null ? membExam.getOnlineOffline() :"");
				cListObj.add(mapObj);
			}

			if (cListObj != null && cListObj.size() > 0) {
				json.put("data", cListObj);

				json.put("count", totalMatches.size());
				json.put("status", 1);
			} else {
				json.put("data", cListObj);
				json.put("count", totalMatches.size());
				json.put("msg", "Data not found");
				json.put("status", 0);
			}
		}

		return json.toString();
	}
	
	
	@Override
	public String updateMEMBMaster(HashMap<String, Object> exam, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (exam.get("membId") != null && !exam.get("membId").toString().equalsIgnoreCase("")) {

			String membUpdate = md.updateMEMBMaster(Long.parseLong(exam.get("membId").toString()),
					 exam.get("examName").toString(),exam.get("examCode").toString(),
					Long.parseLong(exam.get("userId").toString()),exam.get("onlineOffline").toString());

			if (membUpdate != null && !membUpdate.equalsIgnoreCase("")) {
				json.put("membUpdate", membUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (membUpdate == null && membUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated!!!");
				json.put("status", 0);
			}

			else {
				json.put("msg", "Exam Code is not available");
				json.put("status", 0);

			}

		} 
		
		return json.toString();
	}
	
	@Override
	public String updateMEMBStatus(HashMap<String, Object> memb, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (memb.get("examId").toString() != null && !memb.get("status").toString().equalsIgnoreCase("")) {			
				String membStatus = md.updateMEMBStatus(Long.parseLong(memb.get("examId").toString()),
						memb.get("status").toString());

				if (membStatus != null && membStatus.equalsIgnoreCase("200")) {
					json.put("membStatus", membStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"Exam Code is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String getInvestigationNameList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<DgMasInvestigation> invList = md.getInvestigationNameList();
		List<Map<String,Object>> listall=new ArrayList<Map<String,Object>>();
		if (invList != null && invList.size() > 0) {
			for(DgMasInvestigation dgInv :invList) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("id",dgInv.getInvestigationId());
				map.put("name",dgInv.getInvestigationName());
				listall.add(map);
			}
			jsonObj.put("data", listall);
			jsonObj.put("count", listall.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", listall);
			jsonObj.put("count", 0);
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		
		return jsonObj.toString();
	}
	
	
	@Override
	public String saveMEInvestigation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String result ="";
		try {			
			
						
			List<MasInvestignationMapping> allMasInv=md.getAllMEMappingList();			
			MasInvestignationMapping masInvestignationMapping=new MasInvestignationMapping();			
			masInvestignationMapping.setInvestignationId(jsonObject.get("investigationId").toString());
			masInvestignationMapping.setAge(jsonObject.get("age").toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"",""));
			masInvestignationMapping.setStatus("Y");			
			masInvestignationMapping.setLastChgBy(Long.parseLong(jsonObject.get("userId").toString()) ); 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);				
			masInvestignationMapping.setLastChgDate(date);	
			
			String ages[]=jsonObject.get("age").toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","").split(",");
			boolean foundAge=false;
			if(allMasInv !=null && allMasInv.size()>0) {
			for(String ag : ages)
			{				
			
			foundAge = allMasInv.stream().anyMatch(s -> s.getAge().contains(ag));
			if (foundAge) {
				break;
			  }
		    }
				if (foundAge) {

					json.put("status", 2);
					json.put("msg", "Age already Exists");

				} else {
					result = md.saveMEInvestigation(masInvestignationMapping);
					if(result.equals("success") && !result.equals("")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					}else {
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}
			}			
			else {			
					
			    result =  md.saveMEInvestigation(masInvestignationMapping);
			
			if(result.equals("success") && !result.equals("")) {
				json.put("status", 1);
				json.put("msg", "Record Added Successfully");
			}else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		 }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

	@Override
	public String getAllInvestigationMapping(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasInvestignationMapping> invList = new ArrayList<MasInvestignationMapping>();

		Map<String, List<MasInvestignationMapping>> meInvMap = md.getAllInvestigationMapping(jsonObj);
		List cListObj = new ArrayList();
		List totalMatches = new ArrayList();
		if (meInvMap.get("meInvestigationList") != null) {
			invList = meInvMap.get("meInvestigationList");
			totalMatches = meInvMap.get("totalMatches");

			for (MasInvestignationMapping masInv : invList) {
				HashMap<String, Object> mapObj = new HashMap<String, Object>();
				mapObj.put("investignationMappingId",masInv.getId());
				mapObj.put("age", masInv.getAge().toString());
				
			String investigationName = md.getMasDesignationByDesignationId(masInv.getInvestignationId());
			if(StringUtils.isNotEmpty(investigationName)&& !investigationName.equalsIgnoreCase("##")) {
				String [] invMappingArray=investigationName.split("##");
				mapObj.put("investignationMappingId", masInv.getId());
				mapObj.put("investigationId", invMappingArray[1]);
				mapObj.put("investigationName", invMappingArray[0].replace(", ", "; "));
			}
				mapObj.put("status", masInv.getStatus());			
				cListObj.add(mapObj);
			}

			if (cListObj != null && cListObj.size() > 0) {
				json.put("data", cListObj);
				json.put("count", totalMatches.size());
				json.put("status", 1);
			} else {
				json.put("data", cListObj);
				json.put("count", totalMatches.size());
				json.put("msg", "Data not found");
				json.put("status", 0);
			}
		}

		return json.toString();
	}
	
	
	@Override
	public String updateMEInvestStatus(HashMap<String, Object> meinv, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (meinv.get("invMappingId").toString() != null && !meinv.get("status").toString().equalsIgnoreCase("")) {			
				String meinvStatus = md.updateMEInvestStatus(Long.parseLong(meinv.get("invMappingId").toString()),
						meinv.get("status").toString());

				if (meinvStatus != null && meinvStatus.equalsIgnoreCase("200")) {
					json.put("meinvStatus", meinvStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateInvestigationMapping(HashMap<String, Object> meinv, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		Long mapid=Long.parseLong(meinv.get("invMappingId").toString());		
		List<MasInvestignationMapping> allMasInv=md.getAllMEMappingList();
		List<MasInvestignationMapping>listMasInvestignationMapping=md.getAllMEMappingById(mapid);		
		allMasInv.removeAll(listMasInvestignationMapping);		
		String ages[]=meinv.get("age").toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","").split(",");
		boolean foundAge=false;
		for(String ag : ages)
		{				
		
		foundAge = allMasInv.stream().anyMatch(s -> s.getAge().contains(ag));
		if (foundAge) {	
			break;
		}
	    }
		if (foundAge) {				
			 
				json.put("status", 2);
				json.put("msg", "Age already Exists");						
		}
		else {
		if (mapid != null && mapid !=0) {
			
			String meinvUpdate = md.updateInvestigationMapping(mapid,
					meinv.get("investigationId").toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"","")
					,meinv.get("age").toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"",""),
					Long.parseLong(meinv.get("userId").toString()));

			if (meinvUpdate != null && !meinvUpdate.equalsIgnoreCase("")) {
				json.put("meinvUpdate", meinvUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (meinvUpdate == null && meinvUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated!!!");
				json.put("status", 0);
			}

			else {
				json.put("msg", "Id is not available");
				json.put("status", 0);

			}

		} 
		}
		return json.toString();
	}
	
	
	/*************************Sub Type Master************************************/
	
	@Override
	public String getMainTypeList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasMainChargecode> subtypetList = md.getMainTypeList();
		if (subtypetList != null && subtypetList.size() > 0) {

			jsonObj.put("data", subtypetList);
			jsonObj.put("count", subtypetList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", subtypetList);
			jsonObj.put("count", subtypetList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	@Override
	public String addSubType(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasSubChargecode masSubChargecode = new MasSubChargecode();
		if (!json.equals(null)) {			
				
				masSubChargecode.setSubChargecodeCode(json.get("subTypeCode").toString().toUpperCase());
				masSubChargecode.setSubChargecodeName(json.get("subTypeName").toString().toUpperCase());				
				
				 long d = System.currentTimeMillis();
				 Timestamp date = new Timestamp(d);
				 masSubChargecode.setLastChgDate(date);
				 long userId = Long.parseLong(json.get("userId").toString());
				 masSubChargecode.setLastChgBy(userId);
				 masSubChargecode.setStatus("y");				 								
				masSubChargecode.setMainChargecodeId(Long.parseLong(json.get("mainTypeId").toString()));					
				List<MasSubChargecode> masSubChargecode1 = md.validateSubType(json.get("subTypeCode").toString().toUpperCase(),
						json.get("subTypeName").toString().toUpperCase());
				if (masSubChargecode1.size() != 0) {
					if (masSubChargecode1 != null && masSubChargecode1.size() > 0) {
						if (masSubChargecode1.get(0).getSubChargecodeCode()
								.equalsIgnoreCase(json.get("subTypeCode").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Sub Type Code already Exists\"}";
						}

						else if (masSubChargecode1.get(0).getSubChargecodeName()
								.equalsIgnoreCase(json.get("subTypeName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"Sub Type Name already Exists\"}";
						}
					}
				} else {
					String massubChargecodeObj = md.addSubType(masSubChargecode);
					if (massubChargecodeObj != null && massubChargecodeObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (massubChargecodeObj != null && massubChargecodeObj.equalsIgnoreCase("500")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", massubChargecodeObj);
						jsonObj.put("status", 0);
					}
				}
			
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}
	
	@Override
	public String getAllSubTypeDetails(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasSubChargecode> subtypeList = new ArrayList<MasSubChargecode>();
		List list = new ArrayList();
		if (jsonObj != null) {
			Map<String, List<MasSubChargecode>> mapSubChargecode = md.getAllSubTypeDetails(jsonObj);
			List totalMatches = new ArrayList();
			if (mapSubChargecode.get("subChargecodeList") != null) {
				subtypeList = mapSubChargecode.get("subChargecodeList");
				totalMatches = mapSubChargecode.get("totalMatches");
				for (MasSubChargecode subChargecode : subtypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();					
						mapObj.put("subChargecodeId", subChargecode.getSubChargecodeId());
						mapObj.put("subChargecodeCode", subChargecode.getSubChargecodeCode());
						mapObj.put("subChargecodeName", subChargecode.getSubChargecodeName());
						mapObj.put("status", subChargecode.getStatus());
						if(subChargecode.getMasMainChargecode()!=null && StringUtils.isNotEmpty(subChargecode.getMasMainChargecode().getMainChargecodeName())) {
						mapObj.put("mainTypeName", subChargecode.getMasMainChargecode().getMainChargecodeName());
						}
						else {
							mapObj.put("mainTypeName","");
						}
						if(subChargecode.getMasMainChargecode()!=null && subChargecode.getMasMainChargecode().getMainChargecodeId()!=null) {
						mapObj.put("mainTypeId", subChargecode.getMasMainChargecode().getMainChargecodeId());
						}
						else {
							mapObj.put("mainTypeId","");
						}
						list.add(mapObj);
					
				}
				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	
	@Override
	public String updateSubTypeStatus(HashMap<String, Object> subtype, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (subtype.get("subTypeId").toString() != null && !subtype.get("status").toString().equalsIgnoreCase("")) {			
				String subtypeStatus = md.updateSubTypeStatus(Long.parseLong(subtype.get("subTypeId").toString()),
						subtype.get("status").toString());

				if (subtypeStatus != null && subtypeStatus.equalsIgnoreCase("200")) {
					//json.put("subtypeStatus", subtypeStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateSubTypeDetails(HashMap<String, Object> subChargecode, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (subChargecode.get("subTypeId") != null
				&& !subChargecode.get("subTypeId").toString().equalsIgnoreCase("")) {			

			String subChargecodeUpdate = md.updateSubTypeDetails(
					Long.parseLong(subChargecode.get("subTypeId").toString()),
					subChargecode.get("subTypeCode").toString(),
					subChargecode.get("subTypeName").toString(),
					Long.parseLong(subChargecode.get("mainTypeId").toString())
					,Long.parseLong(subChargecode.get("userId").toString())
					);
			if (subChargecodeUpdate != null && !subChargecodeUpdate.equalsIgnoreCase("")) {
				json.put("subChargecodeUpdate", subChargecodeUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (subChargecodeUpdate == null && subChargecodeUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

			else {
				return "{\"status\":\"0\",\"msg\":\"sub Chargecode Code is not available !!!\"}";
			}
		}

		return json.toString();

	}
	
	/***************************************
	 * Vendor Type
	 ***********************************************************************/

	@Override
	public String getAllVendorType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMmuType> vendorTypeList = new ArrayList<MasMmuType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasMmuType>> mapVendorType = md.getAllVendorType(jsondata);
			List totalMatches = new ArrayList();
			if (mapVendorType.get("vendorTypeList") != null) {
				vendorTypeList = mapVendorType.get("vendorTypeList");
				totalMatches = mapVendorType.get("totalMatches");
				for (MasMmuType vendorType : vendorTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (vendorType != null) {
						mapObj.put("supplierTypeId", vendorType.getMmuTypeId());
						mapObj.put("supplierTypeCode", vendorType.getMmuTypeCode());
						mapObj.put("supplierTypeName", vendorType.getMmuTypeName());
						mapObj.put("status", vendorType.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String addVendorType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasMmuType masStoreSupplierType = new MasMmuType();
			
			masStoreSupplierType.setMmuTypeCode(jsondata.get("supplierTypeCode").toString().toUpperCase());
			masStoreSupplierType.setMmuTypeName(jsondata.get("supplierTypeName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());			
			masStoreSupplierType.setLastChgBy(userId);
			masStoreSupplierType.setStatus("Y");
			masStoreSupplierType.setLastChgDate(date);

			List<MasMmuType> checkVendorTypeList = md.validateVendorType(masStoreSupplierType.getMmuTypeCode(),
					masStoreSupplierType.getMmuTypeName());
			if (checkVendorTypeList != null && checkVendorTypeList.size() > 0) {
				if (checkVendorTypeList.get(0).getMmuTypeCode().equalsIgnoreCase(jsondata.get("supplierTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Vendor Type Code already Exists");
				}
				if (checkVendorTypeList.get(0).getMmuTypeName().equalsIgnoreCase(jsondata.get("supplierTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Vendor Type Name already Exists");
				}

			} else {
				String addVendorTypeObj = md.addVendorType(masStoreSupplierType);
				if (addVendorTypeObj != null && addVendorTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateVendorType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("supplierTypeCode").toString() != null
					&& !jsonObject.get("supplierTypeCode").toString().trim().equalsIgnoreCase("")) {

				List<MasMmuType> msVendorTypeList = md.validateVendorTypeUpdate(jsonObject.get("supplierTypeCode").toString(),
						jsonObject.get("supplierTypeName").toString());
		
				if (msVendorTypeList.get(0).getMmuTypeName().equalsIgnoreCase(jsonObject.get("supplierTypeName").toString())) {

						json.put("status", 2);
						json.put("msg", "Vendor Type Name already Exists");
					
				}
				else {
				String updateVendorType = md.updateVendorTypeDetails(
						Long.parseLong(jsonObject.get("supplierTypeId").toString()),
						jsonObject.get("supplierTypeCode").toString(), jsonObject.get("supplierTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));
				
				if (updateVendorType != null && updateVendorType.equalsIgnoreCase("200")) {
					json.put("updateVendorType", updateVendorType);
					json.put("msg", "Record successfully Updated.");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
				
			}	

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateVendorTypeStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("supplierTypeCode").toString() != null
					&& !jsonObject.get("supplierTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasMmuType mStoreSupplierType = md.checkVendorType(jsonObject.get("supplierTypeCode").toString());

				if (mStoreSupplierType != null) {
					String vendorTypeStatus = md.updateVendorTypeStatus(Long.parseLong(jsonObject.get("supplierTypeId").toString()),
							jsonObject.get("supplierTypeCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (vendorTypeStatus != null && vendorTypeStatus.equalsIgnoreCase("200")) {
						json.put("vendorTypeStatus", vendorTypeStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}	
	
	/***************************************
	 * Vendor Master
	 ***********************************************************************/
	
	@Override
	public String getAllVendor(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
	List<MasStoreSupplierNew> mmuVendorList = new ArrayList<MasStoreSupplierNew>();
	List list = new ArrayList();

	if (jsonObject != null) {
		Map<String, List<MasStoreSupplierNew>> mapVendor = md.getAllVendor(jsonObject);
		List totalMatches = new ArrayList();
		if (mapVendor.get("mmuVendorList") != null) {
			mmuVendorList = mapVendor.get("mmuVendorList");
			totalMatches = mapVendor.get("totalMatches");
			for (MasStoreSupplierNew vendor : mmuVendorList) {
				HashMap<String, Object> mapObj = new HashMap<String, Object>();

				mapObj.put("supplierId", vendor.getSupplierId());
				mapObj.put("supplierCode", vendor.getSupplierCode() !=null ?  vendor.getSupplierCode() : "");
				mapObj.put("supplierName", vendor.getSupplierName() != null ? vendor.getSupplierName() : "");
				mapObj.put("supplierTypeName", vendor.getMasStoreSupplierType() != null ||
						                       vendor.getMasStoreSupplierType().getSupplierTypeName() !=null ? 
						                       vendor.getMasStoreSupplierType().getSupplierTypeName() : "");
				mapObj.put("status", vendor.getStatus() !=null ? vendor.getStatus() :"" );				
				mapObj.put("supplierTypeId",  vendor.getMasStoreSupplierType() != null ? vendor.getMasStoreSupplierType().getSupplierTypeId() : "");				
				mapObj.put("pinNo",  vendor.getPinNo() !=null ? vendor.getPinNo() :"");
				mapObj.put("tinNo",  vendor.getTinNo() !=null ? vendor.getTinNo() : "");
				mapObj.put("licenceNo",  vendor.getLicenceNo() !=null ? vendor.getLicenceNo() : "");
				mapObj.put("address1",  vendor.getAddress1() !=null ? vendor.getAddress1() :"");
				mapObj.put("address2",  vendor.getAddress2() !=null ? vendor.getAddress2() :"");
				mapObj.put("stateId",  vendor.getState() !=null ? vendor.getState().getStateId() :"");
				mapObj.put("stateName",  vendor.getState() !=null ? vendor.getState().getStateName() :"");
				mapObj.put("cityId",  vendor.getCity() !=null ? vendor.getCity().getDistrictId() :"");
				mapObj.put("cityName",  vendor.getCity() !=null ? vendor.getCity().getDistrictName() :"");
				mapObj.put("phoneno",  vendor.getPhoneno() !=null ? vendor.getPhoneno() :"");
				mapObj.put("mobileno", vendor.getMobileno() !=null ? vendor.getMobileno() :"");
				mapObj.put("pinCode", vendor.getPinCode() !=null ? vendor.getPinCode() : "");
				mapObj.put("emailid",  vendor.getEmailid() !=null ? vendor.getEmailid() : "");
				mapObj.put("faxnumber", vendor.getFaxnumber() !=null ? vendor.getFaxnumber() :"");
				mapObj.put("localAddress1", vendor.getLocalAddress1() !=null ? vendor.getLocalAddress1() :"");
				mapObj.put("localAddress2", vendor.getLocalAddress2() !=null ? vendor.getLocalAddress2() :"");
				mapObj.put("localPhoneNo", vendor.getLocalPhoneNo() !=null ? vendor.getLocalPhoneNo() :"");
				mapObj.put("localstateId", vendor.getMasState() !=null ? vendor.getMasState().getStateId() : "");
				mapObj.put("localStateName", vendor.getMasState() !=null ? vendor.getMasState().getStateName() : "" );
				mapObj.put("localdistrictId",  vendor.getLocalCity() !=null ? vendor.getLocalCity().getDistrictId() :"");
				mapObj.put("localdistrictName", vendor.getLocalCity() !=null ? vendor.getLocalCity().getDistrictName() :"");
				mapObj.put("localPinCode", vendor.getLocalPinCode() !=null ? vendor.getLocalPinCode() :"");
				mapObj.put("panNumber",  vendor.getPanNumber() !=null ? vendor.getPanNumber() :"");
								
				list.add(mapObj);
				
			}

			if (list != null && list.size() > 0) {
				json.put("data", list);
				json.put("count", totalMatches.size());
				json.put("msg", "get Record successfully");
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", totalMatches.size());
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}

		 } 

	  } else {
		json.put("msg", "No Record Found");
		json.put("status", 0);
	 }
	return json.toString();
  }

	@Override
	public String addVendor(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		MasStoreSupplierNew masStoreSupplier = new MasStoreSupplierNew();		
		if (jsonObject !=null) {		
			
				masStoreSupplier.setSupplierCode(jsonObject.get("supplierCode").toString());
				masStoreSupplier.setSupplierName(jsonObject.get("supplierName").toString());
				masStoreSupplier.setPinNo(jsonObject.get("pinNo").toString());
				masStoreSupplier.setTinNo(jsonObject.get("tinNo").toString());
				masStoreSupplier.setLicenceNo(jsonObject.get("licenceNo").toString());
				masStoreSupplier.setPanNumber(jsonObject.get("panNo").toString());
				
				if(jsonObject.get("address1").toString() !=null && !jsonObject.get("address1").toString().equals("")) {
				masStoreSupplier.setAddress1(jsonObject.get("address1").toString());
				}
				if(jsonObject.get("address2").toString().toUpperCase() !=null && !jsonObject.get("address2").toString().equals(""))
				{
					masStoreSupplier.setAddress2(jsonObject.get("address2").toString());
				
				}
				if(jsonObject.get("phoneno").toString() !=null && !jsonObject.get("phoneno").toString().equals("")) {
				masStoreSupplier.setPhoneno(jsonObject.get("phoneno").toString());
				}
				if( jsonObject.get("mobileno").toString() !=null && !jsonObject.get("mobileno").toString().equals("") ) {
				masStoreSupplier.setMobileno(jsonObject.get("mobileno").toString());
				}
				if(!jsonObject.get("pinCode").toString().equals("")) {
				Long pinCode = Long.parseLong(jsonObject.get("pinCode").toString());
				if(jsonObject.get("pinCode") != null || jsonObject.get("pinCode").equals(0) )
				{
					masStoreSupplier.setPinCode(pinCode);
				}
				}
				if(jsonObject.get("emailid").toString()!=null && !jsonObject.get("emailid").toString().isEmpty())
				{
				masStoreSupplier.setEmailid(jsonObject.get("emailid").toString());
				}
				if(jsonObject.get("faxnumber").toString() !=null && !jsonObject.get("faxnumber").toString().isEmpty()) {
				masStoreSupplier.setFaxnumber(jsonObject.get("faxnumber").toString());
				}
				//masStoreSupplier.setUrl(jsonObject.get("url").toString().toUpperCase()); 
				if(jsonObject.get("localAddress1") !=null  && !jsonObject.get("localAddress1").toString().isEmpty()) {
				masStoreSupplier.setLocalAddress1(jsonObject.get("localAddress1").toString());
				}
				if(jsonObject.get("localAddress2").toString() !=null && !jsonObject.get("localAddress2").toString().isEmpty()) {
				masStoreSupplier.setLocalAddress2(jsonObject.get("localAddress2").toString());
				}
				if(jsonObject.get("localPhoneNo").toString() !=null && !jsonObject.get("localPhoneNo").toString().isEmpty()) {
				masStoreSupplier.setLocalPhoneNo(jsonObject.get("localPhoneNo").toString());
				//masStoreSupplier.setMobileno(jsonObject.get("mobileno").toString());
				}
				if(!jsonObject.get("localPinCode").toString().equals("")) {
				Long localPinCode = Long.parseLong(jsonObject.get("localPinCode").toString());
				if(jsonObject.get("localPinCode") == null || jsonObject.get("localPinCode").equals(0) )
				{
					masStoreSupplier.setLocalPinCode(localPinCode);
				}
				}
				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masStoreSupplier.setLastChgDate(date);
				long userId = Long.parseLong(jsonObject.get("userId").toString());

				Users users = new Users();
				users.setUserId(userId);
				masStoreSupplier.setLastChgBy(users);				
				masStoreSupplier.setStatus("y");
				masStoreSupplier.setSupplierTypeId(Long.parseLong(jsonObject.get("supplierTypeId").toString()));
				MasState state = new MasState();
				if(jsonObject.has("stateId")) {
				if(!jsonObject.get("stateId").toString().equals("") && jsonObject.get("stateId").toString() !=null) {
				Long stateId = Long.parseLong(jsonObject.get("stateId").toString());
				
					state.setStateId(stateId);
					masStoreSupplier.setState(state);	
				
				}
				}
				MasState localState = new MasState();
				if(jsonObject.has("localstateId")) {
				if(!jsonObject.get("localstateId").toString().equals("")) {
				Long localStateId = Long.parseLong(jsonObject.get("localstateId").toString());
				
					localState.setStateId(localStateId);
					masStoreSupplier.setMasState(localState);	
				
				}
				}
				MasDistrict city = new MasDistrict();
				if(jsonObject.has("districtId")) {
				if(!jsonObject.get("districtId").toString().equals("")) {
				Long cityId = Long.parseLong(jsonObject.get("districtId").toString());
				if(jsonObject.get("districtId") == null || jsonObject.get("districtId").equals(0)) {
					city.setDistrictId(cityId);
					masStoreSupplier.setCity(city);	
				}
				}
				}
				MasDistrict localdistrictName = new MasDistrict();
				if(jsonObject.has("localdistrictId")) {
				if(!jsonObject.get("localdistrictId").toString().equals("")) {
				Long localCityId = Long.parseLong(jsonObject.get("localdistrictId").toString());
				
					localdistrictName.setDistrictId(localCityId);
					masStoreSupplier.setLocalCity(localdistrictName);	
				
				}
				}
				List<MasStoreSupplierNew> masStoreSupplier1 = md.validateMasStoreSupplier(masStoreSupplier.getSupplierCode().toString(),
						masStoreSupplier.getSupplierName().toString());
				
					if (masStoreSupplier1 != null && !masStoreSupplier1.isEmpty()) {
						
						if (masStoreSupplier1.get(0).getSupplierCode().equalsIgnoreCase(jsonObject.get("supplierCode").toString())) {
							jsonObj.put("status", 2);
							jsonObj.put("msg", "Vendor code already Exists");
							
						}
						
						else if (masStoreSupplier1.get(0).getSupplierName().equalsIgnoreCase(jsonObject.get("supplierName").toString())) {
							jsonObj.put("status", 2);
							jsonObj.put("msg", "Vendor name already Exists");
							
						}
						
						return jsonObj.toString();
					} 
					
					else {
					String masStoreSupplierObj = md.addMasStoreSupplier(masStoreSupplier);
					if (masStoreSupplierObj != null && masStoreSupplierObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (masStoreSupplierObj != null && masStoreSupplierObj.equalsIgnoreCase("500")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} 
				}
			
		     } 

		return jsonObj.toString();

	}


	@Override
	public String updateVendor(HashMap<String, Object> vendorPayload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONObject jsondata = new JSONObject(vendorPayload);
		MasStoreSupplier masStoreSupplier = new MasStoreSupplier();
		masStoreSupplier.setSupplierName(jsondata.get("supplierName").toString());
		
		if (vendorPayload.get("supplierId") != null && !vendorPayload.get("supplierId").toString().equalsIgnoreCase("")) {
			
			
			String vendorUpdate = md.updateStoreSupplier(jsondata);			
			if (vendorUpdate != null && vendorUpdate.equalsIgnoreCase("200")) {
				json.put("vendorUpdate", vendorUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}
				
		} else {
			json.put("msg", "Supplier Id not found");
		}
		return json.toString();
	}

	@Override
	public String updateVendorStatus(HashMap<String, Object> vendor, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (vendor.get("supplierCode") != null && !vendor.get("supplierCode").toString().equalsIgnoreCase("")) {
				String masStoreSupplier = md.updateStoreSupplierStatus(Long.parseLong(vendor.get("supplierId").toString()),
						vendor.get("supplierCode").toString(), vendor.get("status").toString(),
						Long.parseLong(vendor.get("userId").toString()));
				if (masStoreSupplier != null && masStoreSupplier.equalsIgnoreCase("200")) {
					json.put("masStoreSupplier", masStoreSupplier);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
		} else {
			return "{\"status\":\"0\",\"msg\":\"supplier id is not available !!!\"}";
		}

		return json.toString();
	}
	
	@Override
	public String getStateList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasState> stateList = md.getStateList();
		if (stateList != null && stateList.size() > 0) {

			jsonObj.put("data", stateList);
			jsonObj.put("count", stateList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", stateList);
			jsonObj.put("count", stateList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();

	}
	
	@Override
	public String getDistrictList(HashMap<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsondata=new JSONObject(payload);		
		Map<String,List<MasDistrict>> map = md.getDistrictList(jsondata);
		List<MasDistrict> cityList=map.get("cityList");
		List<MasDistrict> localcityList=map.get("localcityList");
		if ((cityList != null && cityList.size() > 0) || (localcityList !=null && localcityList.size()>0)) {

			jsonObj.put("data", cityList);
			jsonObj.put("ldata", localcityList);
			jsonObj.put("count", cityList.size());
			jsonObj.put("lcount", localcityList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", cityList);
			jsonObj.put("count", cityList.size());
			jsonObj.put("ldata", localcityList);
			jsonObj.put("lcount", localcityList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();

	}
	@Override
	public String getDistrictListById(HashMap<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsondata=new JSONObject(payload);		
		List<MasDistrict> listMasDistrict= md.getDistrictListById(jsondata);	
		
		if (listMasDistrict != null && listMasDistrict.size() > 0) {

			jsonObj.put("data", listMasDistrict);			
			jsonObj.put("count", listMasDistrict.size());			
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", listMasDistrict);
			jsonObj.put("count", listMasDistrict.size());			
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();

	}
	
	/***************************************
	 *  Sample Container Master
	 ***********************************************************************/
	
	
	@Override
	public String addSampleContainer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata != null) {			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());			
			DgMasCollection collection = new DgMasCollection();			
			collection.setCollectionCode(jsondata.get("collectionCode").toString().toUpperCase());
			collection.setCollectionName(jsondata.get("collectionName").toString().toUpperCase());			
			collection.setStatus("Y");
			collection.setLastChgBy(userId);
			collection.setLastChgDate(date);
			String validList = md.validateSampleContainer(jsondata.get("collectionCode").toString().toUpperCase(),
					jsondata.get("collectionName").toString().toUpperCase());
			if (validList != null && validList.equalsIgnoreCase("codeExists") ) {
				
					json.put("status", 2);
					json.put("msg", "Sample Code already Exists");
				}
				else if (validList != null && validList.equalsIgnoreCase("nameExists") ) {

					json.put("status", 2);
					json.put("msg", "Sample Name already Exists");
				}

			 else {
				String addSampleObj = md.addSampleContainer(collection);
				if (addSampleObj != null && addSampleObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	
	

	@Override
	public String getAllSampleContainer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgMasCollection> sampleList = new ArrayList<DgMasCollection>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<DgMasCollection>> mapCollection = md.getAllSampleContainer(jsondata);
			List totalMatches = new ArrayList();
			if (mapCollection.get("sampleContainerList") != null) {
				sampleList = mapCollection.get("sampleContainerList");
				totalMatches = mapCollection.get("totalMatches");
				for (DgMasCollection dgmas : sampleList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dgmas != null) {
						mapObj.put("collectionId", dgmas.getCollectionId());
						mapObj.put("collectionCode", dgmas.getCollectionCode());
						mapObj.put("collectionName", dgmas.getCollectionName());
						mapObj.put("status", dgmas.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	
	
	@Override
	public String updateSampleContainerStatus(HashMap<String, Object> collection, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (collection.get("collectionId").toString() != null && !collection.get("status").toString().equalsIgnoreCase("")) {			
				String collectionStatus = md.updateSampleContainerStatus(Long.parseLong(collection.get("collectionId").toString()),
						collection.get("status").toString());

				if (collectionStatus != null && collectionStatus.equalsIgnoreCase("200")) {
					
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	
	@Override
	public String updateSampleContainer(HashMap<String, Object> collection, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (collection.get("collectionId") != null
				&& !collection.get("collectionId").toString().equalsIgnoreCase("")) {

			String validList = md.validateSampleContainerName(collection.get("collectionName").toString());
			
				if(validList != null && validList.equalsIgnoreCase("nameExists")) {					
				return "{\"status\":\"2\",\"msg\":\"Collection Name already exists\"}";
				}
			

			String collectionUpdate = md.updateSampleContainer(
					Long.parseLong(collection.get("collectionId").toString()),
					collection.get("collectionCode").toString(),
					collection.get("collectionName").toString()					
					,Long.parseLong(collection.get("userId").toString())
					);
			if (collectionUpdate != null && collectionUpdate.equalsIgnoreCase("success")) {
				json.put("collectionUpdate", collectionUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (collectionUpdate == null && collectionUpdate.equalsIgnoreCase("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

		}

		return json.toString();

	}
	
	/***************************************
	 * Department Type
	 ***********************************************************************/

	@Override
	public String getAllDepartmentType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDepartmentType> departmentTypeList = new ArrayList<MasDepartmentType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDepartmentType>> mapDepartmentType = md.getAllDepartmentType(jsondata);
			List totalMatches = new ArrayList();
			if (mapDepartmentType.get("departmentTypeList") != null) {
				departmentTypeList = mapDepartmentType.get("departmentTypeList");
				totalMatches = mapDepartmentType.get("totalMatches");
				for (MasDepartmentType departmentType : departmentTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (departmentType != null) {
						mapObj.put("departmentTypeId", departmentType.getDepartmentTypeId());
						mapObj.put("departmentTypeCode", departmentType.getDepartmentTypeCode());
						mapObj.put("departmentTypeName", departmentType.getDepartmentTypeName());
						mapObj.put("status", departmentType.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDepartmentType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasDepartmentType masDepartmentType = new MasDepartmentType();

			masDepartmentType.setDepartmentTypeCode(jsondata.get("departmentTypeCode").toString().toUpperCase());
			masDepartmentType.setDepartmentTypeName(jsondata.get("departmentTypeName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users users = new Users();
			users.setUserId(userId);
			masDepartmentType.setUser(users);
			masDepartmentType.setStatus("Y");
			masDepartmentType.setLastChgDate(date);

			List<MasDepartmentType> checkDepartmentTypeList = md.validateDepartmentType(masDepartmentType.getDepartmentTypeCode(),
					masDepartmentType.getDepartmentTypeName());
			if (checkDepartmentTypeList != null && checkDepartmentTypeList.size() > 0) {
				if (checkDepartmentTypeList.get(0).getDepartmentTypeCode().equalsIgnoreCase(jsondata.get("departmentTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Department Type Code already Exists");
				}
				if (checkDepartmentTypeList.get(0).getDepartmentTypeName().equalsIgnoreCase(jsondata.get("departmentTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Department Type Name already Exists");
				}

			} else {
				String addDepartmentTypeObj = md.addDepartmentType(masDepartmentType);
				if (addDepartmentTypeObj != null && addDepartmentTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDepartmentType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("departmentTypeCode").toString() != null
					&& !jsonObject.get("departmentTypeCode").toString().trim().equalsIgnoreCase("")) {

				List<MasDepartmentType> msDepartmentTypeList = md.validateDepartmentTypeUpdate(jsonObject.get("departmentTypeCode").toString(),
						jsonObject.get("departmentTypeName").toString());
				if (msDepartmentTypeList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Department Type Name already exists\"}";
				}

				String updateDepartmentType = md.updateDepartmentTypeDetails(
						Long.parseLong(jsonObject.get("departmentTypeId").toString()),
						jsonObject.get("departmentTypeCode").toString(), jsonObject.get("departmentTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));
				
				if (updateDepartmentType != null && updateDepartmentType.equalsIgnoreCase("200")) {
					json.put("updateDepartmentType", updateDepartmentType);
					json.put("msg", "Successfully Updated.");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDepartmentTypeStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("departmentTypeCode").toString() != null
					&& !jsonObject.get("departmentTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasDepartmentType mDepartmentType = md.checkDepartmentType(jsonObject.get("departmentTypeCode").toString());

				if (mDepartmentType != null) {
					String departmentTypeStatus = md.updateDepartmentTypeStatus(Long.parseLong(jsonObject.get("departmentTypeId").toString()),
							jsonObject.get("departmentTypeCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (departmentTypeStatus != null && departmentTypeStatus.equalsIgnoreCase("200")) {
						json.put("departmentTypeStatus", departmentTypeStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}
	
	/***************************************
	 * Investigation UOM Master
	 ***********************************************************************/
	
	
	@Override
	public String addInvestigationUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata != null) {			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			MasUOM dguom=new MasUOM();
			dguom.setUOMCode(jsondata.get("uomCode").toString());
			dguom.setUOMName(jsondata.get("uomName").toString());			
			dguom.setUOMStatus("Y");
			Users users=new Users();
			users.setUserId(userId);
			dguom.setUser(users);
			dguom.setLastChgDate(date);
			
			List<MasUOM> validList = md.validateInvestigationUOM(dguom.getUOMCode(),
					dguom.getUOMName());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getUOMCode().equalsIgnoreCase(jsondata.get("uomCode").toString())) {

					json.put("status", 2);
					json.put("msg", "UOM Code already Exists");
				}
				if (validList.get(0).getUOMName().equalsIgnoreCase(jsondata.get("uomName").toString())) {

					json.put("status", 2);
					json.put("msg", "UOM Name already Exists");
				}

			}else {
				String addSampleObj = md.addInvestigationUOM(dguom);
				if (addSampleObj != null && addSampleObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	
	@Override
	public String getAllInvestigationUOM(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasUOM> uomList = new ArrayList<MasUOM>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUOM>> mapCollection = md.getAllInvestigationUOM(jsondata);
			List totalMatches = new ArrayList();
			if (mapCollection.get("uomList") != null) {
				uomList = mapCollection.get("uomList");
				totalMatches = mapCollection.get("totalMatches");
				for (MasUOM dguom : uomList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dguom != null) {
						mapObj.put("uomId",dguom.getUOMId());
						mapObj.put("uomCode",dguom.getUOMCode());
						mapObj.put("uomName", dguom.getUOMName());
						mapObj.put("status", dguom.getUOMStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String updateInvestigationUOMStatus(HashMap<String, Object> uom, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (uom.get("uomId").toString() != null && !uom.get("status").toString().equalsIgnoreCase("")) {			
				String uomStatus = md.updateInvestigationUOMStatus(Long.parseLong(uom.get("uomId").toString()),
						uom.get("status").toString());

				if (uomStatus != null && uomStatus.equalsIgnoreCase("200")) {
					
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateInvestigationUOM(HashMap<String, Object> uom, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (uom.get("uomId") != null
				&& !uom.get("uomId").toString().equalsIgnoreCase("")) {

			List<MasUOM> validList = md.validateInvestigationUOMName(uom.get("uomName").toString());
			
				if(validList != null && validList.size() > 0) {					
				return "{\"status\":\"2\",\"msg\":\"UOM Name already exists\"}";
				}
			
				else
				{
			String uomUpdate = md.updateInvestigationUOM(
					Long.parseLong(uom.get("uomId").toString()),
					uom.get("uomCode").toString(),
					uom.get("uomName").toString()					
					,Long.parseLong(uom.get("userId").toString())
					);
			if (uomUpdate != null && uomUpdate.equalsIgnoreCase("success")) {
				json.put("uomUpdate", uomUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (uomUpdate == null && uomUpdate.equals("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}
				}
		}

		return json.toString();

	}
	
	/**************************************
	 * Investigation Master
	 **************************************************/
	
	@Override
	public String getAllMainChargeList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasMainChargecode> mcList = md.getAllMainChargeList();
		if (mcList != null && mcList.size() > 0) {

			jsonObj.put("data", mcList);
			jsonObj.put("count", mcList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", mcList);
			jsonObj.put("count", mcList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	@Override
	public String getAllModalityList(JSONObject json, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasSubChargecode> scList = md.getAllModalityList(json);
		if (scList != null && scList.size() > 0) {

			jsonObj.put("data", scList);
			jsonObj.put("count", scList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", scList);
			jsonObj.put("count", scList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	@Override
	public String getAllSampleList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasSample> scList = md.getAllSampleList();
		if (scList != null && scList.size() > 0) {

			jsonObj.put("data", scList);
			jsonObj.put("count", scList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", scList);
			jsonObj.put("count", scList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	@Override
	public String getAllCollectionList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<DgMasCollection> cList = md.getAllCollectionList();
		if (cList != null && cList.size() > 0) {

			jsonObj.put("data", cList);
			jsonObj.put("count", cList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", cList);
			jsonObj.put("count", cList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	@Override
	public String getAllUOMList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasUOM> uomList = md.getAllUOMList();
		if (uomList != null && uomList.size() > 0) {

			jsonObj.put("data", uomList);
			jsonObj.put("count", uomList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", uomList);
			jsonObj.put("count", uomList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	
	@Override
	public String addInvestigation(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata != null) {			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			DgMasInvestigation dgmas=new DgMasInvestigation();
			
			dgmas.setInvestigationName(jsondata.get("investigationName").toString());
			dgmas.setMainChargecodeId(Long.parseLong(jsondata.get("departmentId").toString()));
			dgmas.setSubChargecodeId(Long.parseLong(jsondata.get("modalityId").toString()));
			
			if(jsondata.get("investigationType").toString().equalsIgnoreCase("Lab")) {
			 dgmas.setSampleId(Long.parseLong(jsondata.get("sampleId").toString()));
			 dgmas.setCollectionId(Long.parseLong(jsondata.get("collectionId").toString()));
			 dgmas.setUomId(Long.parseLong(jsondata.get("uomId").toString()));
			 dgmas.setMinNormalValue(jsondata.get("minValue").toString());
			 dgmas.setMaxNormalValue(jsondata.get("maxValue").toString());
			 dgmas.setLoincCode(jsondata.get("loincCode").toString());
			 if(null!=jsondata.get("pandemicFlag")) 
			 {
				 dgmas.setPandemicFlag("Y");
				 dgmas.setPandemicCases(Long.parseLong(jsondata.get("pandemicFlag").toString()));
			 }
				 
				 
			}			
			dgmas.setInvestigationType(jsondata.get("resultType").toString());
			if(jsondata.has("flag")) {
			dgmas.setFlag(jsondata.get("flag").toString());
			}
			dgmas.setConfidential(jsondata.get("confidential").toString());			
			dgmas.setStatus("y");
			dgmas.setLastChgBy(userId);
			dgmas.setLastChgDate(date);	
			
			String validList = md.validateInvestigationName(jsondata.get("investigationName").toString());
			if (validList != null && validList.equalsIgnoreCase("nameExists") ) {
				
					json.put("status", 2);
					json.put("msg", "Investigation Name already Exists");
				}				

			 else {
				String addInvObj = md.addInvestigation(dgmas);
				if (addInvObj != null && addInvObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	@Override
	public String getAllInvestigationDetails(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgMasInvestigation> invList = new ArrayList<DgMasInvestigation>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<DgMasInvestigation>> mapInvestigation = md.getAllInvestigationDetails(jsondata);
			List totalMatches = new ArrayList();
			if (mapInvestigation.get("invList") != null) {
				invList = mapInvestigation.get("invList");
				totalMatches = mapInvestigation.get("totalMatches");
				for (DgMasInvestigation dgmas : invList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dgmas != null) {
						mapObj.put("investigationId",dgmas.getInvestigationId());
						mapObj.put("investigationName",dgmas.getInvestigationName());
						mapObj.put("deptId", dgmas.getMasMainChargecode() !=null ? dgmas.getMasMainChargecode().getMainChargecodeId() :"");
						mapObj.put("deptName", dgmas.getMasMainChargecode() !=null ?dgmas.getMasMainChargecode().getMainChargecodeName() :"");
						mapObj.put("modalityId", dgmas.getMasSubChargecode() !=null ? dgmas.getMasSubChargecode().getSubChargecodeId() :"");
						mapObj.put("modalityName", dgmas.getMasSubChargecode()!=null ? dgmas.getMasSubChargecode().getSubChargecodeName():"");
						mapObj.put("sampleId", dgmas.getMasSample() !=null ? dgmas.getMasSample().getSampleId() : "");
						mapObj.put("sampleName", dgmas.getMasSample() !=null ? dgmas.getMasSample().getSampleDescription() :"");
						mapObj.put("collectionId", dgmas.getDgMasCollection() !=null ? dgmas.getDgMasCollection().getCollectionId() :"");						
						mapObj.put("collectionName", dgmas.getDgMasCollection() !=null ? dgmas.getDgMasCollection().getCollectionName() :"");
						mapObj.put("uomId", dgmas.getMasUOM() !=null ?  dgmas.getMasUOM().getUOMId() :"");
						mapObj.put("uomName", dgmas.getMasUOM() !=null ? dgmas.getMasUOM().getUOMName():"");	
						mapObj.put("result", dgmas.getInvestigationType() !=null ? dgmas.getInvestigationType() : "");
						mapObj.put("minval", dgmas.getMinNormalValue() !=null ? dgmas.getMinNormalValue() :"");
						mapObj.put("maxval", dgmas.getMaxNormalValue() !=null ? dgmas.getMaxNormalValue() :"");
						mapObj.put("loincCode", dgmas.getLoincCode() !=null ? dgmas.getLoincCode() :"");
						mapObj.put("confidential", dgmas.getConfidential() !=null ? dgmas.getConfidential() :"n");
						mapObj.put("status", dgmas.getStatus());
						mapObj.put("flag", dgmas.getFlag() !=null ? dgmas.getFlag() :"");
						mapObj.put("pandemicFlag", dgmas.getPandemicFlag() !=null ? dgmas.getPandemicFlag() :"N");
						mapObj.put("pandemicCases", dgmas.getPandemicCases() !=null ? dgmas.getPandemicCases() :"");
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} 
		return json.toString();
	}
	
	@Override
	public String updateInvestigationStatus(HashMap<String, Object> inv, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (inv.get("investigationId").toString() != null && !inv.get("status").toString().equalsIgnoreCase("")) {			
				String invStatus = md.updateInvestigationStatus(Long.parseLong(inv.get("investigationId").toString()),
						inv.get("status").toString());

				if (invStatus != null && invStatus.equalsIgnoreCase("200")) {
					json.put("invStatus", invStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateInvestigation(HashMap<String, Object> inv, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsondata = new JSONObject(inv);
		JSONObject json = new JSONObject();
		String invUpdate ="";
		if (jsondata !=null) {

			
			invUpdate = md.updateInvestigation(jsondata);
					
			if (invUpdate != null && invUpdate.equalsIgnoreCase("success")) {
				json.put("invUpdate", invUpdate);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		
	} else {
		json.put("msg", "No Record Found");
		json.put("status", 0);
	}

	return json.toString();
}
	
	/**************************************
	 * Sub Investigation Master
	 **************************************************/
	@Override
	public String getAllSubInvestigationDetails(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgSubMasInvestigation> subinvList = new ArrayList<DgSubMasInvestigation>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<DgSubMasInvestigation>> mapSubInvestigation = md.getAllSubInvestigationDetails(jsondata);
			
			if (mapSubInvestigation.get("subinvList") != null) {
				subinvList = mapSubInvestigation.get("subinvList");
				
				for (DgSubMasInvestigation sdgmas : subinvList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (sdgmas != null) {
						mapObj.put("subInvId",sdgmas.getSubInvestigationId());
						mapObj.put("printOrder",sdgmas.getOrderNo() !=null ? sdgmas.getOrderNo() :"");
						mapObj.put("modalityId",sdgmas.getMasSubChargecode() !=null ?  sdgmas.getMasSubChargecode() .getSubChargecodeId() :"");
						mapObj.put("modalityName",sdgmas.getMasSubChargecode() !=null ?  sdgmas.getMasSubChargecode().getSubChargecodeName() :"");
						mapObj.put("subtestId", sdgmas.getSubInvestigationId());
						mapObj.put("subtestCode",sdgmas.getSubInvestigationCode() !=null ?  sdgmas.getSubInvestigationCode() :"");
						mapObj.put("subtestName",sdgmas.getSubInvestigationName() !=null ?  sdgmas.getSubInvestigationName() :"");
						mapObj.put("loincCode",sdgmas.getLoincCode() !=null ? sdgmas.getLoincCode() :"");
						mapObj.put("uomId",sdgmas.getMasUOM() !=null ? sdgmas.getMasUOM().getUOMId() :"");
						mapObj.put("uomName",sdgmas.getMasUOM() !=null ? sdgmas.getMasUOM().getUOMName() :"");						
						mapObj.put("resultType",sdgmas.getResultType() !=null ? sdgmas.getResultType() :"");
						mapObj.put("cmpType",sdgmas.getComparisonType() !=null ? sdgmas.getComparisonType() :"");
						mapObj.put("status",sdgmas.getStatus() !=null ? sdgmas.getStatus() :"");
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", list.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	
	@Override
	public String updateSubInvestigation(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		
		if (jsondata != null) {
			
			
			String subinvObj = md.updateSubInvestigation(jsondata);

			if (subinvObj != null && subinvObj.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "SubInvestigation updated Successfully");
				} else {
					jsonObj.put("status", 0);
					jsonObj.put("msg", "Record Not Added");
				}
			//}
		}

		else {
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();
	}
	
	
	@Override
	public String deleteSunbInvestigationById(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
				

		JSONObject jsonObj = new JSONObject();
        
		if (jsondata != null) {
			Long subInvId=Long.parseLong(jsondata.get("subInvId").toString());
			String result = md.deleteSunbInvestigationById(subInvId);
			if (result != null && result.equalsIgnoreCase("success")) {				
				jsonObj.put("msg", "SubInvestigation Deleted Successfully");
				jsonObj.put("status", 1);

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "SubInvestigation not Deleted Successfully ");
			}
		}

		return jsonObj.toString();
	}
	
	@Override
	public String deleteFixedValueById(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
				

		JSONObject jsonObj = new JSONObject();
        
		if (jsondata != null) {
			Long fixedValueId=Long.parseLong(jsondata.get("fixedValueId").toString());
			String result = md.deleteFixedValueById(fixedValueId);
			if (result != null && result.equalsIgnoreCase("success")) {				
				jsonObj.put("msg", "Fixed Value Deleted Successfully");
				jsonObj.put("status", 1);

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Fixed Value not Deleted");
			}
		}

		return jsonObj.toString();
	}
	
	/********************************************DepartmentAndDoctorMapping***********************************************/
	
	@Override
	public String addDepartmentAndDoctorMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
		if (json != null) {
			
			MasDoctorMapping masDoctorMapping=new MasDoctorMapping();
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long hospitalId = Long.parseLong(jsondata.get("hospitalId").toString());
			long doctorId = Long.parseLong(jsondata.get("doctorId").toString());
			long departmentId = Long.parseLong(jsondata.get("departmentId").toString());
			
			masDoctorMapping.setStatus("Y");
			Users users=new Users();
			users.setUserId(userId);
			masDoctorMapping.setLastChgBy(users);
			masDoctorMapping.setLastChgDate(date);
			
			MasHospital location=new MasHospital();
			location.setHospitalId(hospitalId);
			masDoctorMapping.setLocationId(hospitalId);
			
			Users users2 = new Users();
			users2.setUserId(doctorId);
			masDoctorMapping.setDoctorId(users2);
			
			MasDepartment masDepartment = new MasDepartment();
			masDepartment.setDepartmentId(departmentId);
			masDoctorMapping.setMasDepartment(masDepartment);
			
			List<MasDoctorMapping> masDoctorMapping1 = md.validateMasDoctorMapping(
					Long.parseLong(jsondata.get("doctorId").toString()),
							Long.parseLong(jsondata.get("departmentId").toString()));
				
					if (masDoctorMapping1 != null && masDoctorMapping1.size() > 0) {					

						json.put("status", 2);
						json.put("msg", "Doctor Mapping already exists against the selected Doctor Name and selected Department Name");
					
				}
				
					else {
						
						String masDNDObj = md.addMasDoctorMapping(masDoctorMapping);
						if (masDNDObj != null && masDNDObj.equalsIgnoreCase("200")) {
							json.put("status", 1);
							json.put("msg", "Record Added Successfully");
						} else {
							json.put("status", 0);
							json.put("msg", "Record Not Added");
						}
					}
					

					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return json.toString();
		}
	
	
	@Override
	public String getAllDepartmentAndDoctorMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDoctorMapping> dndList = new ArrayList<MasDoctorMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDoctorMapping>> mapDND = md.getAllDoctorMapping(jsondata);
			List totalMatches = new ArrayList();
			if (mapDND.get("dndList") != null) {
				dndList = mapDND.get("dndList");
				totalMatches = mapDND.get("totalMatches");
				for (MasDoctorMapping masDoctorMapping : dndList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (masDoctorMapping != null) {
						mapObj.put("doctorMapId",masDoctorMapping.getDoctorMapId());
						//mapObj.put("firstName",masDoctorMapping.getDoctorId().getFirstName());
						mapObj.put("doctorId",masDoctorMapping.getDoctorId().getUserId());
						mapObj.put("departmentName",masDoctorMapping.getMasDepartment().getDepartmentName());
						mapObj.put("departmentId",masDoctorMapping.getMasDepartment().getDepartmentId());
						  mapObj.put("status", masDoctorMapping.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String updateDepartmentAndDoctorMappingStatus(HashMap<String, Object> dnd, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dnd.get("doctorMapId").toString() != null && !dnd.get("status").toString().equalsIgnoreCase("")) {	
				String status = md.updateMasDoctorMappingStatus(Long.parseLong(dnd.get("doctorMapId").toString()),
						dnd.get("status").toString(),Long.parseLong(dnd.get("userId").toString()),
						Long.parseLong(dnd.get("hospitalId").toString()));

				if (status != null && status.equalsIgnoreCase("200")) {
					
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
					
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"doctorMapId is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateDepartmentAndDoctorMapping(HashMap<String, Object> dnd, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dnd.get("doctorMapId") != null
				&& !dnd.get("doctorMapId").toString().equalsIgnoreCase("")) {

			String update = md.updateMasDoctorMapping(
					Long.parseLong(dnd.get("doctorMapId").toString()),
					Long.parseLong(dnd.get("departmentId").toString()),
					Long.parseLong(dnd.get("doctorId").toString()),					
					Long.parseLong(dnd.get("userId").toString()),
					Long.parseLong(dnd.get("hospitalId").toString())
					);
			if (update != null && update.equalsIgnoreCase("200")) {
				json.put("update", update);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (update == null && update.equals("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

		}

		return json.toString();

	}

	@Override
	public String getAllEmployee(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasEmployee> empList = new ArrayList<MasEmployee>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasEmployee>> mapEmployee = md.getAllEmployee(jsondata);
			List totalMatches = new ArrayList();
			if (mapEmployee.get("empList") != null) {
				empList = mapEmployee.get("empList");
				totalMatches = mapEmployee.get("totalMatches");
				for (MasEmployee masEmployee : empList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					String rankName="";
					String unitName="";
					long unitId=0;
					if (masEmployee != null) {
						mapObj.put("employeeId",masEmployee.getEmployeeId());
						mapObj.put("employeeName",masEmployee.getEmployeeName());
						mapObj.put("serviceNo",masEmployee.getServiceNo());
						//mapObj.put("unitId", masEmployee.getMasUnit());
						if(empList.get(0).getMasRank()!=null ) {
							MasRank masRank=md.getRankByRankCode((empList.get(0).getMasRank()));
							if(masRank!=null && StringUtils.isNotEmpty(masRank.getRankName()))
								rankName=masRank.getRankName();
						}
						mapObj.put("rankName",rankName);
						
						if(empList.get(0).getMasUnit()!=null ) {
							MasUnit masUnit=md.getMasUnitByUnitCode((empList.get(0).getMasUnit()));
							if(masUnit!=null && StringUtils.isNotEmpty(masUnit.getUnitName()))
							unitName=masUnit.getUnitName();
						}
						
						mapObj.put("unitName",unitName);
						mapObj.put("adId",masEmployee.getAdId());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String getAllUnitList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasUnit> unitList = md.getAllUnitList();
		if (unitList != null && unitList.size() > 0) {

			jsonObj.put("data", unitList);
			jsonObj.put("count", unitList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", unitList);
			jsonObj.put("count", unitList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}
	
	
	/*****************************Fixed Value Master*********************************/
	
	@Override
	public String updateFixedValue(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();	
		if (jsondata != null) {

			String subinvObj = md.updateFixedValue(jsondata);

			if (subinvObj != null && subinvObj.equalsIgnoreCase("success")) {
				json.put("status", 1);
				json.put("msg", "Fixed Value updated Successfully");

			} else {
				json.put("status", 0);
				json.put("msg", "Fixed Value not updated ");
			}
		}

		return json.toString();
	}
	
	@Override
	public String getAllFixeValueById(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgFixedValue> fvlist = new ArrayList<DgFixedValue>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<DgFixedValue>> mapFixedValue = md.getAllFixeValueById(jsondata);
			
			if (mapFixedValue.get("fixedValueList") != null) {
				fvlist = mapFixedValue.get("fixedValueList");
				
				for (DgFixedValue dgfv : fvlist) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dgfv != null) {
						mapObj.put("fixedId", dgfv.getFixedId());
						mapObj.put("fixedValueName", dgfv.getFixedValue());
						list.add(mapObj);
					}
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", list.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String validateFixedValue(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata != null) {
			String validStatus = md.validateFixedValue(jsondata);			
			
				if (validStatus != null && validStatus.equals("fvFound")) {
					
					json.put("msg", "Fixed value already exists");
					json.put("status", 1);
				} 

			} 

		
		return json.toString();
	}
	
	
	
/*****************************Normal Value Master*********************************/
	
	@Override
	public String updateNormalValue(HashMap<String, Object> jsondata, HttpServletRequest request, HttpServletResponse response) {
		
		JSONObject jsonObj = new JSONObject();

		if (jsondata != null) {

			String subinvObj = md.updateNormalValue(jsondata);

			if (subinvObj != null && subinvObj.equalsIgnoreCase("success")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Normal Value updated Successfully");

			} else {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Normal Value not updated ");
			}
		}

		return jsonObj.toString();
	}
	
	@Override
	public String getAllNormalValueById(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<DgNormalValue> nvlist = new ArrayList<DgNormalValue>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<DgNormalValue>> mapNormalValue = md.getAllNormalValueById(jsondata);
			
			if (mapNormalValue.get("normalValueList") != null) {
				nvlist = mapNormalValue.get("normalValueList");
				
				for (DgNormalValue dgnv : nvlist) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dgnv != null) {
						mapObj.put("normalValueId",dgnv.getNormalId());
						mapObj.put("sexId", dgnv.getSex());
						mapObj.put("fromAge",dgnv.getFromAge() !=null ? dgnv.getFromAge() :"");
						mapObj.put("toAge",dgnv.getToAge() !=null ? dgnv.getToAge() :"");
						mapObj.put("minNormalValue",dgnv.getMinNormalValue() !=null ? dgnv.getMinNormalValue() :"");
						mapObj.put("maxNormalValue",dgnv.getMaxNormalValue() !=null ? dgnv.getMaxNormalValue() :"");
						mapObj.put("normalValue",dgnv.getNormalValue() !=null ? dgnv.getNormalValue() :"");
						list.add(mapObj);
					}
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", list.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String validateServiceNo(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		if (jsondata != null) {
			String validServiceNo = md.validateServiceNo(jsondata);			
			
				if (validServiceNo.equals("") && !validServiceNo.equals("svFound")) {
					
					json.put("msg", "Service No. does not exists");
					json.put("status", 1);
				} 

			} 

		
		return json.toString();
	}
	
	/***************************************
	 * MAS Discharge Status
	 ***********************************************************************/

	@Override
	public String getAllDischargeStatus(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDischargeStatus> dischargeStatusList = new ArrayList<MasDischargeStatus>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDischargeStatus>> mapDischargeStatus = md.getAllDischargeStatus(jsondata);
			List totalMatches = new ArrayList();
			if (mapDischargeStatus.get("dischargeStatusList") != null) {
				dischargeStatusList = mapDischargeStatus.get("dischargeStatusList");
				totalMatches = mapDischargeStatus.get("totalMatches");
				for (MasDischargeStatus dischargeStatus : dischargeStatusList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (dischargeStatus != null) {
						mapObj.put("dischargeStatusId", dischargeStatus.getDischargeStatusId());
						mapObj.put("dischargeStatusCode", dischargeStatus.getDischargeStatusCode());
						mapObj.put("dischargeStatusName", dischargeStatus.getDischargeStatusName());
						mapObj.put("status", dischargeStatus.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDischargeStatus(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasDischargeStatus masDischargeStatus = new MasDischargeStatus();

			masDischargeStatus
					.setDischargeStatusCode(jsondata.get("dischargeStatusCode").toString().toUpperCase());
			masDischargeStatus
					.setDischargeStatusName(jsondata.get("dischargeStatusName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masDischargeStatus.setUser(user);
			masDischargeStatus.setStatus("Y");
			masDischargeStatus.setLastChgDate(date);

			List<MasDischargeStatus> checkDischargeStatusList = md.validateDischargeStatus(
					masDischargeStatus.getDischargeStatusCode(), masDischargeStatus.getDischargeStatusName());
			if (checkDischargeStatusList != null && checkDischargeStatusList.size() > 0) {
				if (checkDischargeStatusList.get(0).getDischargeStatusCode()
						.equalsIgnoreCase(jsondata.get("dischargeStatusCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Discharge Status Code already Exists");
				}
				if (checkDischargeStatusList.get(0).getDischargeStatusName()
						.equalsIgnoreCase(jsondata.get("dischargeStatusName").toString())) {

					json.put("status", 2);
					json.put("msg", "Discharge Status Name already Exists");
				}

			} else {
				String addDischargeStatusObj = md.addDischargeStatus(masDischargeStatus);
				if (addDischargeStatusObj != null && addDischargeStatusObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDischargeStatusDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("dischargeStatusCode").toString() != null
					&& !jsonObject.get("dischargeStatusCode").toString().trim().equalsIgnoreCase("")) {

				List<MasDischargeStatus> msDischargeStatusList = md.validateDischargeStatusUpdate(
						jsonObject.get("dischargeStatusCode").toString(),
						jsonObject.get("dischargeStatusName").toString());
				if (msDischargeStatusList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Discharge Status Name already exists\"}";
				}

				String updateDischargeStatus = md.updateDischargeStatusDetails(
						Long.parseLong(jsonObject.get("dischargeStatusId").toString()),
						jsonObject.get("dischargeStatusCode").toString(),
						jsonObject.get("dischargeStatusName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateDischargeStatus != null && updateDischargeStatus.equalsIgnoreCase("200")) {
					json.put("updateDischargeStatus", updateDischargeStatus);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDischargeStatusStatus(JSONObject DischargeStatus, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (DischargeStatus.get("dischargeStatusCode").toString() != null
					&& !DischargeStatus.get("dischargeStatusCode").toString().trim().equalsIgnoreCase("")) {

				MasDischargeStatus mDischargeStatus = md
						.checkDischargeStatus(DischargeStatus.get("dischargeStatusCode").toString());

				if (mDischargeStatus != null) {
					String dischargeStatusStatus = md.updateDischargeStatusStatus(
							Long.parseLong(DischargeStatus.get("dischargeStatusId").toString()),
							DischargeStatus.get("dischargeStatusCode").toString(),
							DischargeStatus.get("status").toString(),
							Long.parseLong(DischargeStatus.get("userId").toString()));

					if (dischargeStatusStatus != null && dischargeStatusStatus.equalsIgnoreCase("200")) {
						jsonObject.put("dischargeStatusStatus", dischargeStatusStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	/***************************************
	 * MAS Bed Status
	 ***********************************************************************/

	@Override
	public String getAllBedStatus(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasBedStatus> bedStatusList = new ArrayList<MasBedStatus>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasBedStatus>> mapBedStatus = md.getAllBedStatus(jsondata);
			List totalMatches = new ArrayList();
			if (mapBedStatus.get("bedStatusList") != null) {
				bedStatusList = mapBedStatus.get("bedStatusList");
				totalMatches = mapBedStatus.get("totalMatches");
				for (MasBedStatus bedStatus : bedStatusList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (bedStatus != null) {
						mapObj.put("bedStatusId", bedStatus.getBedStatusId());
						mapObj.put("bedStatusCode", bedStatus.getBedStatusCode());
						mapObj.put("bedStatusName", bedStatus.getBedStatusName());
						mapObj.put("status", bedStatus.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addBedStatus(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasBedStatus masBedStatus = new MasBedStatus();

			masBedStatus
					.setBedStatusCode(jsondata.get("bedStatusCode").toString().toUpperCase());
			masBedStatus
					.setBedStatusName(jsondata.get("bedStatusName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masBedStatus.setUser(user);
			masBedStatus.setStatus("Y");
			masBedStatus.setLastChgDate(date);

			List<MasBedStatus> checkBedStatusList = md.validateBedStatus(
					masBedStatus.getBedStatusCode(), masBedStatus.getBedStatusName());
			if (checkBedStatusList != null && checkBedStatusList.size() > 0) {
				if (checkBedStatusList.get(0).getBedStatusCode()
						.equalsIgnoreCase(jsondata.get("bedStatusCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Bed Status Code already Exists");
				}
				if (checkBedStatusList.get(0).getBedStatusName()
						.equalsIgnoreCase(jsondata.get("bedStatusName").toString())) {

					json.put("status", 2);
					json.put("msg", "Bed Status Name already Exists");
				}

			} else {
				String addBedStatusObj = md.addBedStatus(masBedStatus);
				if (addBedStatusObj != null && addBedStatusObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateBedStatusDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("bedStatusCode").toString() != null
					&& !jsonObject.get("bedStatusCode").toString().trim().equalsIgnoreCase("")) {

				List<MasBedStatus> msBedStatusList = md.validateBedStatusUpdate(
						jsonObject.get("bedStatusCode").toString(),
						jsonObject.get("bedStatusName").toString());
				if (msBedStatusList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Bed Status Name already exists\"}";
				}

				String updateBedStatus = md.updateBedStatusDetails(
						Long.parseLong(jsonObject.get("bedStatusId").toString()),
						jsonObject.get("bedStatusCode").toString(),
						jsonObject.get("bedStatusName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateBedStatus != null && updateBedStatus.equalsIgnoreCase("200")) {
					json.put("updateBedStatus", updateBedStatus);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateBedStatusStatus(JSONObject BedStatus, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (BedStatus.get("bedStatusCode").toString() != null
					&& !BedStatus.get("bedStatusCode").toString().trim().equalsIgnoreCase("")) {

				MasBedStatus mBedStatus = md
						.checkBedStatus(BedStatus.get("bedStatusCode").toString());

				if (mBedStatus != null) {
					String bedStatusStatus = md.updateBedStatusStatus(
							Long.parseLong(BedStatus.get("bedStatusId").toString()),
							BedStatus.get("bedStatusCode").toString(),
							BedStatus.get("status").toString(),
							Long.parseLong(BedStatus.get("userId").toString()));

					if (bedStatusStatus != null && bedStatusStatus.equalsIgnoreCase("200")) {
						jsonObject.put("bedStatusStatus", bedStatusStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
/********************************************BedMaster***********************************************/
	
	@Override
	public String addBed(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
		MasBed masBed = new MasBed();
		if (json != null) {
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long hospitalId = Long.parseLong(jsondata.get("hospitalId").toString());
			long departmentId = Long.parseLong(jsondata.get("departmentId").toString());
			
			masBed.setStatus("Y");
			Users users=new Users();
			users.setUserId(userId);
			masBed.setUser(users);
			masBed.setLastChgDate(date);
			
			MasHospital location=new MasHospital();
			location.setHospitalId(hospitalId);
			masBed.setMasHospital(location);
			
			MasDepartment masDepartment = new MasDepartment();
			masDepartment.setDepartmentId(departmentId);
			masBed.setMasDepartment(masDepartment); 
			
			masBed.setBedNo(jsondata.get("bedNo").toString().toUpperCase());
		
			List<MasBed> validBed = md.validateBed(masBed.getBedNo(),
					Long.parseLong(jsondata.get("departmentId").toString()), Long.parseLong(jsondata.get("hospitalId").toString()));
			
			if (validBed != null && validBed.size() > 0) {					
					json.put("status", 2);
					json.put("msg", "Bed already exists against the enter Bed No ,selected Department and Hospital");			
			}
			else {
				
				String savedObject = md.addBed(masBed);
				if (savedObject != null && savedObject.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				}
				else if (savedObject != null && savedObject.equalsIgnoreCase("400")) {
					json.put("status", 0);
					json.put("msg", "Unoccupied Bed Status is not Defined in Bed Status Master");
				}
				else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
		
			
	
	@Override
	public String getAllBed(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasBed> bedList = new ArrayList<MasBed>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasBed>> mapBed = md.getAllBed(jsondata);
			List totalMatches = new ArrayList();
			if (mapBed.get("bedList") != null) {
				bedList = mapBed.get("bedList");
				totalMatches = mapBed.get("totalMatches");
				for (MasBed masBed : bedList) {
					String bedStatusId="";
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (masBed != null) {
						mapObj.put("bedId",masBed.getBedId());
						mapObj.put("bedNo",masBed.getBedNo());
						if(masBed.getMasBedStatus() != null) {
							mapObj.put("bedStatusName",masBed.getMasBedStatus().getBedStatusName() !=null?
									masBed.getMasBedStatus().getBedStatusName():"");
						}
						
						if(masBed.getMasBedStatus() != null) {
							bedStatusId = String.valueOf(masBed.getMasBedStatus().getBedStatusId());
							mapObj.put("bedStatusId", bedStatusId);
						}
						mapObj.put("departmentName",masBed.getMasDepartment() != null
								? masBed.getMasDepartment().getDepartmentName()
								: "");
						mapObj.put("departmentId",masBed.getMasDepartment() != null
								? masBed.getMasDepartment().getDepartmentId(): "0");
						  mapObj.put("status", masBed.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String updateBedStatus(HashMap<String, Object> bed, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (bed.get("bedId").toString() != null && !bed.get("status").toString().equalsIgnoreCase("")) {			
				String status = md.updateBedStatus(Long.parseLong(bed.get("bedId").toString()),
						bed.get("status").toString(),Long.parseLong(bed.get("userId").toString()),
						Long.parseLong(bed.get("hospitalId").toString()));

				if (status != null && status.equalsIgnoreCase("200")) {
					
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"bedId is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateBed(HashMap<String, Object> bed, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (bed.get("bedId") != null
				&& !bed.get("bedId").toString().equalsIgnoreCase("")) {

			String update = md.updateBed(
					Long.parseLong(bed.get("bedId").toString()),
					bed.get("bedNo").toString(),
					Long.parseLong(bed.get("departmentId").toString()),										
					Long.parseLong(bed.get("userId").toString()),
					Long.parseLong(bed.get("hospitalId").toString())
					);
			if (update != null && update.equalsIgnoreCase("200")) {
				json.put("update", update);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else if (update == null && update.equals("")) {
				json.put("msg", "Record Not Updated");
				json.put("status", 0);
			}

		}

		return json.toString();

	}
	
	/***************************************
	 * MAS Speciality
	 ***********************************************************************/

	@Override
	public String getAllSpeciality(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasSpeciality> specialityList = new ArrayList<MasSpeciality>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasSpeciality>> mapSpeciality = md.getAllSpeciality(jsondata);
			List totalMatches = new ArrayList();
			if (mapSpeciality.get("specialityList") != null) {
				specialityList = mapSpeciality.get("specialityList");
				totalMatches = mapSpeciality.get("totalMatches");
				for (MasSpeciality speciality : specialityList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (speciality != null) {
						mapObj.put("specialityId", speciality.getSpecialityId());
						mapObj.put("specialityCode", speciality.getSpecialityCode());
						mapObj.put("specialityName", speciality.getSpecialityName());
						mapObj.put("status", speciality.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addSpeciality(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasSpeciality masSpeciality = new MasSpeciality();

			masSpeciality
					.setSpecialityCode(jsondata.get("specialityCode").toString().toUpperCase());
			masSpeciality
					.setSpecialityName(jsondata.get("specialityName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masSpeciality.setUser(user);
			masSpeciality.setStatus("Y");
			masSpeciality.setLastChgDate(date);

			List<MasSpeciality> checkSpecialityList = md.validateSpeciality(
					masSpeciality.getSpecialityCode(), masSpeciality.getSpecialityName());
			if (checkSpecialityList != null && checkSpecialityList.size() > 0) {
				if (checkSpecialityList.get(0).getSpecialityCode()
						.equalsIgnoreCase(jsondata.get("specialityCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Speciality Code already Exists");
				}
				if (checkSpecialityList.get(0).getSpecialityName()
						.equalsIgnoreCase(jsondata.get("specialityName").toString())) {

					json.put("status", 2);
					json.put("msg", "Speciality Name already Exists");
				}

			} else {
				String addSpecialityObj = md.addSpeciality(masSpeciality);
				if (addSpecialityObj != null && addSpecialityObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateSpecialityDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("specialityCode").toString() != null
					&& !jsonObject.get("specialityCode").toString().trim().equalsIgnoreCase("")) {

				List<MasSpeciality> msSpecialityList = md.validateSpecialityUpdate(
						jsonObject.get("specialityCode").toString(),
						jsonObject.get("specialityName").toString());
				if (msSpecialityList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Speciality Name already exists\"}";
				}

				String updateSpeciality = md.updateSpecialityDetails(
						Long.parseLong(jsonObject.get("specialityId").toString()),
						jsonObject.get("specialityCode").toString(),
						jsonObject.get("specialityName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateSpeciality != null && updateSpeciality.equalsIgnoreCase("200")) {
					json.put("updateSpeciality", updateSpeciality);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateSpecialityStatus(JSONObject speciality, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (speciality.get("specialityCode").toString() != null
					&& !speciality.get("specialityCode").toString().trim().equalsIgnoreCase("")) {

				MasSpeciality mSpeciality = md
						.checkSpeciality(speciality.get("specialityCode").toString());

				if (mSpeciality != null) {
					String specialityStatus = md.updateSpecialityStatus(
							Long.parseLong(speciality.get("specialityId").toString()),
							speciality.get("specialityCode").toString(),
							speciality.get("status").toString(),
							Long.parseLong(speciality.get("userId").toString()));

					if (specialityStatus != null && specialityStatus.equalsIgnoreCase("200")) {
						jsonObject.put("specialityStatus", specialityStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	/***************************************
	 * MAS AdmissionType
	 ***********************************************************************/

	@Override
	public String getAllAdmissionType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasAdmissionType> admissionTypeList = new ArrayList<MasAdmissionType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasAdmissionType>> mapAdmissionType = md.getAllAdmissionType(jsondata);
			List totalMatches = new ArrayList();
			if (mapAdmissionType.get("admissionTypeList") != null) {
				admissionTypeList = mapAdmissionType.get("admissionTypeList");
				totalMatches = mapAdmissionType.get("totalMatches");
				for (MasAdmissionType admissionType : admissionTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (admissionType != null) {
						mapObj.put("admissionTypeId", admissionType.getAdmissionTypeId());
						mapObj.put("admissionTypeCode", admissionType.getAdmissionTypeCode());
						mapObj.put("admissionTypeName", admissionType.getAdmissionTypeName());
						mapObj.put("status", admissionType.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addAdmissionType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasAdmissionType masAdmissionType = new MasAdmissionType();

			masAdmissionType
					.setAdmissionTypeCode(jsondata.get("admissionTypeCode").toString().toUpperCase());
			masAdmissionType
					.setAdmissionTypeName(jsondata.get("admissionTypeName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masAdmissionType.setUser(user);
			masAdmissionType.setStatus("Y");
			masAdmissionType.setLastChgDate(date);

			List<MasAdmissionType> checkAdmissionTypeList = md.validateAdmissionType(
					masAdmissionType.getAdmissionTypeCode(), masAdmissionType.getAdmissionTypeName());
			if (checkAdmissionTypeList != null && checkAdmissionTypeList.size() > 0) {
				if (checkAdmissionTypeList.get(0).getAdmissionTypeCode()
						.equalsIgnoreCase(jsondata.get("admissionTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Admission Type Code already Exists");
				}
				if (checkAdmissionTypeList.get(0).getAdmissionTypeName()
						.equalsIgnoreCase(jsondata.get("admissionTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Admission Type Name already Exists");
				}

			} else {
				String addAdmissionTypeObj = md.addAdmissionType(masAdmissionType);
				if (addAdmissionTypeObj != null && addAdmissionTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateAdmissionTypeDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("admissionTypeCode").toString() != null
					&& !jsonObject.get("admissionTypeCode").toString().trim().equalsIgnoreCase("")) {

				List<MasAdmissionType> msAdmissionTypeList = md.validateAdmissionTypeUpdate(
						jsonObject.get("admissionTypeCode").toString(),
						jsonObject.get("admissionTypeName").toString());
				if (msAdmissionTypeList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Admission Type Name already exists\"}";
				}

				String updateAdmissionType = md.updateAdmissionTypeDetails(
						Long.parseLong(jsonObject.get("admissionTypeId").toString()),
						jsonObject.get("admissionTypeCode").toString(),
						jsonObject.get("admissionTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateAdmissionType != null && updateAdmissionType.equalsIgnoreCase("200")) {
					json.put("updateAdmissionType", updateAdmissionType);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateAdmissionTypeStatus(JSONObject admissionType, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (admissionType.get("admissionTypeCode").toString() != null
					&& !admissionType.get("admissionTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasAdmissionType mAdmissionType = md
						.checkAdmissionType(admissionType.get("admissionTypeCode").toString());

				if (mAdmissionType != null) {
					String admissionTypeStatus = md.updateAdmissionTypeStatus(
							Long.parseLong(admissionType.get("admissionTypeId").toString()),
							admissionType.get("admissionTypeCode").toString(),
							admissionType.get("status").toString(),
							Long.parseLong(admissionType.get("userId").toString()));

					if (admissionTypeStatus != null && admissionTypeStatus.equalsIgnoreCase("200")) {
						jsonObject.put("admissionTypeStatus", admissionTypeStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	/***************************************
	 * MAS DisposedTo
	 ***********************************************************************/

	@Override
	public String getAllDisposedTo(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDisposedTo> disposedToList = new ArrayList<MasDisposedTo>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDisposedTo>> mapDisposedTo = md.getAllDisposedTo(jsondata);
			List totalMatches = new ArrayList();
			if (mapDisposedTo.get("disposedToList") != null) {
				disposedToList = mapDisposedTo.get("disposedToList");
				totalMatches = mapDisposedTo.get("totalMatches");
				for (MasDisposedTo disposedTo : disposedToList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (disposedTo != null) {
						mapObj.put("disposedToId", disposedTo.getDisposedToId());
						mapObj.put("disposedToCode", disposedTo.getDisposedToCode());
						mapObj.put("disposedToName", disposedTo.getDisposedToName());
						mapObj.put("status", disposedTo.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDisposedTo(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasDisposedTo masDisposedTo = new MasDisposedTo();

			masDisposedTo
					.setDisposedToCode(jsondata.get("disposedToCode").toString().toUpperCase());
			masDisposedTo
					.setDisposedToName(jsondata.get("disposedToName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masDisposedTo.setUser(user);
			masDisposedTo.setStatus("Y");
			masDisposedTo.setLastChgDate(date);

			List<MasDisposedTo> checkDisposedToList = md.validateDisposedTo(
					masDisposedTo.getDisposedToCode(), masDisposedTo.getDisposedToName());
			if (checkDisposedToList != null && checkDisposedToList.size() > 0) {
				if (checkDisposedToList.get(0).getDisposedToCode()
						.equalsIgnoreCase(jsondata.get("disposedToCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Disposed To Code already Exists");
				}
				if (checkDisposedToList.get(0).getDisposedToName()
						.equalsIgnoreCase(jsondata.get("disposedToName").toString())) {

					json.put("status", 2);
					json.put("msg", "Disposed To Name already Exists");
				}

			} else {
				String addDisposedToObj = md.addDisposedTo(masDisposedTo);
				if (addDisposedToObj != null && addDisposedToObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDisposedToDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("disposedToCode").toString() != null
					&& !jsonObject.get("disposedToCode").toString().trim().equalsIgnoreCase("")) {

				List<MasDisposedTo> msDisposedToList = md.validateDisposedToUpdate(
						jsonObject.get("disposedToCode").toString(),
						jsonObject.get("disposedToName").toString());
				if (msDisposedToList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Disposed To Name already exists\"}";
				}

				String updateDisposedTo = md.updateDisposedToDetails(
						Long.parseLong(jsonObject.get("disposedToId").toString()),
						jsonObject.get("disposedToCode").toString(),
						jsonObject.get("disposedToName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateDisposedTo != null && updateDisposedTo.equalsIgnoreCase("200")) {
					json.put("updateDisposedTo", updateDisposedTo);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDisposedToStatus(JSONObject disposedTo, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (disposedTo.get("disposedToCode").toString() != null
					&& !disposedTo.get("disposedToCode").toString().trim().equalsIgnoreCase("")) {

				MasDisposedTo mDisposedTo = md
						.checkDisposedTo(disposedTo.get("disposedToCode").toString());

				if (mDisposedTo != null) {
					String disposedToStatus = md.updateDisposedToStatus(
							Long.parseLong(disposedTo.get("disposedToId").toString()),
							disposedTo.get("disposedToCode").toString(),
							disposedTo.get("status").toString(),
							Long.parseLong(disposedTo.get("userId").toString()));

					if (disposedToStatus != null && disposedToStatus.equalsIgnoreCase("200")) {
						jsonObject.put("disposedToStatus", disposedToStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	
	/***************************************
	 * MAS PatientCondition
	 ***********************************************************************/

	@Override
	public String getAllCondition(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasPatientCondition> conditionList = new ArrayList<MasPatientCondition>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasPatientCondition>> mapCondition = md.getAllCondition(jsondata);
			List totalMatches = new ArrayList();
			if (mapCondition.get("conditionList") != null) {
				conditionList = mapCondition.get("conditionList");
				totalMatches = mapCondition.get("totalMatches");
				for (MasPatientCondition condition : conditionList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (condition != null) {
						mapObj.put("conditionId", condition.getConditionId());
						//mapObj.put("conditionCode", condition.getConditionCode());
						mapObj.put("conditionName", condition.getConditionName());
						mapObj.put("status", condition.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addCondition(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasPatientCondition masPatientCondition = new MasPatientCondition();

			masPatientCondition
					.setConditionName(jsondata.get("conditionName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masPatientCondition.setUser(user);
			masPatientCondition.setStatus("Y");
			masPatientCondition.setLastChgDate(date);

			List<MasPatientCondition> checkConditionList = md.validateCondition(masPatientCondition.getConditionName());
			if (checkConditionList != null && checkConditionList.size() > 0) {
				
				if (checkConditionList.get(0).getConditionName()
						.equalsIgnoreCase(jsondata.get("conditionName").toString())) {

					json.put("status", 2);
					json.put("msg", "Patient Condition Name already Exists");
				}

			} else {
				String addConditionObj = md.addCondition(masPatientCondition);
				if (addConditionObj != null && addConditionObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateConditionDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("conditionName").toString() != null
					&& !jsonObject.get("conditionName").toString().trim().equalsIgnoreCase("")) {

				List<MasPatientCondition> msConditionList = md.validateConditionUpdate(jsonObject.get("conditionName").toString());
				if (msConditionList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Patient Condition Name already exists\"}";
				}

				String updateCondition = md.updateConditionDetails(
						Long.parseLong(jsonObject.get("conditionId").toString()),
						//jsonObject.get("conditionCode").toString(),
						jsonObject.get("conditionName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateCondition != null && updateCondition.equalsIgnoreCase("200")) {
					json.put("updateCondition", updateCondition);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateConditionStatus(JSONObject condition, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (condition.get("conditionId").toString() != null
					&& !condition.get("conditionId").toString().trim().equalsIgnoreCase("")) {

				
					String conditionStatus = md.updateConditionStatus(
							Long.parseLong(condition.get("conditionId").toString()),
							condition.get("status").toString(),
							Long.parseLong(condition.get("userId").toString()));

					if (conditionStatus != null && conditionStatus.equalsIgnoreCase("200")) {
						jsonObject.put("conditionStatus", conditionStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} 

			}
		

		return jsonObject.toString();
	}
	
	
	/***************************************
	 * MAS Diet
	 ***********************************************************************/

	@Override
	public String getAllDiet(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDiet> dietList = new ArrayList<MasDiet>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDiet>> mapDiet = md.getAllDiet(jsondata);
			List totalMatches = new ArrayList();
			if (mapDiet.get("dietList") != null) {
				dietList = mapDiet.get("dietList");
				totalMatches = mapDiet.get("totalMatches");
				for (MasDiet diet : dietList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (diet != null) {
						mapObj.put("dietId", diet.getDietId());
						mapObj.put("dietCode", diet.getDietCode());
						mapObj.put("dietName", diet.getDietName());
						mapObj.put("status", diet.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDiet(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			long userId = Long.parseLong(jsondata.get("userId").toString());
			

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasDiet masDiet = new MasDiet();

			masDiet
					.setDietCode(jsondata.get("dietCode").toString().toUpperCase());
			masDiet
					.setDietName(jsondata.get("dietName").toString().toUpperCase());
			Users user = new Users();
			user.setUserId(userId);
			masDiet.setUser(user);
			masDiet.setStatus("Y");
			masDiet.setLastChgDate(date);

			List<MasDiet> checkDietList = md.validateDiet(
					masDiet.getDietCode(), masDiet.getDietName());
			if (checkDietList != null && checkDietList.size() > 0) {
				if (checkDietList.get(0).getDietCode()
						.equalsIgnoreCase(jsondata.get("dietCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Diet Code already Exists");
				}
				if (checkDietList.get(0).getDietName()
						.equalsIgnoreCase(jsondata.get("dietName").toString())) {

					json.put("status", 2);
					json.put("msg", "Diet Name already Exists");
				}

			} else {
				String addDietObj = md.addDiet(masDiet);
				if (addDietObj != null && addDietObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDietDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("dietCode").toString() != null
					&& !jsonObject.get("dietCode").toString().trim().equalsIgnoreCase("")) {

				List<MasDiet> msDietList = md.validateDietUpdate(
						jsonObject.get("dietCode").toString(),
						jsonObject.get("dietName").toString());
				if (msDietList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Diet Name already exists\"}";
				}

				String updateDiet = md.updateDietDetails(
						Long.parseLong(jsonObject.get("dietId").toString()),
						jsonObject.get("dietCode").toString(),
						jsonObject.get("dietName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));

				if (updateDiet != null && updateDiet.equalsIgnoreCase("200")) {
					json.put("updateDiet", updateDiet);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDietStatus(JSONObject diet, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (diet.get("dietCode").toString() != null
					&& !diet.get("dietCode").toString().trim().equalsIgnoreCase("")) {

				MasDiet mDiet = md
						.checkDiet(diet.get("dietCode").toString());

				if (mDiet != null) {
					String dietStatus = md.updateDietStatus(
							Long.parseLong(diet.get("dietId").toString()),
							diet.get("dietCode").toString(),
							diet.get("status").toString(),
							Long.parseLong(diet.get("userId").toString()));

					if (dietStatus != null && dietStatus.equalsIgnoreCase("200")) {
						jsonObject.put("dietStatus", dietStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	@Override
	public String getAllNiv(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreItem> storeItemList = new ArrayList<MasStoreItem>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasStoreItem>> mapStoreItem = md.getAllStoreNiv(jsonObj);
			List totalMatches = new ArrayList();
			if (mapStoreItem.get("masStoreItemList") != null) {
				storeItemList = mapStoreItem.get("masStoreItemList");
				totalMatches = mapStoreItem.get("totalMatches");
				for (MasStoreItem storeItem : storeItemList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("itemId", storeItem.getItemId());
					mapObj.put("pvmsNo", storeItem.getPvmsNo());
					mapObj.put("nomenclature", storeItem.getNomenclature() !=null ? storeItem.getNomenclature() :"");
					mapObj.put("status", storeItem.getStatus());
					mapObj.put("dispUnitQty", storeItem.getDispUnitQty() !=null ? storeItem.getDispUnitQty() :"");
					mapObj.put("rolD", storeItem.getRolD() !=null ? storeItem.getRolD() :"");
					mapObj.put("rolS", storeItem.getRolS() !=null ? storeItem.getRolS() :"");				
					mapObj.put("groupName",
							storeItem.getMasStoreGroup() != null ? storeItem.getMasStoreGroup().getGroupName() : "");
					mapObj.put("groupId",
							storeItem.getMasStoreGroup() != null ? storeItem.getMasStoreGroup().getGroupId() : "0");
					
					mapObj.put("storeUnitName",
							storeItem.getMasStoreUnit() != null ? storeItem.getMasStoreUnit().getStoreUnitName() : "");
					mapObj.put("storeUnitId",
							storeItem.getMasStoreUnit() != null ? storeItem.getMasStoreUnit().getStoreUnitId() : "0");
					
					mapObj.put("sectionName",
							storeItem.getMasStoreSection() != null ? storeItem.getMasStoreSection().getSectionName() : "");
					mapObj.put("sectionId",
							storeItem.getMasStoreSection() != null ? storeItem.getMasStoreSection().getSectionId() : "0");
					
					mapObj.put("itemClassName",
							storeItem.getMasItemClass() != null ? storeItem.getMasItemClass().getItemClassName() : "");
					mapObj.put("itemClassId",
							storeItem.getMasItemClass() != null ? storeItem.getMasItemClass().getItemClassId() : "0");
					
					mapObj.put("itemTypeName",
							storeItem.getMasItemType() != null ? storeItem.getMasItemType().getItemTypeName() : "");
					mapObj.put("itemTypeId",
							storeItem.getMasItemType() != null ? storeItem.getMasItemType().getItemTypeId() : "0");
					
					mapObj.put("hospitalName",
							storeItem.getMasHospital() != null ? storeItem.getMasHospital().getHospitalName() : "");
					mapObj.put("hospitalId",
							storeItem.getMasHospital() != null ? storeItem.getMasHospital().getHospitalId() : "0");
					
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	 
	@Override
	public String addDisease(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasDisease masDisease =new MasDisease();
			masDisease.setDiseaseCode(jsondata.getString("diseaseCode").toString());
			masDisease.setDiseaseName(jsondata.getString("diseaseName").toString());
			masDisease.setDiseaseTypeId(Long.parseLong(jsondata.getString("diseaseTypeId").toString()));
			masDisease.setLastChgBy(userId);
			masDisease.setStatus("Y");
			masDisease.setLastChgDate(date);

			List<MasDisease> validList = md.validateDisease(jsondata.getString("diseaseCode").toString(), jsondata.getString("diseaseName").toString());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getDiseaseCode().equalsIgnoreCase(jsondata.getString("diseaseCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Disease Code already Exists");
				}
				else if (validList.get(0).getDiseaseName().equalsIgnoreCase(jsondata.getString("diseaseName").toString())) {

					json.put("status", 2);
					json.put("msg", "Disease Name already Exists");
				}

			} else {
				String result = md.addDisease(masDisease);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	
	@Override
	public String getAllDisease(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDisease> diseaseList = new ArrayList<MasDisease>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDisease>> mapMasDisease = md.getAllDisease(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasDisease.get("diseaseList") != null) {
				diseaseList = mapMasDisease.get("diseaseList");
				totalMatches = mapMasDisease.get("totalMatches");
				for (MasDisease  masDisease : diseaseList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (masDisease != null) {
						mapObj.put("diseaseId", masDisease.getDiseaseId());
						mapObj.put("diseaseCode", masDisease.getDiseaseCode());
						mapObj.put("diseaseName", masDisease.getDiseaseName());
						mapObj.put("diseaseTypeId", masDisease.getMasDiseaseType() !=null ? masDisease.getMasDiseaseType().getDiseaseTypeId() : "");
						mapObj.put("diseaseType", masDisease.getMasDiseaseType() !=null ? masDisease.getMasDiseaseType().getDiseaseTypeName() : "");
						mapObj.put("status", masDisease.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateDiseaseStatus(HashMap<String, Object> dis, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dis.get("diseaseId").toString() != null && !dis.get("status").toString().equalsIgnoreCase("")) {			
				String disStatus = md.updateDiseaseStatus(Long.parseLong(dis.get("diseaseId").toString()),
						dis.get("status").toString());

				if (disStatus != null && disStatus.equals("200")) {
					json.put("disStatus", disStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateDisease(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasDisease masDisease=new MasDisease();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDisease.setDiseaseId(Long.parseLong(jsonObject.get("diseaseId").toString()));
					masDisease.setDiseaseName(jsonObject.get("diseaseName").toString());
					masDisease.setDiseaseCode(jsonObject.get("diseaseCode").toString());
					masDisease.setDiseaseTypeId(Long.parseLong(jsonObject.getString("diseaseTypeId").toString()));
					masDisease.setLastChgDate(date);
					masDisease.setLastChgBy(userId);
				String updateDisease = md.updateDisease(masDisease);
				if (updateDisease != null && updateDisease.equalsIgnoreCase("success")) {
					json.put("updateDisease", updateDisease);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	
	@Override
	public String addDocument(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasDocument masDocument=new MasDocument();
			masDocument.setDocumentCode(jsondata.getString("documentCode").toString());
			masDocument.setDocumentName(jsondata.getString("documentName").toString());
			masDocument.setLastChgBy(userId);
			masDocument.setStatus("Y");
			masDocument.setLastChgDate(date);

			List<MasDocument> validList = md.validateDocument(jsondata.getString("documentCode").toString().toUpperCase(), jsondata.getString("documentName").toString().toUpperCase());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getDocumentCode().equalsIgnoreCase(jsondata.getString("documentCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Document Code already Exists");
				}
				if (validList.get(0).getDocumentName().equalsIgnoreCase(jsondata.getString("documentName").toString())) {

					json.put("status", 2);
					json.put("msg", "Document Name already Exists");
				}

			} else {
				String result = md.addDocument(masDocument);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	@Override
	public String getAllDocument(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDocument> docList = new ArrayList<MasDocument>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDocument>> mapMasDisease = md.getAllDocument(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasDisease.get("docList") != null) {
				docList = mapMasDisease.get("docList");
				totalMatches = mapMasDisease.get("totalMatches");
				for (MasDocument  masDocument : docList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (mapMasDisease != null) {
						mapObj.put("documentId", masDocument.getDocumentId());
						mapObj.put("documentCode", masDocument.getDocumentCode());
						mapObj.put("documentName", masDocument.getDocumentName());
						mapObj.put("status", masDocument.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateDocumentStatus(HashMap<String, Object> dis, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dis.get("documentId").toString() != null && !dis.get("status").toString().equalsIgnoreCase("")) {			
				String docStatus = md.updateDocumentStatus(Long.parseLong(dis.get("documentId").toString()),
						dis.get("status").toString());

				if (docStatus != null && docStatus.equals("200")) {
					json.put("docStatus", docStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateDocument(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

				List<MasDocument> masDocumentList = md.validateDocument(jsonObject.get("documentCode").toString(), jsonObject.get("documentName").toString());
				if (masDocumentList.get(0).getDocumentName().equals(jsonObject.get("documentName").toString())) {
					return "{\"status\":\"0\",\"msg\":\"Document Name already exists\"}";
				}
				else {
					MasDocument masDocument=new MasDocument();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDocument.setDocumentId(Long.parseLong(jsonObject.get("documentId").toString()));
					masDocument.setDocumentName(jsonObject.get("documentName").toString());
					masDocument.setLastChgDate(date);
					masDocument.setLastChgBy(userId);
					
				String updateDocument = md.updateDocument(masDocument);
				if (updateDocument != null && updateDocument.equalsIgnoreCase("success")) {
					json.put("updateDocument", updateDocument);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String addBank(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasBank masBank=new MasBank();			
			masBank.setBankCode(jsondata.get("bankCode").toString().toUpperCase());
			masBank.setBankName(jsondata.get("bankName").toString().toUpperCase());
			masBank.setLastChgBy(userId);
			masBank.setStatus("Y");
			masBank.setLastChgDate(date);

			List<MasBank> validList = md.validateBankDetails(jsondata.getString("bankCode").toString(), jsondata.getString("bankName").toString());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getBankCode().equalsIgnoreCase(jsondata.getString("bankCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Bank Code already Exists");
				}
				if (validList.get(0).getBankName().equalsIgnoreCase(jsondata.getString("bankName").toString())) {

					json.put("status", 2);
					json.put("msg", "Bank Name already Exists");
				}

			} else {
				String result = md.addBank(masBank);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	
	@Override
	public String updateBankStatus(HashMap<String, Object> bank, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (bank.get("bankId").toString() != null && !bank.get("status").toString().equalsIgnoreCase("")) {			
				String bankStatus = md.updateBankStatus(Long.parseLong(bank.get("bankId").toString()),
						bank.get("status").toString());

				if (bankStatus != null && bankStatus.equals("200")) {
					json.put("bankStatus", bankStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String getAllBank(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasBank> bankList = new ArrayList<MasBank>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasBank>> mapMasBank = md.getAllBank(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasBank.get("bankList") != null) {
				bankList = mapMasBank.get("bankList");
				totalMatches = mapMasBank.get("totalMatches");
				for (MasBank  masBank : bankList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (mapMasBank != null) {
						mapObj.put("bankId", masBank.getBankId());
						mapObj.put("bankCode", masBank.getBankCode());
						mapObj.put("bankName", masBank.getBankName());
						mapObj.put("status", masBank.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateBankDetails(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

				List<MasBank> masBankList = md.validateBankDetails(jsonObject.get("bankCode").toString(), jsonObject.get("bankName").toString());
				if (masBankList.get(0).getBankName().equals(jsonObject.get("bankName").toString())) {
					return "{\"status\":\"0\",\"msg\":\"Bank Name already exists\"}";
				}
				else {
					MasBank masBank=new MasBank();
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masBank.setBankId(Long.parseLong(jsonObject.get("bankId").toString()));
					masBank.setBankName(jsonObject.get("bankName").toString().toUpperCase());
					masBank.setLastChgDate(date);
					masBank.setLastChgBy(userId);
					
				String updateBank = md.updateBankDetails(masBank);
				if (updateBank != null && updateBank.equalsIgnoreCase("success")) {
					json.put("updateBank", updateBank);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String addAccountType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasAccountType masAccountType=new MasAccountType();
			masAccountType.setAccountTypeCode(jsondata.get("accountTypeCode").toString().toUpperCase());
			masAccountType.setAccountTypeName(jsondata.get("accountTypeName").toString().toUpperCase());
			masAccountType.setLastChgBy(userId);
			masAccountType.setStatus("Y");
			masAccountType.setLastChgDate(date);

			List<MasAccountType> validList = md.validateAccountType(jsondata.getString("accountTypeCode").toString(), jsondata.getString("accountTypeName").toString());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getAccountTypeCode().equalsIgnoreCase(jsondata.getString("accountTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Account Type Code already Exists");
				}
				if (validList.get(0).getAccountTypeName().equalsIgnoreCase(jsondata.getString("accountTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Account Type Name already Exists");
				}

			} else {
				String result = md.addAccountType(masAccountType);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	
	@Override
	public String updateAccountTypeStatus(HashMap<String, Object> actstatus, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (actstatus.get("accountId").toString() != null && !actstatus.get("status").toString().equalsIgnoreCase("")) {			
				String actypeStatus = md.updateAccountTypeStatus(Long.parseLong(actstatus.get("accountId").toString()),
						actstatus.get("status").toString());

				if (actypeStatus != null && actypeStatus.equals("200")) {
					json.put("actypeStatus", actypeStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String getAllAccountType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasAccountType> accountTypeList = new ArrayList<MasAccountType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasAccountType>> mapMasAccount = md.getAllAccountType(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasAccount.get("accountTypeNameList") != null) {
				accountTypeList = mapMasAccount.get("accountTypeNameList");
				totalMatches = mapMasAccount.get("totalMatches");
				for (MasAccountType  massAccountType : accountTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (massAccountType != null) {
						mapObj.put("accountId", massAccountType.getAccountId());
						mapObj.put("accountTypeCode", massAccountType.getAccountTypeCode());
						mapObj.put("accountTypeName", massAccountType.getAccountTypeName());
						mapObj.put("status", massAccountType.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateAccountType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

				List<MasAccountType> masAccountTypeList = md.validateAccountTypeUpdate(jsonObject.get("accountTypeName").toString());
				if (masAccountTypeList!= null && masAccountTypeList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Account Type Name  already exists\"}";
				}
				else {
					MasAccountType masAccountType=new MasAccountType();
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masAccountType.setAccountId(Long.parseLong(jsonObject.get("accountId").toString()));
					masAccountType.setAccountTypeName(jsonObject.get("accountTypeName").toString().toUpperCase());
					masAccountType.setLastChgDate(date);
					masAccountType.setLastChgBy(userId);
					
				String updateAccountType = md.updateAccountType(masAccountType);
				if (updateAccountType != null && updateAccountType.equalsIgnoreCase("success")) {
					json.put("updateAccountType", updateAccountType);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	/***************************************
	 * MAS ICD_Diagnosis
	 ***********************************************************************/

	@Override
	public String getAllDiagnosis(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasIcd> icdList = new ArrayList<MasIcd>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasIcd>> mapIcd = md.getAllDiagnosis(jsondata);
			List totalMatches = new ArrayList();
			if (mapIcd.get("icdList") != null) {
				icdList = mapIcd.get("icdList");
				totalMatches = mapIcd.get("totalMatches");
				for (MasIcd icd : icdList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (icd != null) {
						mapObj.put("icdId", icd.getIcdId());
						mapObj.put("icdCode", icd.getIcdCode());
						mapObj.put("icdName", icd.getIcdName());
						mapObj.put("communicable", icd.getCommunicableFlag() !=null ? icd.getCommunicableFlag() :"N");
						mapObj.put("infectious", icd.getInfectionsFlag() !=null ? icd.getInfectionsFlag() : "N"  );
						mapObj.put("mfDiagnosis", icd.getMostCommonUser() !=null ? icd.getMostCommonUser() : "N"  );
						mapObj.put("status", icd.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDiagnosis(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			String status = "y";
			Long lastChgBy = new Long(1);

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());

			Users user = new Users();
			user.setUserId(userId);
			
			

			MasIcd masIcd = new MasIcd();

			masIcd.setIcdCode((String) jsondata.get("icdCode"));
			masIcd.setIcdName(jsondata.get("icdName").toString().toUpperCase());
			masIcd.setCommunicableFlag(jsondata.get("communicable").toString());
			masIcd.setInfectionsFlag(jsondata.get("infectious").toString());
			masIcd.setLastChgBy(lastChgBy);
			masIcd.setMostCommonUser(jsondata.getString("mfDiagnosis"));
			masIcd.setStatus("Y");
			masIcd.setLastChgDate(date);
			masIcd.setOpdFlag("N");

			List<MasIcd> checkIcdList = md.validateDiagnosis(masIcd.getIcdCode(),
					masIcd.getIcdName());
			if (checkIcdList != null && checkIcdList.size() > 0) {
				if ((checkIcdList.get(0).getIcdCode() + "")
						.equalsIgnoreCase((String) jsondata.get("icdCode"))) {

					json.put("status", 2);
					json.put("msg", "Diagnosis Code already Exists");
				}
				if (checkIcdList.get(0).getIcdName()
						.equalsIgnoreCase(jsondata.get("icdName").toString())) {

					json.put("status", 2);
					json.put("msg", "Diagnosis Name already Exists");
				}

			} else {
				String addIcdObj = md.addDiagnosis(masIcd);
				if (addIcdObj != null && addIcdObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateDiagnosis(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("icdCode").toString() != null
					&& !jsonObject.get("icdCode").toString().trim().equalsIgnoreCase("")) {				

				String updateIcd = md.updateDiagnosis(
						Long.parseLong(jsonObject.get("icdId").toString()),
						(jsonObject.get("icdCode").toString()), jsonObject.get("icdName").toString()
						,Long.parseLong(jsonObject.get("userId").toString()),jsonObject.get("communicable").toString(), 
						jsonObject.get("infectious").toString(),jsonObject.get("mfDiagnosis").toString());

				if (updateIcd != null && updateIcd.equalsIgnoreCase("200")) {
					json.put("updateIcd", updateIcd);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateDiagnosisStatus(JSONObject icd, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			if (icd.get("icdCode").toString() != null
					&& !icd.get("icdCode").toString().trim().equalsIgnoreCase("")) {

				MasIcd mIcd = md.checkDiagnosis(icd.get("icdCode").toString());

				if (mIcd != null) {
					String icdStatus = md.updateDiagnosisStatus(
							Long.parseLong(icd.get("icdId").toString()),
							(icd.get("icdCode").toString()), icd.get("status").toString()
							,Long.parseLong(icd.get("userId").toString()));

					if (icdStatus != null && icdStatus.equalsIgnoreCase("200")) {
						jsonObject.put("icdStatus", icdStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			}
		}

		return jsonObject.toString();
	}
	
	/*-------------------Medical Exam Schedule Master----------------------*/
	
	@Override
	public String addMedicalExamSchedule(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		CategoryDue categoryDue = new CategoryDue();

		if (!json.equals(null)) {

			if (json.get("fromMonth") == null) {
				return "{\"status\":\"0\",\"msg\":\"AMU Due From is not contain in json data or it will be null or blank please check\"}";
			}
			if (json.get("toMonth") == null) {
				return "{\"status\":\"0\",\"msg\":\"AMU Due To is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				categoryDue.setFromMonth(Long.parseLong(json.get("fromMonth").toString()));
				categoryDue.setToMonth(Long.parseLong(json.get("toMonth").toString()));

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				categoryDue.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				categoryDue.setStatus("y");
				categoryDue.setUser(users);
				MasEmployeeCategory employeeCategory = new MasEmployeeCategory();
				employeeCategory.setEmployeeCategoryId(Long.parseLong(json.get("employeeCategoryId").toString()));
				categoryDue.setMasEmployeeCategory(employeeCategory);
				categoryDue.setUser(users);
				List<CategoryDue> categoryDue1 = md.validateMedicalExamSchedule(Long.parseLong(json.get("employeeCategoryId").toString()));
				if (categoryDue1.size() != 0) {
					if (categoryDue1 != null && categoryDue1.size() > 0) {
						return "{\"status\":\"2\",\"msg\":\"Medica Exam Schedule already exists against the selected Employee Category\"}";
					}
				} else {
					String categoryDueObj = md.addMedicalExamSchedule(categoryDue);
					if (categoryDueObj != null && categoryDueObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (categoryDueObj != null && categoryDueObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", categoryDueObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}


	@Override
	public String getAllMedicalExamSchedule(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			List<CategoryDue> medicalExamScheduleList = new ArrayList<CategoryDue>();
			List<MasEmployeeCategory> mEmployeeCategoryList = new ArrayList<MasEmployeeCategory>();
			List totalMatches = new ArrayList();
			List hList = new ArrayList();
			List <Object[]>objectList= new ArrayList<Object[]>();
			List <Object[]>objectList1= new ArrayList<Object[]>();
			Map<String, Object> map = md.getAllMedicalExamSchedule(jsonObject);
			objectList = (List<Object[]>) map.get("objectList");
			objectList1 = (List<Object[]>) map.get("objectList1");
			totalMatches =(List<Object[]>) map.get("totalMatches");
			if (map.get("objectList") != null) {			

				for (Object[] list : objectList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (list != null) {
						mapObj.put("categoryDueId", list[2] !=null?list[2]:"");
						mapObj.put("employeeCategoryName",list[1] !=null?list[1]:"" );
						mapObj.put("employeeCategoryId", list[0] !=null?list[0]:"");
						//mapObj.put("categoryId", list[0] !=null?list[0]:"");
						mapObj.put("fromMonth", list[4] !=null?list[4]:"");
						mapObj.put("toMonth", list[5] !=null?list[5]:"");
						mapObj.put("status", list[6] !=null?list[6]:"");
						
						hList.add(mapObj);
					}
				}

				if (hList != null && hList.size() > 0) {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			} 
			if (map.get("objectList1") != null) {
				objectList1 = (List<Object[]>) map.get("objectList1");
				totalMatches =(List<Object[]>) map.get("totalMatches");

				for (Object[] list : objectList1) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (list != null) {						
						mapObj.put("categoryDueId",list[0] );
						mapObj.put("employeeCategoryName", list[1]);
						mapObj.put("employeeCategoryId", list[2]);
						mapObj.put("categoryId", list[3]);
						mapObj.put("fromMonth", list[4]);
						mapObj.put("toMonth", list[5]);
						mapObj.put("status", list[6]);
						hList.add(mapObj);
					}
				}

				if (hList != null && hList.size() > 0) {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("status", 1);
				} else {
					json.put("data", hList);
					json.put("count", totalMatches.size());
					json.put("msg", "Data not found");
					json.put("status", 0);
				}

			}		

		} else {
			json.put("status", 0);
			json.put("msg", "Record Not Found");
		}

		return json.toString();
	}
	
	@Override
	public String updateMedicalExamSchedule(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String medicalExamScheduleUpdate="";
		if (medicalExamSchedule.get("categoryDueId") != null && !medicalExamSchedule.get("categoryDueId").toString().equals("")) {
			
					medicalExamScheduleUpdate = md.updateMedicalExamSchedule(Long.parseLong(medicalExamSchedule.get("categoryDueId").toString()),
							Long.parseLong(medicalExamSchedule.get("employeeCategoryId").toString()),
							Long.parseLong(medicalExamSchedule.get("fromMonth").toString()),
							Long.parseLong(medicalExamSchedule.get("toMonth").toString()),
						Long.parseLong(medicalExamSchedule.get("userId").toString()));
				if (medicalExamScheduleUpdate != null && medicalExamScheduleUpdate.equalsIgnoreCase("200")) {
					json.put("medicalExamScheduleUpdate", medicalExamScheduleUpdate);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else if (medicalExamScheduleUpdate == null && medicalExamScheduleUpdate.equals("")) {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);
				}
				//}
		}
		 else {
			return "{\"status\":\"0\",\"msg\":\"Category Due Id is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String medicalExamScheduleStatus(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (medicalExamSchedule.get("categoryDueId").toString() != null && !medicalExamSchedule.get("categoryDueId").toString().equalsIgnoreCase("")) {		
			
				String medicalExamScheduleStatus = md.updateMedicalExamScheduleStatus(Long.parseLong(medicalExamSchedule.get("categoryDueId").toString()),
						medicalExamSchedule.get("status").toString());
				if (medicalExamScheduleStatus != null && medicalExamScheduleStatus.equalsIgnoreCase("200")) {
					json.put("medicalExamScheduleStatus", medicalExamScheduleStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			return "{\"status\":\"0\",\"msg\":\"Category Due Id is not available !!!\"}";
		}

		return json.toString();

	}
    
	@Override
	public String getRankCategoryList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();

		List<MasEmployeeCategory> mEmpCategory = md.getRankCategoryList();
		if (mEmpCategory != null && mEmpCategory.size() > 0) {

			json.put("data", mEmpCategory);
			json.put("count", mEmpCategory.size());
			json.put("status", 1);
		} else {
			json.put("status", 0);
			json.put("count", mEmpCategory.size());
			json.put("msg", "No Record Found");
		}

		return json.toString();
	}
	
	@Override
	public String getMedicalExamSchedule(HashMap<String, Object> medicalExamSchedule, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*-------------------FWC Master----------------------*/
	@Override
	public String addFWC(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		MasHospital masHospital = new MasHospital();

		if (!json.equals(null)) {

			if (json.get("hospitalName") == null) {
				return "{\"status\":\"0\",\"msg\":\"FWC is not contain in json data or it will be null or blank please check\"}";
			}

			else {
				masHospital.setHospitalName(json.get("hospitalName").toString().toUpperCase());

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				masHospital.setLastChgDate(date);

				String lastChgTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				long userId = Long.parseLong(json.get("userId").toString());
				Users users = new Users();
				users.setUserId(userId);
				masHospital.setStatus("y");
				masHospital.setUser(users);
				
				masHospital.setHospitalParentId(Long.parseLong(json.get("hospitalId").toString()));
				masHospital.setHospitalFlag("F");
				masHospital.setUser(users);
				List<MasHospital> masHospital1 = md.validateFWC(masHospital.getHospitalName().toString());
				if (masHospital1.size() != 0) {
					if (masHospital1 != null && masHospital1.size() > 0) {
						if (masHospital1.get(0).getHospitalName()
								.equalsIgnoreCase(json.get("hospitalName").toString())) {

							return "{\"status\":\"2\",\"msg\":\"FWC already Exists\"}";
						}
					}
				} else {
					String FWCObj = md.addFWC(masHospital);
					if (FWCObj != null && FWCObj.equalsIgnoreCase("200")) {
						jsonObj.put("status", 1);
						jsonObj.put("msg", "Record Added Successfully");

					} else if (FWCObj != null && FWCObj.equalsIgnoreCase("403")) {
						jsonObj.put("status", 0);
						jsonObj.put("msg", "Record Not Added");

					} else {
						jsonObj.put("msg", FWCObj);
						jsonObj.put("status", 0);
					}
				}
			}
		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}

		return jsonObj.toString();

	}
	
	public String getAllFWC(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasHospital> FWCList = new ArrayList<MasHospital>();
		List list = new ArrayList();
		if (jsonObj != null) {
			Map<String, List<MasHospital>> mapFWC = md.getAllFWC(jsonObj);
			List totalMatches = new ArrayList();
			if (mapFWC.get("FWCList") != null) {
				FWCList = mapFWC.get("FWCList");
				totalMatches = mapFWC.get("totalMatches");
				for (MasHospital fwc : FWCList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("hospitalFlag", fwc.getHospitalFlag());
					mapObj.put("status", fwc.getStatus());
					mapObj.put("hospitalId", fwc.getHospitalId());
					mapObj.put("hospitalName", fwc.getHospitalName());
					mapObj.put("ParentHospitalName",
							fwc.getHospitalParent() != null ? fwc.getHospitalParent().getHospitalName()
									: "");
					mapObj.put("hospitalParentId",
							fwc.getHospitalParent() != null ? fwc.getHospitalParent().getHospitalId()
									: "0");
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	

	@Override
	public String updateFWC(HashMap<String, Object> fwc, HttpServletRequest request,HttpServletResponse response) {

		JSONObject json = new JSONObject();
		String FWCUpdate="";
		if (fwc.get("hospitalId") != null && !fwc.get("hospitalId").toString().equals("")) {
			
							
					FWCUpdate = md.updateFWC(Long.parseLong(fwc.get("hospitalId").toString()),
						 fwc.get("hospitalName").toString(),
						Long.parseLong(fwc.get("hospitalId1").toString()),
						Long.parseLong(fwc.get("userId").toString()));
				if (FWCUpdate != null && FWCUpdate.equalsIgnoreCase("200")) {
					json.put("FWCUpdate", FWCUpdate);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else if (FWCUpdate == null && FWCUpdate.equals("")) {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);
				}
				//}
		}
		 else {
			return "{\"status\":\"0\",\"msg\":\"Hospital id is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String statusFWC(HashMap<String, Object> fwc, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		if (fwc.get("hospitalId").toString() != null && !fwc.get("hospitalId").toString().equalsIgnoreCase("")) {		
			
				String fwcStatus = md.updateFWCStatus(Long.parseLong(fwc.get("hospitalId").toString()),
						 fwc.get("status").toString());
				if (fwcStatus != null && fwcStatus.equalsIgnoreCase("200")) {
					json.put("fwcStatus", fwcStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					// json.put("masCmdStatus", masCmdStatus);
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			return "{\"status\":\"0\",\"msg\":\"Hospital id is not available !!!\"}";
		}

		return json.toString();

	}

	@Override
	public String getMIRoomList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasHospital> miRoomList = md.getMIRoomList();
		if (miRoomList != null && miRoomList.size() > 0) {

			jsonObj.put("data", miRoomList);
			jsonObj.put("count", miRoomList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", miRoomList);
			jsonObj.put("count", miRoomList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();

	}
	@Override
	public String addDiseaseType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasDiseaseType masDiseaseType =new MasDiseaseType();
			masDiseaseType.setDiseaseTypeCode(jsondata.getString("diseaseTypeCode").toString());
			masDiseaseType.setDiseaseTypeName(jsondata.getString("diseaseTypeName").toString());
			masDiseaseType.setLastChgBy(userId);
			masDiseaseType.setStatus("Y");
			masDiseaseType.setLastChgDate(date);

			List<MasDiseaseType> validList = md.validateDiseaseType(jsondata.getString("diseaseTypeCode").toString().toUpperCase(), jsondata.getString("diseaseTypeName").toString().toUpperCase());
			if (validList != null && validList.size() > 0) {
				if (validList.get(0).getDiseaseTypeCode().equalsIgnoreCase(jsondata.getString("diseaseTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Disease Type Code already Exists");
					
				}
				else if (validList.get(0).getDiseaseTypeName().equalsIgnoreCase(jsondata.getString("diseaseTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Disease Type Name already Exists");
				}

			} else {
				String result = md.addDiseaseType(masDiseaseType);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	@Override
	public String getAllDiseaseType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDiseaseType> diseaseTypeList = new ArrayList<MasDiseaseType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasDiseaseType>> mapMasDiseaseType = md.getAllDiseaseType(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasDiseaseType.get("diseaseTypeList") != null) {
				diseaseTypeList = mapMasDiseaseType.get("diseaseTypeList");
				totalMatches = mapMasDiseaseType.get("totalMatches");
				 {
					 diseaseTypeList.forEach( dt -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (dt != null ) {
								mapObj.put("diseaseTypeId", dt.getDiseaseTypeId());
								mapObj.put("diseaseTypeCode", dt.getDiseaseTypeCode());
								mapObj.put("diseaseTypeName", dt.getDiseaseTypeName());
								mapObj.put("status", dt.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateDiseaseTypeStatus(HashMap<String, Object> dis, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dis.get("diseaseTypeId").toString() != null && !dis.get("status").toString().equalsIgnoreCase("")) {			
				String disStatus = md.updateDiseaseTypeStatus(Long.parseLong(dis.get("diseaseTypeId").toString()),
						dis.get("status").toString());

				if (disStatus != null && disStatus.equals("200")) {
					json.put("disStatus", disStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateDiseaseType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasDiseaseType masDiseaseType=new MasDiseaseType();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDiseaseType.setDiseaseTypeId(Long.parseLong(jsonObject.get("diseaseTypeId").toString()));
					masDiseaseType.setDiseaseTypeName(jsonObject.get("diseaseTypeName").toString());
					masDiseaseType.setDiseaseTypeCode(jsonObject.get("diseaseTypeCode").toString());
					masDiseaseType.setLastChgDate(date);
					masDiseaseType.setLastChgBy(userId);
				String updateDiseaseType = md.updateDiseaseType(masDiseaseType);
				if (updateDiseaseType != null && updateDiseaseType.equalsIgnoreCase("success")) {
					json.put("updateDiseaseType", updateDiseaseType);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String addDiseaseMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Long diseaseId=Long.parseLong(jsondata.get("diseaseId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasDiseaseMapping masDiseaseMapping =new MasDiseaseMapping();
			masDiseaseMapping.setIcdId(jsondata.getString("diagnosis").toString());
			masDiseaseMapping.setDiseaseId(Long.parseLong(jsondata.getString("diseaseId").toString()));
			masDiseaseMapping.setLastChgBy(userId);
			masDiseaseMapping.setStatus("y");
			masDiseaseMapping.setLastChgDate(date);
			boolean flag = md.validateDiseaseById(diseaseId);
			if (flag) {

				json.put("status", 2);
				json.put("msg", "Disease Name already Exists");
			}
		
			 else {
				String result = md.addDiseaseMapping(masDiseaseMapping);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}
	
	@Override
	public String getAllDiseaseMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDiseaseMapping> diseaseMappingList = new ArrayList<MasDiseaseMapping>();
		List list = new ArrayList();
		List cListObj=new ArrayList<>();		
		if (jsondata != null) {
			Map<String, List<MasDiseaseMapping>> mapMasDiseaseMapping = md.getAllDiseaseMapping(jsondata);
			List totalMatches = new ArrayList();			
			if (mapMasDiseaseMapping.get("diseaseMappingList") != null) {
				diseaseMappingList = mapMasDiseaseMapping.get("diseaseMappingList");
				totalMatches = mapMasDiseaseMapping.get("totalMatches");
				 {
					 diseaseMappingList.forEach( dm -> {
						 String diagnosisName="";
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (dm != null ) {
									if(dm.getIcdId()!=null) {
									diagnosisName	=	md.getMasIcdById(dm.getIcdId());									
									if(StringUtils.isNotEmpty(diagnosisName)&& !diagnosisName.equalsIgnoreCase("##")) {
										String [] diagnosisArray=diagnosisName.split("##");
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("diseaseMappingId", dm.getDiseaseMappingId());
										map.put("masIcId", diagnosisArray[1]);
										map.put("masIcdName", diagnosisArray[0]);
										
										cListObj.add(map);
									}
								}
								mapObj.put("diseaseMappingId", dm.getDiseaseMappingId());
								mapObj.put("diseaseId", dm.getMasDisease().getDiseaseId());
								mapObj.put("diseaseName", dm.getMasDisease().getDiseaseName());
								mapObj.put("status", dm.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("diagnosisData", cListObj);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateDiseaseMappingStatus(HashMap<String, Object> dis, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (dis.get("diseaseMappingId").toString() != null && !dis.get("status").toString().equalsIgnoreCase("")) {			
				String disStatus = md.updateDiseaseMappingStatus(Long.parseLong(dis.get("diseaseMappingId").toString()),
						dis.get("status").toString());

				if (disStatus != null && disStatus.equals("200")) {
					json.put("disStatus", disStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateDiseaseMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasDiseaseMapping masDiseaseMapping=new MasDiseaseMapping();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masDiseaseMapping.setDiseaseMappingId(Long.parseLong(jsonObject.get("diseaseMappingId").toString()));
					masDiseaseMapping.setIcdId(jsonObject.get("diagnosis").toString());
					masDiseaseMapping.setDiseaseId(Long.parseLong(jsonObject.get("diseaseId").toString()));
					masDiseaseMapping.setLastChgDate(date);
					masDiseaseMapping.setLastChgBy(userId);
				String updateDiseaseMapping = md.updateDiseaseMapping(masDiseaseMapping);
				if (updateDiseaseMapping != null && updateDiseaseMapping.equalsIgnoreCase("success")) {
					json.put("updateDiseaseMapping", updateDiseaseMapping);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	
	@Override
	public String addMMU(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasMMU mmu=new MasMMU();
			//mmu.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
			mmu.setMmuCode(jsondata.get("mmuCode").toString());
			mmu.setMmuName(jsondata.get("mmuName").toString());
			mmu.setMmuNo(jsondata.get("regNo").toString());
			mmu.setMmuTypeId(Long.parseLong(jsondata.get("mmuTypeId").toString()));
			mmu.setMmuVendorId(Long.parseLong(jsondata.get("mmuVendorId").toString()));
			if(jsondata.has("oprationalDate") && !jsondata.getString("oprationalDate").isEmpty())
				mmu.setOprationalDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("oprationalDate").toString()));
			mmu.setLastChgBy(userId);
			mmu.setStatus("y");
			mmu.setLastChgDate(date);
			if(jsondata.has("pollNo"))
				mmu.setPollNo(jsondata.getString("pollNo"));
			if(jsondata.has("chassisNo"))
				mmu.setChassisNo(jsondata.getString("chassisNo"));
			if(jsondata.has("officeId"))
				mmu.setOfficeId(jsondata.getLong("officeId"));
			if(jsondata.has("districtId")&& jsondata.get("districtId")!=null 
					&& !jsondata.get("districtId").toString().equalsIgnoreCase("")
					&& !jsondata.get("districtId").toString().equalsIgnoreCase("0"))
				mmu.setDistrictId(jsondata.getLong("districtId"));
			String result = md.addMMU(mmu);
			if (result != null && result.equals("200")) {
				json.put("status", 1);
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}

		return json.toString();
	}
	
	@Override
	public String updateMMUStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("mmuId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String mmuStatus = md.updateMMUStatus(Long.parseLong(statusPayload.get("mmuId").toString()),
						statusPayload.get("status").toString() );

				if (mmuStatus != null && mmuStatus.equals("200")) {
					json.put("mmuStatus", mmuStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String getAllMMU(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMMU> mmuList = new ArrayList<MasMMU>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasMMU>> mapMasmmu = md.getAllMMU(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasmmu.get("mmuList") != null) {
				mmuList = mapMasmmu.get("mmuList");
				totalMatches = mapMasmmu.get("totalMatches");
				 {
					 mmuList.forEach( mmu -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (mmu != null ) {
								mapObj.put("mmuId", mmu.getMmuId());
								mapObj.put("regNo", mmu.getMmuNo() !=null ? mmu.getMmuNo() : "");
								mapObj.put("mmuName", mmu.getMmuName());
								mapObj.put("mmuCity", mmu.getMasCity() !=null ? mmu.getMasCity().getCityName() :"");
								mapObj.put("cityId", mmu.getMasCity()!=null ? mmu.getMasCity().getCityId() :"");
								mapObj.put("mmuCode", mmu.getMmuCode() !=null ? mmu.getMmuCode() :"");
								mapObj.put("mmuVendorId", mmu.getMasMmuVendor() !=null ? mmu.getMasMmuVendor().getMmuVendorId() :"");
								mapObj.put("operationalDate", mmu.getOprationalDate() !=null ? HMSUtil.changeDateToddMMyyyy(mmu.getOprationalDate()) :"");
								mapObj.put("mmuTypeId", mmu.getMasMmuType() !=null ? mmu.getMasMmuType().getMmuTypeId() :"");
							    mapObj.put("status", mmu.getStatus());
								mapObj.put("officeId", mmu.getOfficeId());
								mapObj.put("chassisNo", mmu.getChassisNo());
								mapObj.put("pollNo", mmu.getPollNo());
								if(mmu.getDistrictId()!=null)
								mapObj.put("districtId", mmu.getDistrictId());
								else
									mapObj.put("districtId","");
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateMMU(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasMMU masMMU=new MasMMU();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					masMMU.setMmuId(Long.parseLong(jsonObject.get("mmuId").toString()));
					masMMU.setMmuName(jsonObject.get("mmuName").toString());
					masMMU.setStatus(jsonObject.get("status").toString());
					masMMU.setLastChgBy(userId);
					masMMU.setLastChgDate(date);
					masMMU.setMmuNo(jsonObject.get("regNo").toString());
					//masMMU.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
					if(jsonObject.get("mmuTypeId")!=null && !jsonObject.get("mmuTypeId").toString().equalsIgnoreCase(""))
					masMMU.setMmuTypeId(Long.parseLong(jsonObject.get("mmuTypeId").toString()));
					if(jsonObject.get("mmuVendorId")!=null && !jsonObject.get("mmuVendorId").toString().equalsIgnoreCase(""))
					masMMU.setMmuVendorId(Long.parseLong(jsonObject.get("mmuVendorId").toString()));
					if(jsonObject.has("oprationalDate") && !jsonObject.getString("oprationalDate").isEmpty())
						masMMU.setOprationalDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsonObject.get("oprationalDate").toString()));
					if(jsonObject.has("pollNo"))
						masMMU.setPollNo(jsonObject.getString("pollNo"));
					if(jsonObject.has("chassisNo"))
						masMMU.setChassisNo(jsonObject.getString("chassisNo"));
					if(jsonObject.has("officeId") && !jsonObject.get("officeId").toString().equalsIgnoreCase("") 
							&& !jsonObject.get("officeId").toString().equalsIgnoreCase("0"))
						masMMU.setOfficeId(jsonObject.getLong("officeId"));
					if(jsonObject.has("districtId") && !jsonObject.get("districtId").toString().equalsIgnoreCase("") 
							&& !jsonObject.get("districtId").toString().equalsIgnoreCase("0"))
						masMMU.setDistrictId(jsonObject.getLong("districtId"));
				
					
				String updateMMU = md.updateMMU(masMMU);
				if (updateMMU != null && updateMMU.equalsIgnoreCase("success")) {
					json.put("updateMMU", updateMMU);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	
	@Override
	public String getAllCity(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasCity> mascityList = new ArrayList<MasCity>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasCity>> mapMascity = md.getAllCity(jsondata);
			List totalMatches = new ArrayList();
			if (mapMascity.get("mascityList") != null) {
				mascityList = mapMascity.get("mascityList");
				totalMatches = mapMascity.get("totalMatches");
				 {
					 
					 mascityList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("cityId", ct.getCityId());
								mapObj.put("cityCode", ct.getCityCode());
								mapObj.put("cityName", ct.getCityName());
								mapObj.put("districtName", ct.getMasDistrict().getDistrictName());
								mapObj.put("districtId", ct.getMasDistrict().getDistrictId());
								mapObj.put("mmuOperational", ct.getMmuOperational());
								mapObj.put("status", ct.getStatus());
								mapObj.put("indentCity", ct.getIndentCity());
								if(null!=ct.getOrderNo())
								{
									mapObj.put("sequenceNo", ct.getOrderNo());
								}
								else
								{
									mapObj.put("sequenceNo", "");
								}
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String getAllMMUVendor(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMmuVendor> masvendorList = new ArrayList<MasMmuVendor>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasMmuVendor>> mapMMUVendor = md.getAllMMUVendor(jsondata);
			List totalMatches = new ArrayList();
			if (mapMMUVendor.get("masvendorList") != null) {
				masvendorList = mapMMUVendor.get("masvendorList");
				totalMatches = mapMMUVendor.get("totalMatches");
				 {
					 masvendorList.forEach( vr -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (vr != null ) {
								mapObj.put("mmuVendorId", vr.getMmuVendorId());
								mapObj.put("mmuVendorCode", vr.getMmuVendorCode());
								mapObj.put("mmuVendorName", vr.getMmuVendorName());
								mapObj.put("status", vr.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String getAllMMUType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMmuType> masmmutypeList = new ArrayList<MasMmuType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasMmuType>> mapMMUType = md.getAllMMUType(jsondata);
			List totalMatches = new ArrayList();
			if (mapMMUType.get("masmmutypeList") != null) {
				masmmutypeList = mapMMUType.get("masmmutypeList");
				totalMatches = mapMMUType.get("totalMatches");
				 {
					 masmmutypeList.forEach( type -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (type != null ) {
								mapObj.put("mmuTypeId", type.getMmuTypeId());
								mapObj.put("mmuTypeCode", type.getMmuTypeCode());
								mapObj.put("mmuTypeName", type.getMmuTypeName());
								mapObj.put("status", type.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String validateRegNo(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
			String result = md.validateRegNo(jsondata);
			if(result !=null && result.equals("exists")) {
				json.put("msg", "Reg. No Already Exists");
				json.put("status", 1);
			}
			
		return json.toString();
	}
	
	@Override
	public String addUserType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasUserType userType=new MasUserType();
			userType.setUserTypeName(jsondata.getString("userTypeName").toString());
			userType.setUserTypeCode(jsondata.getString("userTypeCode").toString());
			userType.setMmuStaff(jsondata.getString("mmuStaff").toString());
			userType.setLastChgBy(userId);
			userType.setStatus("y");
			userType.setLastChgDate(date);
			List<MasUserType> validate=md.validateMasUserType(jsondata.getString("userTypeCode").toString(), jsondata.getString("userTypeName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getUserTypeCode().equalsIgnoreCase(jsondata.getString("userTypeCode").toString())) {
				json.put("status", 0);
				json.put("msg", "User Type Code Already Exists");
			}
			else if(validate.get(0).getUserTypeName().equalsIgnoreCase(jsondata.getString("userTypeName").toString())) {
				json.put("status", 0);
				json.put("msg", "User Type Name Already Exists");
			}
			}
			else {
			String result = md.addUserType(userType);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	
	
	@Override
	public String getAllUserType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUserType>> mapUserType = md.getAllUserType(jsondata);
			List totalMatches = new ArrayList();
			if (mapUserType.get("userTypeList") != null) {
				userTypeList = mapUserType.get("userTypeList");
				totalMatches = mapUserType.get("totalMatches");
				 {
					 userTypeList.forEach( ut -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ut != null ) {
								mapObj.put("userTypeId", ut.getUserTypeId());
								mapObj.put("userTypeCode", ut.getUserTypeCode());
								mapObj.put("userTypeName", ut.getUserTypeName());
								mapObj.put("status", ut.getStatus());
								mapObj.put("mmuStaff", ut.getMmuStaff() !=null ? ut.getMmuStaff() : "N");
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateUserTypeStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("userTypeId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String mmuStatus = md.updateUserTypeStatus(Long.parseLong(statusPayload.get("userTypeId").toString()),
						statusPayload.get("status").toString());

				if (mmuStatus != null && mmuStatus.equals("200")) {
					json.put("mmuStatus", mmuStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateUserType(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasUserType userType=new MasUserType();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					userType.setUserTypeId(Long.parseLong(jsonObject.get("userTypeId").toString()));
					userType.setUserTypeCode(jsonObject.get("userTypeCode").toString());
					userType.setUserTypeName(jsonObject.get("userTypeName").toString());
					userType.setStatus(jsonObject.get("status").toString());
					userType.setMmuStaff(jsonObject.get("mmuStaff").toString());		
					userType.setLastChgBy(userId);
					userType.setLastChgDate(date);				 
					String updateUserType = md.updateUserType(userType);
				if (updateUserType != null && updateUserType.equalsIgnoreCase("success")) {
					json.put("updateUserType", updateUserType);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String addCity(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Long districtId=Long.parseLong(jsondata.get("districtId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasCity masCity=new MasCity();
			masCity.setOrderNo(Long.parseLong(jsondata.get("sequenceNo").toString()));
			masCity.setCityCode(jsondata.get("cityCode").toString());	
			masCity.setCityName(jsondata.get("cityName").toString());
			masCity.setMmuOperational(jsondata.get("mmuOperational").toString());
			masCity.setIndentCity(jsondata.get("indentCity").toString());
			MasDistrict district=new MasDistrict();
			//district.setDistrictId(districtId);
			//masCity.setMasDistrict(district);
			masCity.setDistrictId(districtId);
			masCity.setLastChangeBy(userId);
			masCity.setStatus("y");
			masCity.setLastChangeDate(date);
			List<MasCity> validate=md.validateMasCity(jsondata.getString("cityCode").toString(), jsondata.getString("cityName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getCityCode().equalsIgnoreCase(jsondata.getString("cityCode").toString())) {
				json.put("status", 0);
				json.put("msg", "City Code Already Exists");
			}
			else if(validate.get(0).getCityName().equalsIgnoreCase(jsondata.getString("cityName").toString())) {
				json.put("status", 0);
				json.put("msg", "City Name Already Exists");
			}
			}
			else {
			String result = md.addCity(masCity);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	
	
	@Override
	public String updateCityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("cityId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String cityStatus = md.updateCityStatus(Long.parseLong(statusPayload.get("cityId").toString()),
						statusPayload.get("status").toString(),statusPayload.get("indentCity").toString());

				if (cityStatus != null && cityStatus.equals("200")) {
					json.put("cityStatus", cityStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateCity(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasCity  masCity=new MasCity();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					Long districtId=Long.parseLong(jsonObject.get("districtId").toString());
					masCity.setOrderNo(Long.parseLong(jsonObject.get("sequenceNo").toString()));
					masCity.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
					masCity.setCityCode(jsonObject.get("cityCode").toString());	
					masCity.setCityName(jsonObject.get("cityName").toString());
					masCity.setMmuOperational(jsonObject.get("mmuOperational").toString());
					MasDistrict district=new MasDistrict();
					district.setDistrictId(districtId);
					masCity.setMasDistrict(district);
					masCity.setLastChangeBy(userId);
					masCity.setStatus("y");
					masCity.setLastChangeDate(date);
					masCity.setIndentCity(jsonObject.get("indentCity").toString());
					String updateCity = md.updateCity(masCity);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String getAllDistrict(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDistrict> districtList = new ArrayList<MasDistrict>();
		List list = new ArrayList();
		MasMMU masMMu = null;
		if (jsondata != null) {
			Map<String, List<MasDistrict>> mapMasDistrict;
			if(jsondata.has("districtUser"))
			{
			 if(!jsondata.get("districtUser").equals("")&&!jsondata.get("districtUser").equals("0"))
			 {
			   mapMasDistrict = md.getFilterDistrict(jsondata);
			 }
			 else
			 {
					mapMasDistrict = md.getAllDistrict(jsondata);	
			 }
			}
			else
			{
				mapMasDistrict = md.getAllDistrict(jsondata);	
			}
			 List totalMatches = new ArrayList();
			if (jsondata.has("mmuIdSession") && StringUtils.isNotEmpty( jsondata.get("mmuIdSession").toString())) {
			 
				  masMMu =  md.getMasMMU(Long.parseLong(jsondata.get("mmuIdSession").toString()));
				  if(masMMu!=null && masMMu.getDistrictId()!=null)
				  json.put("districtIdByMMU", masMMu.getDistrictId());
				  else
					  json.put("districtIdByMMU", "");
			}
			else {
				json.put("districtIdByMMU", "");
			}
				districtList = mapMasDistrict.get("districtList");
				totalMatches = mapMasDistrict.get("totalMatches");
				 {
					 districtList.forEach( dt -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (dt != null ) {
								if(null!=dt.getOrderNo())
								{	
								 mapObj.put("sequenceNo", dt.getOrderNo());
								}
								else
								{
									mapObj.put("sequenceNo", "");	
								}
								mapObj.put("districtId", dt.getDistrictId());
								mapObj.put("districtCode", dt.getDistrictCode());
								if(jsondata.has("upssFlag"))
								{
									mapObj.put("districtName", dt.getUpss());	
								}
								else
								{	
								mapObj.put("districtName", dt.getDistrictName());
								}
								if(dt.getStartDate()!=null)
								{
									String startDate=HMSUtil.convertDateToStringFormat(dt.getStartDate(), "dd/MM/yyyy");
									mapObj.put("startDate", startDate);
								}
								else
								{
									mapObj.put("startDate", "");
								}
								mapObj.put("stateId", dt.getMasState().getStateId());
								mapObj.put("population", dt.getPopulation() !=null ?  dt.getPopulation() :" ");
								mapObj.put("status", dt.getStatus());
								mapObj.put("upss", dt.getUpss());
								if(dt.getLongitude()!=null)
								mapObj.put("longitude", dt.getLongitude());
								else
									mapObj.put("longitude","");
								if(dt.getLattitude()!=null)	
								mapObj.put("latitude", dt.getLattitude());
								else
									mapObj.put("latitude","");
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
					//json.put("district", masMMu);
					
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
					json.put("districtIdByMMU", "");
				}

			//} 

		} 
		return json.toString();
	}
	
	@Override
	public String addZone(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasZone masZone=new MasZone();
			MasCity mascity=new MasCity();
			mascity.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
			masZone.setZoneCode(jsondata.get("zoneCode").toString());
			masZone.setZoneName(jsondata.get("zoneName").toString());
			masZone.setMasCity(mascity);
			masZone.setLastChangeBy(userId);
			masZone.setStatus("y");
			masZone.setLastChangeDate(date);
			List<MasZone> validate=md.validateMasZone(jsondata.getString("zoneCode").toString(), jsondata.getString("zoneName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getZoneCode().equalsIgnoreCase(jsondata.getString("zoneCode").toString())) {
				json.put("status", 0);
				json.put("msg", "Zone Code Already Exists");
			}
			else if(validate.get(0).getZoneName().equalsIgnoreCase(jsondata.getString("zoneName").toString())) {
				json.put("status", 0);
				json.put("msg", "Zone Name Already Exists");
			}
			}
			else {
			String result = md.addZone(masZone);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString();
	}
	
	@Override
	public String getAllZone(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasZone> zoneList = new ArrayList<MasZone>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasZone>> mapZone = md.getAllZone(jsondata);
			List totalMatches = new ArrayList();
			if (mapZone.get("zoneList") != null) {
				zoneList = mapZone.get("zoneList");
				totalMatches = mapZone.get("totalMatches");
				 {
					 zoneList.forEach( zone -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (zone != null ) {
								mapObj.put("zoneId", zone.getZoneId());
								mapObj.put("zoneCode", zone.getZoneCode());
								mapObj.put("zoneName", zone.getZoneName());
								mapObj.put("cityName", zone.getMasCity().getCityName());
								mapObj.put("cityId", zone.getMasCity().getCityId());
								mapObj.put("status", zone.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	@Override
	public String updateZoneStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("zoneId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String zoneStatus = md.updateZoneStatus(Long.parseLong(statusPayload.get("zoneId").toString()),
						statusPayload.get("status").toString());

				if (zoneStatus != null && zoneStatus.equals("200")) {
					json.put("zoneStatus", zoneStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateZone(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasZone  masZone=new MasZone();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasCity mascity=new MasCity();
					mascity.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
					masZone.setZoneId(Long.parseLong(jsonObject.get("zoneId").toString()));
					masZone.setZoneCode(jsonObject.get("zoneCode").toString());
					masZone.setZoneName(jsonObject.get("zoneName").toString());
					masZone.setMasCity(mascity);
					masZone.setLastChangeBy(userId);
					masZone.setStatus("y");
					masZone.setLastChangeDate(date);					
					String updateZone = md.updateZone(masZone);
				if (updateZone != null && updateZone.equalsIgnoreCase("success")) {
					json.put("updateZone", updateZone);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}

	@Override
	public String getAllSymptoms(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		try {
			List<MasSymptoms> list =  md.getAllSymptoms(jsonObject);
			if(!list.isEmpty()) {
				for(MasSymptoms symptoms : list) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("id", symptoms.getId());
					map.put("name",symptoms.getName());
					map.put("code", symptoms.getCode());
					responseList.add(map);
				}
			}
			obj.put("status", true);
			obj.put("list", list);
			return obj.toString();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public String addWard(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasWard masWard=new MasWard();
			masWard.setWardName(jsondata.getString("wardName").toString());
			masWard.setWardNo(jsondata.getString("wardNo").toString());
			MasCity mascity=new MasCity();
			mascity.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
			//masWard.setMasCity(mascity);
			if(jsondata.get("cityId")!=null)
			masWard.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
			if(jsondata.has("zoneId")){
			
			if(jsondata.get("zoneId").toString() !=null) {
				MasZone masZone=new MasZone();
				
			masZone.setZoneId(Long.parseLong(jsondata.get("zoneId").toString()));
			
			masWard.setMasZone(masZone);
			}
			}
			masWard.setLastChangeBy(userId);
			masWard.setStatus("y");
			masWard.setLastChangeDate(date);
			boolean flag=true;
						
			List<MasWard> validateWname=md.validateWardName(jsondata.getString("wardName").toString(),
				Long.parseLong(jsondata.getString("cityId").toString()));
			
			if(validateWname !=null && validateWname.size()>0 && flag) {
				String cityName=validateWname.get(0).getMasCity().getCityName();
			  if(validateWname.get(0).getWardName().equalsIgnoreCase(jsondata.getString("wardName").toString())) {
				json.put("status", 0);
				json.put("msg", "Ward Name already mapped with the "+cityName);
				flag=false;
			  }			
			}
			
			if(flag) {
			String result = md.addWard(masWard);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		   }
			
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString(); 
	}

	
	@Override
	public String getAllWard(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasWard> wardList = new ArrayList<MasWard>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasWard>> mapWard = md.getAllWard(jsondata);
			List totalMatches = new ArrayList();
			if (mapWard.get("wardList") != null) {
				wardList = mapWard.get("wardList");
				totalMatches = mapWard.get("totalMatches");
				 {
					 wardList.forEach( ward -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ward != null ) {
								mapObj.put("wardId", ward.getWardId());
								mapObj.put("wardNo", ward.getWardNo() !=null ? ward.getWardNo() :"" );
								mapObj.put("wardName", ward.getWardName() !=null ? ward.getWardName() :"");
								
								mapObj.put("cityName", ward.getMasCity()!=null && ward.getMasCity().getCityName() !=null ? ward.getMasCity().getCityName() :"");
								mapObj.put("cityId", ward.getMasCity()!=null && ward.getMasCity().getCityId()!=null ?ward.getMasCity().getCityId():"" );
								mapObj.put("zoneId", ward.getMasZone() !=null && ward.getMasZone().getZoneId() !=null ? ward.getMasZone().getZoneId() :"");
								mapObj.put("zoneName", ward.getMasZone() !=null && ward.getMasZone().getZoneName() !=null ? ward.getMasZone().getZoneName() :"");	
								mapObj.put("status", ward.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}

		
	@Override
	public String updateWardStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("wardId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String wardStatus = md.updateWardStatus(Long.parseLong(statusPayload.get("wardId").toString()),
						statusPayload.get("status").toString());

				if (wardStatus != null && wardStatus.equals("200")) {
					json.put("wardStatus", wardStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateWard(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasWard masWard=new MasWard();
					masWard.setWardId(Long.parseLong(jsonObject.get("wardId").toString()));
					masWard.setWardName(jsonObject.getString("wardName").toString());
					masWard.setWardNo(jsonObject.getString("wardNo").toString());
					MasCity mascity=new MasCity();
					mascity.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
					masWard.setMasCity(mascity);
					if(jsonObject.get("cityId")!=null)
					masWard.setCityId(Long.parseLong(jsonObject.get("cityId").toString()));
					if(jsonObject.has("zoneId")) {
					MasZone masZone=new MasZone();
					masZone.setZoneId(Long.parseLong(jsonObject.get("zoneId").toString()));
					masWard.setMasZone(masZone);
					}
					masWard.setStatus("y");			
					
					masWard.setLastChangeBy(userId);
					masWard.setLastChangeDate(date);					
					String updateWard = md.updateWard(masWard);
				if (updateWard != null && updateWard.equalsIgnoreCase("success")) {
					json.put("updateWard", updateWard);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
  
	@Override
	public String addDistrict(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasDistrict masMasDistrict=new MasDistrict();
			masMasDistrict.setDistrictCode(jsondata.get("districtCode").toString());
			masMasDistrict.setDistrictName(jsondata.get("districtName").toString());
			masMasDistrict.setOrderNo(Long.parseLong(jsondata.get("sequenceNo").toString()));
			masMasDistrict.setLastChangeBy(userId);
			if(jsondata.get("population").toString() !=null && !jsondata.get("population").toString().isEmpty())
			{
				masMasDistrict.setPopulation(Long.parseLong(jsondata.get("population").toString()));	
			}
			MasState masState=new MasState();
			masState.setStateId(Long.parseLong(jsondata.get("stateId").toString()));
			masMasDistrict.setMasState(masState);
			masMasDistrict.setStatus("y");
			masMasDistrict.setLastChangeDate(date);
			masMasDistrict.setUpss(jsondata.get("UPSS").toString());
			masMasDistrict.setStartDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("startDate").toString()));
			if(jsondata.get("longitude").toString()!=null && StringUtils.isNotEmpty(jsondata.get("longitude").toString()))
			masMasDistrict.setLongitude(Double.parseDouble(jsondata.get("longitude").toString()));
			if(jsondata.get("latitude").toString()!=null && StringUtils.isNotEmpty(jsondata.get("latitude").toString()))
			masMasDistrict.setLattitude(Double.parseDouble(jsondata.get("latitude").toString()));
			
			
			List<MasDistrict> validate=md.validateMasDistrict(jsondata.getString("districtCode").toString(), jsondata.getString("districtName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getDistrictCode().equalsIgnoreCase(jsondata.getString("districtCode").toString())) {
				json.put("status", 0);
				json.put("msg", "District Code Already Exists");
			}
			else if(validate.get(0).getDistrictName().equalsIgnoreCase(jsondata.getString("districtName").toString())) {
				json.put("status", 0);
				json.put("msg", "District Name Already Exists");
			}
			}
			else {
			String result = md.addDistrict(masMasDistrict);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	
		
	@Override
	public String updateDistrictStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("districtId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String districtStatus = md.updateDistrictStatus(Long.parseLong(statusPayload.get("districtId").toString()),
						statusPayload.get("status").toString());

				if (districtStatus != null && districtStatus.equals("200")) {
					json.put("districtStatus", districtStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateDistrict(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasDistrict masDistrict=new MasDistrict();
					masDistrict.setDistrictId(Long.parseLong(jsonObject.get("districtId").toString()));
					masDistrict.setOrderNo(Long.parseLong(jsonObject.get("sequenceNo").toString()));
					masDistrict.setDistrictCode(jsonObject.getString("districtCode").toString());
					masDistrict.setDistrictName(jsonObject.getString("districtName").toString());
					if(jsonObject.getString("population").toString() !=null && !jsonObject.getString("population").toString().isEmpty())
					{
						masDistrict.setPopulation(Long.parseLong(jsonObject.getString("population").toString()));
					}
					masDistrict.setStartDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsonObject.get("startDate").toString()));
					MasState masState=new MasState();
					masState.setStateId(Long.parseLong(jsonObject.get("stateId").toString()));
					masDistrict.setMasState(masState);					
					masDistrict.setStatus(jsonObject.get("status").toString());					
					masDistrict.setLastChangeBy(userId);
					masDistrict.setLastChangeDate(date);	
					masDistrict.setUpss(jsonObject.getString("UPSS").toString());
					masDistrict.setStartDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsonObject.get("startDate").toString()));
					if(jsonObject.get("longitude").toString()!=null && StringUtils.isNotEmpty(jsonObject.get("longitude").toString()))
					masDistrict.setLongitude(Double.parseDouble(jsonObject.get("longitude").toString()));
					if(jsonObject.get("latitude").toString()!=null && StringUtils.isNotEmpty(jsonObject.get("latitude").toString()))
					masDistrict.setLattitude(Double.parseDouble(jsonObject.get("latitude").toString()));
					
					
					String updateDistrict = md.updateDistrict(masDistrict);
				if (updateDistrict != null && updateDistrict.equalsIgnoreCase("success")) {
					json.put("updateDistrict", updateDistrict);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
	
	@Override
	public String addTreatmentInstructions(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasTreatmentInstruction mti=new MasTreatmentInstruction();
			mti.setInstructionsCode(jsondata.getString("instructionsCode").toString());
			mti.setInstructionsName(jsondata.getString("instructionsName").toString());			
			mti.setLastChgBy(userId);
			mti.setStatus("y");
			mti.setLastChgDate(date);
			List<MasTreatmentInstruction> validate=md.validateMasTreatmentInstruction(jsondata.getString("instructionsCode").toString(), jsondata.getString("instructionsName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getInstructionsCode().equalsIgnoreCase(jsondata.getString("instructionsCode").toString())) {
				json.put("status", 0);
				json.put("msg", "Instructions Code Already Exists");
			}
			else if(validate.get(0).getInstructionsName().equalsIgnoreCase(jsondata.getString("instructionsName").toString())) {
				json.put("status", 0);
				json.put("msg", "Instructions Name Already Exists");
			}
			}
			else {
			String result = md.addTreatmentInstructions(mti);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString(); 
	}

	
	@Override
	public String getAllTreatmentInstructions(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasTreatmentInstruction> mtiList = new ArrayList<MasTreatmentInstruction>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasTreatmentInstruction>> mapInstructions = md.getAllTreatmentInstructions(jsondata);
			List totalMatches = new ArrayList();
			if (mapInstructions.get("mtiList") != null) {
				mtiList = mapInstructions.get("mtiList");
				totalMatches = mapInstructions.get("totalMatches");
				 {
					 mtiList.forEach( mti -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (mti != null ) {
								mapObj.put("instructionsId", mti.getInstructionsId());
								mapObj.put("instructionsCode", mti.getInstructionsCode() !=null ? mti.getInstructionsCode() :"" );
								mapObj.put("instructionsName", mti.getInstructionsName() !=null ? mti.getInstructionsName() :"");									
								mapObj.put("status", mti.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}
	
	@Override
	public String updateTreatmentInstructionsStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("instructionsId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String updateTreatmentInstructionsStatus = md.updateTreatmentInstructionsStatus(Long.parseLong(statusPayload.get("instructionsId").toString()),
						statusPayload.get("status").toString());

				if (updateTreatmentInstructionsStatus != null && updateTreatmentInstructionsStatus.equals("200")) {
					json.put("updateTreatmentInstructionsStatus", updateTreatmentInstructionsStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateTreatmentInstructions(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasTreatmentInstruction inst=new MasTreatmentInstruction();
					inst.setInstructionsId(Long.parseLong(jsonObject.get("instructionsId").toString()));	
					inst.setInstructionsCode(jsonObject.get("instructionsCode").toString());		
					inst.setInstructionsName(jsonObject.get("instructionsName").toString());			
					inst.setStatus(jsonObject.get("status").toString());					
					inst.setLastChgBy(userId);
					inst.setLastChgDate(date);					
					String updateTreatmentInstructions = md.updateTreatmentInstructions(inst);
				if (updateTreatmentInstructions != null && updateTreatmentInstructions.equalsIgnoreCase("success")) {
					json.put("updateTreatmentInstructions", updateTreatmentInstructions);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
	
	@Override
	public String addSignSymtoms(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasSymptoms masSymptoms=new MasSymptoms();
			masSymptoms.setCode(jsondata.get("code").toString());
			masSymptoms.setName(jsondata.get("name").toString());
			masSymptoms.setMostFrequentSymptoms(jsondata.get("mfSymtoms").toString());
			masSymptoms.setLastChgBy(userId);
			masSymptoms.setStatus("y");
			masSymptoms.setLastChgDate(date);
			List<MasSymptoms> validate=md.validateMasSymptoms(jsondata.getString("code").toString(), jsondata.getString("name").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getCode().equalsIgnoreCase(jsondata.getString("code").toString())) {
				json.put("status", 0);
				json.put("msg", "Sign Symtoms Code Already Exists");
			}
			else if(validate.get(0).getName().equalsIgnoreCase(jsondata.getString("name").toString())) {
				json.put("status", 0);
				json.put("msg", "Sign Symtoms Name Already Exists");
			}
			}
			else {
			String result = md.addSignSymtoms(masSymptoms);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString(); 
	}
	
	@Override
	public String getAllSignSymtoms(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasSymptoms> symptomsList = new ArrayList<MasSymptoms>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasSymptoms>> mapSignSymtoms = md.getAllSignSymtoms(jsondata);
			List totalMatches = new ArrayList();
			if (mapSignSymtoms.get("symptomsList") != null) {
				symptomsList = mapSignSymtoms.get("symptomsList");
				totalMatches = mapSignSymtoms.get("totalMatches");
				 {
					 symptomsList.forEach( ss -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ss != null ) {
								mapObj.put("id", ss.getId());
								mapObj.put("code", ss.getCode() !=null ? ss.getCode() :"");
								mapObj.put("name", ss.getName() !=null ? ss.getName() :"");	
								mapObj.put("mfSymtoms", ss.getMostFrequentSymptoms() !=null ? ss.getMostFrequentSymptoms() :"N");
								mapObj.put("status", ss.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}
	
	@Override
	public String updateSignSymtomsStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("id").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String updateSignSymtomsStatus = md.updateSignSymtomsStatus(Long.parseLong(statusPayload.get("id").toString()),
						statusPayload.get("status").toString());

				if (updateSignSymtomsStatus != null && updateSignSymtomsStatus.equals("200")) {
					json.put("updateSignSymtomsStatus", updateSignSymtomsStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateSignSymtoms(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasSymptoms masSymptoms=new MasSymptoms();
					masSymptoms.setId(Long.parseLong(jsonObject.get("id").toString()));
					masSymptoms.setCode(jsonObject.get("code").toString());	
					masSymptoms.setName(jsonObject.get("name").toString());
					masSymptoms.setMostFrequentSymptoms(jsonObject.get("mfSymtoms").toString());
					masSymptoms.setStatus(jsonObject.get("status").toString());					
					masSymptoms.setLastChgBy(userId);
					masSymptoms.setLastChgDate(date);	
					
					String updateSignSymtoms = md.updateSignSymtoms(masSymptoms);
				if (updateSignSymtoms != null && updateSignSymtoms.equalsIgnoreCase("success")) {
					json.put("updateSignSymtoms", updateSignSymtoms);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
	
	
	@Override
	public String addLabour(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasLabor masLabor=new MasLabor();
			masLabor.setLaborCode(jsondata.get("laborCode").toString());
			masLabor.setLaborName(jsondata.get("laborName").toString());
			masLabor.setLastChgBy(userId);
			masLabor.setStatus("y");
			masLabor.setLastChgDate(date);
			List<MasLabor> validate=md.validateMasLabor(jsondata.getString("laborCode").toString(), jsondata.getString("laborName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getLaborCode().equalsIgnoreCase(jsondata.getString("laborCode").toString())) {
				json.put("status", 0);
				json.put("msg", "Labour Code Already Exists");
			}
			else if(validate.get(0).getLaborName().equalsIgnoreCase(jsondata.getString("laborName").toString())) {
				json.put("status", 0);
				json.put("msg", "Labour Name Already Exists");
			}
			}
			else {
			String result = md.addLabour(masLabor);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString(); 
	}
	
	@Override
	public String getAllLabour(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasLabor> laborList = new ArrayList<MasLabor>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasLabor>> mapLabor = md.getAllLabour(jsondata);
			List totalMatches = new ArrayList();
			if (mapLabor.get("laborList") != null) {
				laborList = mapLabor.get("laborList");
				totalMatches = mapLabor.get("totalMatches");
				 {
					 laborList.forEach( labor -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (labor != null ) {
								mapObj.put("laborId", labor.getLaborId());
								mapObj.put("laborCode", labor.getLaborCode() !=null ?  labor.getLaborCode() :"");
								mapObj.put("laborName", labor.getLaborName() !=null ? labor.getLaborName() :"");									
								mapObj.put("status", labor.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}
	
	@Override
	public String updateLabourStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("laborId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String updateLabourStatus = md.updateLabourStatus(Long.parseLong(statusPayload.get("laborId").toString()),
						statusPayload.get("status").toString());

				if (updateLabourStatus != null && updateLabourStatus.equals("200")) {
					json.put("updateLabourStatus", updateLabourStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateLabour(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					MasLabor masLabor=new MasLabor();
					masLabor.setLaborId(Long.parseLong(jsonObject.get("laborId").toString()));
					masLabor.setLaborCode(jsonObject.get("laborCode").toString());
					masLabor.setLaborName(jsonObject.get("laborName").toString());
					masLabor.setStatus(jsonObject.get("status").toString());					
					masLabor.setLastChgBy(userId);
					masLabor.setLastChgDate(date);	
					
					String updateLabour = md.updateLabour(masLabor);
				if (updateLabour != null && updateLabour.equalsIgnoreCase("success")) {
					json.put("updateLabour", updateLabour);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
	
	public String getMMUHierarchicalList_old(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)md.getMMUHierarchicalList(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("mmuListdata");	
		jsonResponse.put("mmuListdata", result);
		return jsonResponse.toString(); 
	}
	
	public String getMMUHierarchicalList(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonResponse = new JSONObject();
		Map<String,Object> map = (Map<String,Object>)md.getMMUHierarchicalList(jsondata, request, response);
		JSONObject result = (JSONObject)map.get("mmuListdata");
		JSONArray jsonArray1 = new JSONArray();	
		jsonArray1=  (JSONArray) result.get("mmuList");
		List list = new ArrayList();
		List<MasMMU>listMasMMu=new ArrayList<MasMMU>();
		 for (int i = 0; i < jsonArray1.length(); i++) {
			 JSONObject explrObject = jsonArray1.getJSONObject(i);  
			 //System.err.println(explrObject.get("mmu_id"));
			 MasMMU masmmu=new MasMMU();
			 masmmu.setMmuId(Long.parseLong(explrObject.get("mmu_id").toString()));
			 masmmu.setMmuName(explrObject.get("mmu_name").toString());	
			 masmmu.setLastChgBy(Long.parseLong(explrObject.get("userid").toString()));
			 listMasMMu.add(masmmu);
		 }
		Collections.sort(listMasMMu, MasMMU.orderNoComparator);
		//System.out.println("listMasMMu"+listMasMMu);
		JSONArray jsonArray2 = new JSONArray();	
		listMasMMu.forEach( mmu -> {
		 
			JSONObject jsonObj = new JSONObject();
			if (mmu != null ) {
				jsonObj.put("mmu_id", mmu.getMmuId());
				jsonObj.put("mmu_name", mmu.getMmuName());
				jsonObj.put("userid", mmu.getLastChgBy());
				jsonArray2.put(jsonObj);
			}
		});
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mmuList", jsonArray2);
		
		jsonResponse.put("mmuListdata", jsonObject);
		return jsonResponse.toString(); 
	}

	
	@Override
	public String getAllPenalty(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		List<MasPenalty> penaltyList = new ArrayList<MasPenalty>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasPenalty>> mapPenalty = md.getAllPenalty(jsondata);
			List totalMatches = new ArrayList();
			if (mapPenalty.get("penaltyList") != null) {
				penaltyList = mapPenalty.get("penaltyList");
				totalMatches = mapPenalty.get("totalMatches");
				{
					penaltyList.forEach( penalty -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (penalty != null ) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

							mapObj.put("penaltyId", penalty.getPenaltyId());
							mapObj.put("penaltyCode", penalty.getPenaltyCode());
							mapObj.put("penaltyDescription", penalty.getPenaltyDescription() );
							mapObj.put("penaltyAmount", penalty.getPenaltyAmount());
							mapObj.put("startDate", formatter.format(penalty.getStartDate()));
							mapObj.put("endDate", formatter.format(penalty.getEndDate()));
							mapObj.put("status", penalty.getStatus());
							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}

	@Override
	public String addPenalty(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				if (!md.isRecordAlreadyExists("penaltyCode", jsondata.getString("penaltyCode"), MasPenalty.class)) {
					MasPenalty masPenalty = new MasPenalty();
					masPenalty.setPenaltyCode(jsondata.getString("penaltyCode"));
					masPenalty.setPenaltyDescription(jsondata.getString("penaltyDescription"));
					masPenalty.setPenaltyAmount(jsondata.getDouble("penaltyAmount"));

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
					Date startDate = formatter.parse(jsondata.getString("startDate"));
					Date endDate = formatter.parse(jsondata.getString("endDate"));
					masPenalty.setStartDate(startDate);
					masPenalty.setEndDate(endDate);
					masPenalty.setStatus("Y");

					String result = md.createRecord(masPenalty);
					if (result != null && result.equals("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully!");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Added!");
					}
				} else {
					json.put("status", 0);
					json.put("msg", "Record already exists with given Penalty Code!");
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Payload!");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String updatePenaltyStatus(HashMap<String, Object> statusPayload) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("penaltyId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String penaltyStatus = md.updatePenaltyStatus(Long.parseLong(statusPayload.get("penaltyId").toString()), statusPayload.get("status").toString());

			if (penaltyStatus != null && penaltyStatus.equals("200")) {
				json.put("penaltyStatus", penaltyStatus);
				json.put("msg", "Status Updated Successfully!");
				json.put("status", 1);
			} else {
				json.put("msg", "Status Not Updated!");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "Invalid Payload!");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updatePenalty(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				MasPenalty masPenalty = (MasPenalty)md.read(MasPenalty.class, jsondata.getLong("penaltyId"));
				masPenalty.setPenaltyCode(jsondata.getString("penaltyCode"));
				masPenalty.setPenaltyDescription(jsondata.getString("penaltyDescription"));
				masPenalty.setPenaltyAmount(jsondata.getDouble("penaltyAmount"));

				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
				Date startDate = formatter.parse(jsondata.getString("startDate"));
				Date endDate = formatter.parse(jsondata.getString("endDate"));
				masPenalty.setStartDate(startDate);
				masPenalty.setEndDate(endDate);

				String updatePenalty = md.updatePenalty(masPenalty);
				if (updatePenalty != null && updatePenalty.equalsIgnoreCase("success")) {
					json.put("updatePenalty", updatePenalty);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);
				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String getAllEquipmentChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		List<MasEquipmentChecklist> equipmentChecklists = new ArrayList<MasEquipmentChecklist>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasEquipmentChecklist>> masEquipmentChecklists = md.getAllEquipmentChecklist(jsondata);
			List totalMatches = new ArrayList();
			if (masEquipmentChecklists.get("equipmentChecklists") != null) {
				equipmentChecklists = masEquipmentChecklists.get("equipmentChecklists");
				totalMatches = masEquipmentChecklists.get("totalMatches");
				{
					List<MasPenalty> masPenalty = new ArrayList<>();
					equipmentChecklists.forEach( equipmentChecklist -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (equipmentChecklist != null ) {
							if (masPenalty.isEmpty())
								masPenalty.add((MasPenalty)md.read(MasPenalty.class, equipmentChecklist.getPenaltyId()));

							boolean isPenaltyExpired = false;
							if(masPenalty.get(0).getEndDate() != null && masPenalty.get(0).getEndDate().getTime() < new Date().getTime())
								isPenaltyExpired = true;

							mapObj.put("checklistId", equipmentChecklist.getChecklistId());
							mapObj.put("sequenceNo", equipmentChecklist.getSequenceNo());
							mapObj.put("instrumentCode", equipmentChecklist.getInstrumentCode());
							mapObj.put("instrumentName", equipmentChecklist.getInstrumentName() );
							mapObj.put("quantity", equipmentChecklist.getQuantity());
							mapObj.put("penaltyId", equipmentChecklist.getPenaltyId());
							mapObj.put("status", equipmentChecklist.getStatus());
							mapObj.put("penaltyCode", masPenalty.get(0).getPenaltyCode());
							mapObj.put("isPenaltyExpired", isPenaltyExpired);

							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}

	@Override
	public String addEquipmentChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				if (!md.isRecordAlreadyExists("instrumentCode", jsondata.getString("instrumentCode"), MasEquipmentChecklist.class)) {
					MasEquipmentChecklist masEquipmentChecklist = new MasEquipmentChecklist();
					masEquipmentChecklist.setInstrumentCode(jsondata.getString("instrumentCode"));
					masEquipmentChecklist.setInstrumentName(jsondata.getString("instrumentName"));
					masEquipmentChecklist.setQuantity(jsondata.getInt("quantity"));
					masEquipmentChecklist.setStatus(jsondata.getString("status"));
					masEquipmentChecklist.setSequenceNo(jsondata.getInt("sequenceNo"));
					masEquipmentChecklist.setPenaltyId(jsondata.getLong("penaltyId"));

					String result = md.createRecord(masEquipmentChecklist);
					if (result != null && result.equals("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully!");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Added!");
					}
				} else {
					json.put("status", 0);
					json.put("msg", "Record already exists with given Instrument Code!");
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Payload!");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String updateEquipmentChecklistStatus(HashMap<String, Object> statusPayload) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("checklistId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String equipmentChecklistStatus = md.updateEquipmentChecklistStatus(Long.parseLong(statusPayload.get("checklistId").toString()), statusPayload.get("status").toString());

			if (equipmentChecklistStatus != null && equipmentChecklistStatus.equals("200")) {
				json.put("equipmentChecklistStatus", equipmentChecklistStatus);
				json.put("msg", "Status Updated Successfully!");
				json.put("status", 1);
			} else {
				json.put("msg", "Status Not Updated!");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "Invalid Payload!");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateEquipmentChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				MasEquipmentChecklist masEquipmentChecklist = (MasEquipmentChecklist)md.read(MasEquipmentChecklist.class, jsondata.getLong("checklistId"));
				masEquipmentChecklist.setInstrumentName(jsondata.getString("instrumentName"));
				masEquipmentChecklist.setQuantity(jsondata.getInt("quantity"));
				masEquipmentChecklist.setSequenceNo(jsondata.getInt("sequenceNo"));
				if (masEquipmentChecklist.getPenaltyId() != jsondata.getLong("penaltyId")){
					List<MasEquipmentChecklist> li = md.getAllEquipmentChecklist(new JSONObject("{\"PN\": \"0\"}")).get("equipmentChecklists");
					li.forEach(entity -> {
						entity.setPenaltyId(jsondata.getLong("penaltyId"));
						md.updateEquipmentChecklist(entity);
					});
				}
				masEquipmentChecklist.setPenaltyId(jsondata.getLong("penaltyId"));

				String updateEquipmentChecklist = md.updateEquipmentChecklist(masEquipmentChecklist);
				if (updateEquipmentChecklist != null && updateEquipmentChecklist.equalsIgnoreCase("success")) {
					json.put("updateEquipmentChecklist", updateEquipmentChecklist);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);
				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String getAllInspectionChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		List<MasInspectionChecklist> inspectionChecklists = new ArrayList<MasInspectionChecklist>();
		List<HashMap<String, Object>> list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasInspectionChecklist>> mapPenalty = md.getAllInspectionChecklist(jsondata);
			List totalMatches = new ArrayList();
			if (mapPenalty.get("inspectionChecklists") != null) {
				inspectionChecklists = mapPenalty.get("inspectionChecklists");
				totalMatches = mapPenalty.get("totalMatches");
				{
					inspectionChecklists.forEach( inspectionChecklist -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (inspectionChecklist != null ) {
							MasPenalty masPenalty = (MasPenalty)md.read(MasPenalty.class, inspectionChecklist.getPenaltyId());
							boolean isPenaltyExpired = false;
							if(masPenalty.getEndDate() != null && masPenalty.getEndDate().getTime() < new Date().getTime())
								isPenaltyExpired = true;

							mapObj.put("checklistId", inspectionChecklist.getChecklistId());
							mapObj.put("checklistName", inspectionChecklist.getChecklistName());
							mapObj.put("typeOfInput", inspectionChecklist.getTypeOfInput() );
							mapObj.put("sequenceNo", inspectionChecklist.getSequenceNo());
							mapObj.put("status", inspectionChecklist.getStatus());
							mapObj.put("subsequence", inspectionChecklist.getSubsequence());
							mapObj.put("penaltyId", inspectionChecklist.getPenaltyId());
							mapObj.put("penaltyCode", masPenalty.getPenaltyCode());
							mapObj.put("isPenaltyExpired", isPenaltyExpired);
							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}

	@Override
	public String addInspectionChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				if (!md.isRecordAlreadyExists("checklistName", jsondata.getString("checklistName"), MasInspectionChecklist.class)) {
					Integer subsequence = null;
					JSONObject searchParams = new JSONObject();
					searchParams.put("PN", 0);
					searchParams.put("sequenceNo", jsondata.getInt("sequenceNo"));
					searchParams.put("status", "Y");

					if (jsondata.has("subsequence") && jsondata.get("subsequence") != null && !jsondata.get("subsequence").toString().isEmpty()){
						subsequence = jsondata.getInt("subsequence");
						searchParams.put("subsequence", jsondata.getInt("subsequence"));
					}
					Map<String, List<MasInspectionChecklist>> mapInspectionChecklist = md.getAllInspectionChecklist(searchParams);
					List<MasInspectionChecklist> list = mapInspectionChecklist.get("inspectionChecklists");
					if (subsequence == null && list != null && list.stream().filter(e -> e.getSubsequence() == null).count() > 0) {
						json.put("status", 0);
						json.put("msg", "Record already exist for given Sequence No.!");
					} else if (subsequence != null && list != null && !list.isEmpty()) {
						json.put("status", 0);
						json.put("msg", "Record already exist for given Sequence No. and Subsequence!");
					}else {
						MasInspectionChecklist masInspectionChecklist = new MasInspectionChecklist();
						masInspectionChecklist.setChecklistName(jsondata.getString("checklistName"));
						masInspectionChecklist.setTypeOfInput(jsondata.getString("typeOfInput"));
						masInspectionChecklist.setSequenceNo(jsondata.getInt("sequenceNo"));
						masInspectionChecklist.setSubsequence(subsequence);
						masInspectionChecklist.setPenaltyId(jsondata.getLong("penaltyId"));
						masInspectionChecklist.setStatus(jsondata.getString("status"));

						String result = md.createRecord(masInspectionChecklist);
						if (result != null && result.equals("200")) {
							json.put("status", 1);
							json.put("msg", "Record Added Successfully!");
						} else {
							json.put("status", 0);
							json.put("msg", "Record Not Added!");
						}
					}
				} else {
					json.put("status", 0);
					json.put("msg", "Record already exists with given Inspection Checklist Name!");
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Payload!");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String updateInspectionChecklistStatus(HashMap<String, Object> statusPayload) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("checklistId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String inspectionChecklistStatus = md.updateInspectionChecklistStatus(Long.parseLong(statusPayload.get("checklistId").toString()), statusPayload.get("status").toString());

			if (inspectionChecklistStatus != null && inspectionChecklistStatus.equals("200")) {
				json.put("inspectionChecklistStatus", inspectionChecklistStatus);
				json.put("msg", "Status Updated Successfully!");
				json.put("status", 1);
			} else {
				json.put("msg", "Status Not Updated!");
				json.put("status", 0);
			}

		} else {
			json.put("msg", "Invalid Payload!");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateInspectionChecklist(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				MasInspectionChecklist masInspectionChecklist = new MasInspectionChecklist();
				masInspectionChecklist.setChecklistId(jsondata.getLong("checklistId"));
				masInspectionChecklist.setTypeOfInput(jsondata.getString("typeOfInput"));
				masInspectionChecklist.setSequenceNo(jsondata.getInt("sequenceNo"));
				if(jsondata.has("subsequence") && !jsondata.getString("subsequence").isEmpty())
					masInspectionChecklist.setSubsequence(jsondata.getInt("subsequence"));
				masInspectionChecklist.setPenaltyId(jsondata.getLong("penaltyId"));
				masInspectionChecklist.setChecklistName(jsondata.getString("checklistName"));

				String updateInspectionChecklist = md.updateInspectionChecklist(masInspectionChecklist);
				if (updateInspectionChecklist != null && updateInspectionChecklist.equalsIgnoreCase("success")) {
					json.put("updateInspectionChecklist", updateInspectionChecklist);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);
				}

			} else {
				json.put("msg", "Invalid Payload!");
				json.put("status", 0);

			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}
	
	
	@Override
	public String getFrequentlyUsedSymptomsList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		List<Map<String,Object>> responseList = new ArrayList<Map<String,Object>>();
		try {
			List<MasSymptoms> list =  md.getFrequentlyUsedSymptomsList(jsonObject);
			if(!list.isEmpty()) {
				for(MasSymptoms symptoms : list) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("id", symptoms.getId());
					map.put("name",symptoms.getName());
					map.put("code", symptoms.getCode());
					responseList.add(map);
				}
			}
			obj.put("status", true);
			obj.put("list", responseList);
			return obj.toString();
		}catch(Exception ex) {
			ex.printStackTrace();
			obj.put("status", false);
			obj.put("list", responseList);
		}
		
		return obj.toString();
	}

	@Override
	public String addDeptMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String result="";
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			String deptIds=jsondata.get("departmentId").toString().replaceAll("\\[(.*?)\\]", "$1");
			String arrdeptIds[]=deptIds.split(",");
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			
			for(String dept : arrdeptIds) {
				dept=dept.replaceAll("\"", "");
				
				/*List<CityMmuMapping> validate= md.validateCityMmuMapping(jsondata.getString("cityId").toString(), mmu.trim().toString());
			     if(validate!=null && validate.size()>0) {
			    	 duplicatCounter++;
			     }
				
			     else {*/
				MMUDepartment mmuDepartment=new MMUDepartment();
				mmuDepartment.setMmuId(Long.parseLong(jsondata.get("mmuId").toString()));
				mmuDepartment.setDepartmentId(Long.parseLong(dept));
			
				mmuDepartment.setLastChangeBy(userId);
				mmuDepartment.setStatus("y");
				mmuDepartment.setLastChangeDate(date);
			    Long mmuId=Long.parseLong(jsondata.get("mmuId").toString());
				List<MMUDepartment> validate=md.validateMMUDepartment(mmuId, Long.parseLong(dept));
				if(validate !=null && validate.size()>0) {
					json.put("status",0);
					json.put("msg", "MMU Name already Mapped With Department");
				}
				else {
				result = md.addDeptMapping(mmuDepartment);
			     
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
			} 
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
		

		return json.toString(); 
	}
	
	
	@Override
	public String getAllDeptMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MMUDepartment> mmuDeptList = new ArrayList<MMUDepartment>();
		List list = new ArrayList();
		
		if (jsondata != null) {
			Map<String, List<MMUDepartment>> mapDepartment = md.getAllDeptMapping(jsondata);
			List totalMatches = new ArrayList();
			if (mapDepartment.get("mmuDeptList") != null) {
				mmuDeptList = mapDepartment.get("mmuDeptList");
				totalMatches = mapDepartment.get("totalMatches");
				 {
					 
					 mmuDeptList.forEach( dept -> {
						
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (dept != null ) {
								mapObj.put("mmuDepartmentId", dept.getMmuDepartmentId());
								mapObj.put("mmuName", dept.getMasMMU().getMmuName());
								mapObj.put("mmuId", dept.getMasMMU().getMmuId());
								mapObj.put("departmentId",dept.getMasDepartment().getDepartmentId());
								mapObj.put("departmentName",dept.getMasDepartment().getDepartmentName());
								mapObj.put("status", dept.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}
	
	@Override
	public String updateDeptMappingStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("mmuDepartmentId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String updateDeptMappingStatus = md.updateDeptMappingStatus(Long.parseLong(statusPayload.get("mmuDepartmentId").toString()),
						statusPayload.get("status").toString());

				if (updateDeptMappingStatus != null && updateDeptMappingStatus.equals("200")) {
					json.put("updateDeptMappingStatus", updateDeptMappingStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateDeptMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String result="";
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					
					String deptIds=jsonObject.get("departmentId").toString().replaceAll("\\[(.*?)\\]", "$1");
					String arrdeptIds[]=deptIds.split(",");
					String message="";
					Integer duplicate=0; 
					for(String dept : arrdeptIds) {
						dept=dept.replaceAll("\"", "");
					
						List<MMUDepartment> validate = md.validateMMUDepartment(Long.parseLong(jsonObject.get("mmuId").toString()), Long.parseLong(dept));
					if(validate!=null && validate.size()>0) {
						message="Department and MMU already exist";
						duplicate++;
					}
					else {
						MMUDepartment mmuDepartment=new MMUDepartment();
						mmuDepartment.setMmuDepartmentId(Long.parseLong(jsonObject.get("mmuDepartmentId").toString()));
						mmuDepartment.setMmuId(Long.parseLong(jsonObject.get("mmuId").toString()));
						mmuDepartment.setDepartmentId(Long.parseLong(dept));
					
						mmuDepartment.setLastChangeBy(userId);
						mmuDepartment.setStatus("y");
						mmuDepartment.setLastChangeDate(date);
					    Long mmuId=Long.parseLong(jsonObject.get("mmuId").toString());
						
						result = md.updateDeptMapping(mmuDepartment);
					}    
						if (result != null && result.equals("success")) {
							 	json.put("status", 1);
							json.put("msg", "Record Updated Successfully");
						}
						else if (duplicate != null && duplicate>0) {
						 	json.put("status", 1);
						json.put("msg", "Record Already exist");
					}
					
						else {
							json.put("status", 0);
							json.put("msg", "Record Not Updated");
						}
					 
					
					}
					
					} 
				
				else {
				   json.put("msg", "Data Not Found");
				   json.put("status", 0);

			}

		return json.toString(); 
	}
	
	
	/***************************************
	 * Supplier Type
	 ***********************************************************************/

	@Override
	public String getAllSupplierType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreSupplierType> supplierTypeList = new ArrayList<MasStoreSupplierType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasStoreSupplierType>> mapVendorType = md.getAllSupplierType(jsondata);
			List totalMatches = new ArrayList();
			if (mapVendorType.get("supplierTypeList") != null) {
				supplierTypeList = mapVendorType.get("supplierTypeList");
				totalMatches = mapVendorType.get("totalMatches");
				for (MasStoreSupplierType supplier : supplierTypeList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (supplier != null) {
						mapObj.put("supplierTypeId", supplier.getSupplierTypeId());
						mapObj.put("supplierTypeCode", supplier.getSupplierTypeCode());
						mapObj.put("supplierTypeName", supplier.getSupplierTypeName());
						mapObj.put("status", supplier.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
	
	@Override
	public String addSupplierType(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
					
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasStoreSupplierType masStoreSupplierType = new MasStoreSupplierType();
			
			masStoreSupplierType.setSupplierTypeCode(jsondata.get("supplierTypeCode").toString().toUpperCase());
			masStoreSupplierType.setSupplierTypeName(jsondata.get("supplierTypeName").toString().toUpperCase());
			long userId = Long.parseLong(jsondata.get("userId").toString());
			Users user=new Users();
			user.setUserId(userId);
			masStoreSupplierType.setLastChgBy(user);
			masStoreSupplierType.setStatus("Y");
			masStoreSupplierType.setLastChgDate(date);

			List<MasStoreSupplierType> checkVendorTypeList = md.validateSupplierType(masStoreSupplierType.getSupplierTypeCode(),
					masStoreSupplierType.getSupplierTypeName());
			if (checkVendorTypeList != null && checkVendorTypeList.size() > 0) {
				if (checkVendorTypeList.get(0).getSupplierTypeCode().equalsIgnoreCase(jsondata.get("supplierTypeCode").toString())) {

					json.put("status", 2);
					json.put("msg", "Supplier Type Code already Exists");
				}
				if (checkVendorTypeList.get(0).getSupplierTypeName().equalsIgnoreCase(jsondata.get("supplierTypeName").toString())) {

					json.put("status", 2);
					json.put("msg", "Supplier Type Name already Exists");
				}

			} else {
				String addSupplierTypeObj = md.addSupplierType(masStoreSupplierType);
				if (addSupplierTypeObj != null && addSupplierTypeObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateSupplierType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("supplierTypeCode").toString() != null
					&& !jsonObject.get("supplierTypeCode").toString().trim().equalsIgnoreCase("")) {

				List<MasStoreSupplierType> msVendorTypeList = md.validateSupplierTypeUpdate(jsonObject.get("supplierTypeCode").toString(),
						jsonObject.get("supplierTypeName").toString());
								
					if (msVendorTypeList.get(0).getSupplierTypeName().equalsIgnoreCase(jsonObject.get("supplierTypeName").toString())) {

						json.put("status", 2);
						json.put("msg", "Supplier Type Name already Exists");
					}
				
				else {
				String updateSupplierType = md.updateSupplierTypeDetails(
						Long.parseLong(jsonObject.get("supplierTypeId").toString()),
						jsonObject.get("supplierTypeCode").toString(), jsonObject.get("supplierTypeName").toString(),
						Long.parseLong(jsonObject.get("userId").toString()));
				
				if (updateSupplierType != null && updateSupplierType.equalsIgnoreCase("200")) {
					json.put("updateSupplierType", updateSupplierType);
					json.put("msg", "Record successfully Updated.");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
				
			}	

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateSupplierTypeStatus(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();

		if (jsonObject != null) {
			if (jsonObject.get("supplierTypeCode").toString() != null
					&& !jsonObject.get("supplierTypeCode").toString().trim().equalsIgnoreCase("")) {

				MasStoreSupplierType mStoreSupplierType = md.checkSupplierType(jsonObject.get("supplierTypeCode").toString());

				if (mStoreSupplierType != null) {
					String supplierTypeStatus = md.updateSupplierTypeStatus(Long.parseLong(jsonObject.get("supplierTypeId").toString()),
							jsonObject.get("supplierTypeCode").toString(), jsonObject.get("status").toString(),
							Long.parseLong(jsonObject.get("userId").toString()));

					if (supplierTypeStatus != null && supplierTypeStatus.equalsIgnoreCase("200")) {
						json.put("supplierTypeStatus", supplierTypeStatus);
						json.put("msg", "Status Updated Successfully");
						json.put("status", 1);
					} else {
						json.put("msg", "Status Not Updated");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Data Not Found");
				}

			}
		}

		return json.toString();
	}	
	
	
	@Override
	public String getAllAuditorName(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Users> auditorNameList = new ArrayList<Users>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<Users>> mapAuditor = md.getAllAuditorName(jsondata);
			List totalMatches = new ArrayList();
			if (mapAuditor.get("auditorNameList") != null) {
				auditorNameList = mapAuditor.get("auditorNameList");
				totalMatches = mapAuditor.get("totalMatches");
				 {
					 
					 auditorNameList.forEach( an -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (an != null ) {
								mapObj.put("auditiorId", an.getUserId());
								mapObj.put("audtiorName", an.getUserName());								
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	
	/***************************************
	 * Treatment Advice Master
	 ***********************************************************************/

	@Override
	public String getAllTreatmentAdvice(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<OpdTemplateMedicalAdvice> treatmentAdviceList = new ArrayList<OpdTemplateMedicalAdvice>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<OpdTemplateMedicalAdvice>> mapAdvice = md.getAllTreatmentAdvice(jsondata);
			List totalMatches = new ArrayList();
			if (mapAdvice.get("treatmentAdviceList") != null) {
				treatmentAdviceList = mapAdvice.get("treatmentAdviceList");
				totalMatches = mapAdvice.get("totalMatches");
				for (OpdTemplateMedicalAdvice advice : treatmentAdviceList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (advice != null) {
						mapObj.put("templateMadviceId", advice.getTemplateMadviceId());
						mapObj.put("adviceName", advice.getMedicalAdvice());
						mapObj.put("status", advice.getStatus() !=null ? advice.getStatus() :"");

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "get Record successfully");
					json.put("status", 1);
				} 

			} else {
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No JSON Data");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addTreatmentAdvice(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {

			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			long userId = Long.parseLong(jsondata.get("userId").toString());
			
			OpdTemplateMedicalAdvice advice = new OpdTemplateMedicalAdvice();

			advice.setMedicalAdvice(jsondata.get("adviceName").toString());
			advice.setLastChgDate(date);
			advice.setStatus("Y");
			advice.setLastChgBy(userId);	

			List<OpdTemplateMedicalAdvice> checkAdviceList = md.validateAdviceName(jsondata.get("adviceName").toString());
			if (checkAdviceList != null && checkAdviceList.size() > 0) {
				if (checkAdviceList.get(0).getMedicalAdvice().equalsIgnoreCase(jsondata.get("adviceName").toString())) {

					json.put("status", 2);
					json.put("msg", "Advice Name already Exists");
				}
				

			} else {
				String addTreatmentAdviceObj = md.addTreatmentAdvice(advice);
				if (addTreatmentAdviceObj != null && addTreatmentAdviceObj.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@Override
	public String updateTreatmentAdvice(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {

			if (jsonObject.get("adviceName").toString() != null && !jsonObject.get("adviceName").toString().trim().equalsIgnoreCase("")) {

				List<OpdTemplateMedicalAdvice> validAdviceList = md.validateAdviceName(jsonObject.get("adviceName").toString());
				if (validAdviceList.size() > 0) {
					return "{\"status\":\"0\",\"msg\":\"Advice Name already exists\"}";
				}

				String updateAdvice = md.updateTreatmentAdvice(Long.parseLong(jsonObject.get("adviceId").toString()),
						jsonObject.get("adviceName").toString(),Long.parseLong(jsonObject.get("userId").toString()));

				if (updateAdvice != null && updateAdvice.equalsIgnoreCase("200")) {
					json.put("updateAdvice", updateAdvice);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}

			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		}
		return json.toString();
	}

	@Override
	public String updateTreatmentAdviceStatus(JSONObject advice, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();

		if (jsonObject != null) {
			
				Long adviceId=Long.parseLong(advice.get("adviceId").toString());

				if (adviceId != null) {
					String adviceStatus = md.updateTreatmentAdviceStatus(adviceId);
							
					if (adviceStatus != null && adviceStatus.equalsIgnoreCase("200")) {
						jsonObject.put("adviceStatus", adviceStatus);
						jsonObject.put("msg", "Status Updated Successfully");
						jsonObject.put("status", 1);
					} else {
						jsonObject.put("msg", "Status Not Updated");
						jsonObject.put("status", 0);
					}
				} else {
					jsonObject.put("msg", "Data Not Found");
				}

			
		}

		return jsonObject.toString();
	}
	
	/**************************************
	 * Manufacturer Master
	 **************************************************/
	
	@Override
	public String addManufacturer(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String result="";
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			String supIds=jsondata.get("supplierTypeId").toString().replaceAll("\\[(.*?)\\]", "$1");
			String arrsupIds[]=supIds.split(",");
			
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			
			for(String sup : arrsupIds) {
				sup=sup.replaceAll("\"", "");
				MasManufacturer manufacturer=new MasManufacturer();
				manufacturer.setSupplierTypeId(Long.parseLong(sup));
				manufacturer.setManufacturerName(jsondata.get("manufacturerName").toString());			
				manufacturer.setLastChangeBy(userId);
				manufacturer.setStatus("y");
				manufacturer.setLastChgDate(date);
				List<MasManufacturer> validate=md.validateMasManufacturer(jsondata.get("manufacturerName").toString(), Long.parseLong(sup));
				if(validate !=null && validate.size()>0) {
					json.put("status",0);
					json.put("msg", "Manufacturer Name already Mapped With Vendor Type "+validate.get(0).getMasStoreSupplierType().getSupplierTypeName());
				}
				else {
				result = md.addManufacturer(manufacturer);
			     
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
			} 
		}	
		else {
			json.put("status", 0);
			json.put("msg", "No Record in JSON Data");
		}
	
		return json.toString(); 
	}
	
	
	@Override
	public String getAllManufacturer(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasManufacturer> manufacturerList = new ArrayList<MasManufacturer>();
		List list = new ArrayList();
		
		if (jsondata != null) {
			Map<String, List<MasManufacturer>> mapManufacturer = md.getAllManufacturer(jsondata);
			List totalMatches = new ArrayList();
			if (mapManufacturer.get("manufacturerList") != null) {
				manufacturerList = mapManufacturer.get("manufacturerList");
				totalMatches = mapManufacturer.get("totalMatches");
				 {
					 
					 manufacturerList.forEach( man -> {
						
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (man != null ) {
								mapObj.put("manufacturerId", man.getManufacturerId());
								mapObj.put("manufacturerName", man.getManufacturerName());
								mapObj.put("supplierTypeId", man.getMasStoreSupplierType().getSupplierTypeId());
								mapObj.put("supplierTypeName",man.getMasStoreSupplierType().getSupplierTypeName());
								mapObj.put("status", man.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString(); 
	}
	
	@Override
	public String updateManufacturerStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("manufacturerId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String updateManufacturerStatus = md.updateManufacturerStatus(Long.parseLong(statusPayload.get("manufacturerId").toString()),
						statusPayload.get("status").toString());

				if (updateManufacturerStatus != null && updateManufacturerStatus.equals("200")) {
					json.put("updateManufacturerStatus", updateManufacturerStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateManufacturer(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String result="";
		if (jsonObject != null) {				
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					
					String supIds=jsonObject.get("supplierTypeId").toString().replaceAll("\\[(.*?)\\]", "$1");
					String arrsupIds[]=supIds.split(",");
					
					for(String sup : arrsupIds) {
						sup=sup.replaceAll("\"", "");
						MasManufacturer manufacturer=new MasManufacturer();
						manufacturer.setManufacturerId(Long.parseLong(jsonObject.get("manufacturerId").toString()));
						manufacturer.setSupplierTypeId(Long.parseLong(sup));
						manufacturer.setManufacturerName(jsonObject.get("manufacturerName").toString());			
						manufacturer.setLastChangeBy(userId);
						manufacturer.setLastChgDate(date);
										
						result = md.updateManufacturer(manufacturer);
					     
						if (result != null && result.equals("success")) {
							json.put("status", 1);
							json.put("msg", "Record Updated Successfully");
						} else {
							json.put("status", 0);
							json.put("msg", "Record Not Updated");
						}
					 }
					} 
				
				else {
				   json.put("msg", "Data Not Found");
				   json.put("status", 0);

			}

		return json.toString(); 
	}

	@Override
	public String getLegacyCityMasterData(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<LagecyData> legacyList = new ArrayList<LagecyData>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		if (jsondata != null) {
		
			List<LagecyData> lData = md.getLegaCityMasterData(Integer.parseInt(jsondata.getString("cityId").toString()));
            if (lData != null &&  CollectionUtils.isNotEmpty(lData) ) {
				
				try {
					for (LagecyData v : lData) {
						HashMap<String, Object> pt = new HashMap<String, Object>();
						pt.put("cityId", v.getCityId());
						pt.put("noOfCamp", v.getDaiCampCount());
						pt.put("totalPatient", v.getDaiTotPatient());
						pt.put("averagePatientCount", v.getDaiAvgPatient());
						pt.put("countOfPatientLabTest", v.getDaiLabPatient());
						pt.put("countOPatientMdicineGiven", v.getDaiMedPatient());
						pt.put("countOPatientLabourDepartment", v.getDaiDepRegPatient());
						pt.put("countOPatientLabourRegistration", v.getDaiLabRegPatient());
						
						pt.put("countOfMMUCamp", v.getMmssyCampCount());
						pt.put("totalNoOfPatient", v.getMmssyTotPatient());
						pt.put("averagePatientMMU", v.getMmssyAvgPatient());
						pt.put("noOfPatientLabTest", v.getMmssyLabPatient());
						pt.put("noOfPatientMedicineDispensed", v.getMmssyMedPatient());
						pt.put("noOfPatientLabourDepartment", v.getMmssyDepRegPatient());
						pt.put("noOfPatientLabourRegistration", v.getMmssyLabRegPatient());
						
						pt.put("noOfLabourMale", v.getLabourMale());
						pt.put("noOfLabourFemale", v.getLabourFemale());
						pt.put("noOfLabourChild", v.getLabourChild());
						pt.put("noOfLabourTransgender", v.getLabourO());
						//pt.put("totalBeneficiary", v.getLabourTot());
						pt.put("noOfPatientAppliedLabourBoc", v.getLabourRegBoc());
						pt.put("noOfPatientAppliedLabour", v.getLabourRegOther());
						pt.put("noOfPatientAppliedBoc", v.getLabourAppBoc());
						pt.put("noOfPatientAppliedOthers", v.getLabourOthBoc());
						pt.put("noOfUnrgistredWorkersTeated", v.getLabourUnregBoc());
						pt.put("noOfUnrgistredWorkers", v.getLabourUnregOther());
						pt.put("noOfNonLabourGeneral", v.getLabourRegGc());
						
						list.add(pt);
						
					}
				}
				
				catch(Exception e)
				{
					e.printStackTrace();
					return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
				}

				

			}
            if (list != null && list.size() > 0) {
				json.put("data", list);
				json.put("msg", "Get Record successfully");
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", 0);
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}

		} 
		return json.toString();
	}

	@Override
	public String saveOrUpdateLgacyData(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
	    String legacyData = null;
		JSONObject json = new JSONObject();
	
	try {
		if (!jsondata.isEmpty())
		{
			if(jsondata.get("cityId").equals(""))
			{
				return "city Id cannot be blank";
			}
			else 
			{
				legacyData = md.saveOrUpdateLgacyData(jsondata);
				
			}
			
			if (legacyData != null && legacyData.equalsIgnoreCase("Successfully saved")) {
					json.put("msg", "Legacy data Details Saved successfully ");
					json.put("status", "1");
				} else if (legacyData != null && legacyData.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", legacyData);
					json.put("status", "0");
				}
			
		} else {
			json.put("status", "0");
			json.put("msg", "json not contain any object");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	return json.toString();
 }
	/*@Override
	public String updateVendorBillRemarks(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try {
			if (jsondata != null) {
				Long captureVendorBillDetailId = jsondata.getLong("captureVendorBillDetailId");
				String remarksType = jsondata.getString("remarksType");
				CaptureVendorBillDetail captureVendorBillDetail = (CaptureVendorBillDetail)auditDao.read(CaptureVendorBillDetail.class, captureVendorBillDetailId);
				captureVendorBillDetail.setStatus(jsondata.getString("status"));
				if("AUDITOR".equals(remarksType))
					captureVendorBillDetail.setAuditorFileName(jsondata.getString("fileName"));
				auditDao.updateRecord(captureVendorBillDetail);

				JSONArray vendorBillAuditorDataArr = new JSONArray(jsondata.getString("vendorBillAuditorData"));
				for(int i=0;i<vendorBillAuditorDataArr.length();i++){
					JSONObject obj = vendorBillAuditorDataArr.getJSONObject(i);
					Long id = obj.getLong("id");
					CaptureVendorBillMMUDetail captureVendorBillMMUDetail = (CaptureVendorBillMMUDetail)auditDao.read(CaptureVendorBillMMUDetail.class, id);

					if("CO".equals(remarksType)) {
						captureVendorBillMMUDetail.setCoRemarks(obj.getString("remarks"));
						captureVendorBillMMUDetail.setRemovePenalty(obj.getString("removePenalty"));
					} else captureVendorBillMMUDetail.setAuditorsRemarks(obj.getString("remarks"));

					auditDao.updateRecord(captureVendorBillMMUDetail);
				}

				json.put("msg", "Record Updated successfully");
				json.put("status", 1);
			}
		}catch (Exception ex) {
			json.put("status", 0);
			json.put("msg", "Error Capturing Vendor Invoice Bill!");
			ex.printStackTrace();
		}

		return json.toString();
	}*/
	@Override
	public String addCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasCluster masCluster=new MasCluster();
			//masCluster.setClusterCode(jsondata.get("cityCode").toString());
			masCluster.setClusterName(jsondata.get("clusterName").toString());
			masCluster.setLastChgBy(userId);
			masCluster.setStatus("y");
			masCluster.setLastChgDate(date);
			if(!jsondata.get("clusterId").equals(""))
			{
				Long clusterId=Long.parseLong(jsondata.get("clusterId").toString());
				String clusterName=jsondata.get("clusterName").toString();
				String status=jsondata.get("status").toString();
				Long userIdUpdate=Long.parseLong(jsondata.get("clusterId").toString());
				String result=md.updateMasCluster(clusterId, clusterName, status, userIdUpdate);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Update Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

			else {
				String result = md.addCluster(masCluster);

				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String getAllCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasCluster> masClusterList = new ArrayList<MasCluster>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasCluster>> mapMascity = md.getAllCluster(jsondata);
			List totalMatches = new ArrayList();
			if (mapMascity.get("clusterList") != null) {
				masClusterList = mapMascity.get("clusterList");
				totalMatches = mapMascity.get("totalMatches");
				{

					masClusterList.forEach( ct -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (ct != null ) {
							mapObj.put("clusterId", ct.getClusterId());
							mapObj.put("clusterName", ct.getClusterName());
							mapObj.put("status", ct.getStatus());
							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}
     
	
	@Override
	public String getAllCityMmuMapping(JSONObject jsondata){
		JSONObject json = new JSONObject();
		//List<CityMmuMapping> cityMmuList = new ArrayList<>();
		List<Object[]>listObject=null;
		List list = new ArrayList();
		if (jsondata != null) {
			Integer cityMapPage=0;
			if(!jsondata.has("cityMmuPage")) {
				cityMapPage	=1;
			}
			Map<String, List<Object[]>> cityMmuMappingMap = md.getAllCityMmuMapping(jsondata);
			List totalMatches = new ArrayList();
			if (cityMmuMappingMap.get("cityMmuMappingList") != null) {
				listObject = cityMmuMappingMap.get("cityMmuMappingList");
				totalMatches = cityMmuMappingMap.get("totalMatches");
				
				Integer count=0;   
					/*cityMmuList.forEach( cityMmuMapping -> {
						 
						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (cityMmuMapping != null ) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

							//MasMMU masMMU = (MasMMU)md.read(MasMMU.class, cityMmuMapping.getMmuId());
							//MasCity masCity = (MasCity)md.read(MasCity.class, cityMmuMapping.getCityId());
							if(cityMmuMapping.getLastChangeBy() != null) {
								Users user = (Users) md.read(Users.class, cityMmuMapping.getLastChangeBy());
								mapObj.put("lastChangeByName", user.getUserName());
							}
							mapObj.put("cityMmuMappingId", cityMmuMapping.getCityMmuMappingId());
							mapObj.put("cityId", cityMmuMapping.getCityId());
							mapObj.put("cityName",cityMmuMapping.getMasCity().getCityName()); //masCity.getCityName());
							mapObj.put("mmuId", cityMmuMapping.getMmuId() );
							mapObj.put("mmuName", cityMmuMapping.getMasMmu().getMmuName());//masMMU.getMmuName() );
							mapObj.put("status", cityMmuMapping.getStatus());
							mapObj.put("lastChangeById", cityMmuMapping.getLastChangeBy());

							mapObj.put("lastChangeDate", formatter.format(cityMmuMapping.getLastChangeDate()));
							list.add(mapObj);
						    
						}
						
					}
					
							);*/
				
			 
				
				
				
				if(listObject!=null && listObject.size()>0) {
  					 
				    	 for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
				    		 HashMap<String, Object> mapObj = new HashMap<String, Object>();
				    		 Object[] row = (Object[]) it.next();
 								if(row[1]!= null) {
									if(cityMapPage==1) {
										Users user = (Users) md.read(Users.class, Long.parseLong(row[1].toString()));
										mapObj.put("lastChangeByName", user.getUserName());
										}
										}
								    if(row[0]!=null)
									mapObj.put("cityMmuMappingId", Long.parseLong(row[0].toString()));
								    else
								    	mapObj.put("cityMmuMappingId","");
								    if(row[3]!=null)	
									mapObj.put("cityId", Long.parseLong(row[3].toString()));
								    else
								    	mapObj.put("cityId","");
								    if(row[4]!=null) {
								    	mapObj.put("cityName",row[4].toString());
								    }
								    else
								    	mapObj.put("cityName","");
								    if(row[5]!=null) {
								    	mapObj.put("mmuId", Long.parseLong(row[5].toString()));	
								    }
								    else
								    	mapObj.put("mmuId", "");
									 
								    if(row[6]!=null) {
								    	mapObj.put("mmuName",  row[6].toString());	
								    }
								    else
								    	mapObj.put("mmuName", "");
									
								    if(row[7]!=null) {
								    	mapObj.put("status",  row[7].toString());	
								    }
								    else
								    	mapObj.put("status", "");
									  
								    if(row[7]!=null) {
								    	mapObj.put("status",  row[7].toString());	
								    }
								    else
								    	mapObj.put("status", "");
									  
								    if(row[1]!=null) {
								    	mapObj.put("lastChangeById",  Long.parseLong(row[1].toString()));	
								    }
								    else
								    	mapObj.put("lastChangeById", "");
									  
									 
								    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
								    String lastChangeDate = "";
								    if(row[2]!=null) {
								   
								    	Date s =  HMSUtil.convertStringDateToUtilDate(row[2].toString(), "yyyy-MM-dd");
								     	lastChangeDate = HMSUtil.convertDateToStringFormat(
								    		s, "dd/MM/yyyy");
								    
								    	mapObj.put("lastChangeDate",   lastChangeDate);
								    
								    }
								    	else
								    	mapObj.put("lastChangeDate","");
								    list.add(mapObj);
								    count++;
								    if(count==5)
										break;
						}
				     }
				 

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}
	public String addCityMmuMapping(JSONObject jsondata){
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				String mmus = jsondata.getString("mmus");
				long cityId = jsondata.getLong("cityId");
				//String status = jsondata.getString("status");
				String msg="";
				String result ="";
				Integer counter=0;
				Integer duplicatCounter=0;
				for(String mmu: mmus.split(",")){
					List<CityMmuMapping> validate= md.validateCityMmuMapping(jsondata.getString("cityId").toString(), mmu.trim().toString());
				     if(validate!=null && validate.size()>0) {
				    	 duplicatCounter++;
				     }
				    else{
					CityMmuMapping cityMmuMapping = new CityMmuMapping();
					cityMmuMapping.setCityId(cityId);
					cityMmuMapping.setStatus("A");
					cityMmuMapping.setLastChangeBy(jsondata.getLong("lastChangeBy"));
					cityMmuMapping.setMmuId(Long.parseLong(mmu));
					cityMmuMapping.setLastChangeDate(new Date());
					result = md.createRecord(cityMmuMapping);
					counter++;
					}
					if (result != null && result.equals("200") && counter>0 && duplicatCounter==0) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully!");
					} 
					else if(duplicatCounter >0 && counter==0) {
						json.put("status", 1);
						json.put("msg", "Record already Exist!");
					}
					else if(duplicatCounter >0 && counter>0) {
						json.put("status", 1);
						json.put("msg", "Some Record Added and Some Record already Exist!");
					}
					else {
						json.put("status", 0);
						json.put("msg", "Record Not Added!");
					}
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Payload!");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String addCityCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			ClusterCityMapping cityCluster=new ClusterCityMapping();
			//masCluster.setClusterCode(jsondata.get("cityCode").toString());
			cityCluster.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
			cityCluster.setClusterId(Long.parseLong(jsondata.getString("clusterId")));
			cityCluster.setLastChgBy(userId);
			cityCluster.setStatus("y");
			cityCluster.setLastChgDate(date);
			if(null!=jsondata.get("cityClusterId")&&!jsondata.get("cityClusterId").equals(""))
			{
				Long cityClusterId=Long.parseLong(jsondata.get("cityClusterId").toString());
				Long clusterId=Long.parseLong(jsondata.get("clusterId").toString());
				Long cityId=Long.parseLong(jsondata.get("cityId").toString());
				String status=jsondata.get("status").toString();
				Long userIdUpdate=Long.parseLong(jsondata.get("userId").toString());
				String result=md.updateMasCityClusterMap(cityClusterId,clusterId, cityId, status, userIdUpdate);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Update Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

			else {
				String result = md.addCityCluster(cityCluster);

				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String getAllCityCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<ClusterCityMapping> clusterCityMappinglList = new ArrayList<ClusterCityMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<ClusterCityMapping>> clusterCity = md.getAllCityCluster(jsondata);
			List totalMatches = new ArrayList();
			if (clusterCity.get("cityClusterList") != null) {
				clusterCityMappinglList = clusterCity.get("cityClusterList");
				totalMatches = clusterCity.get("totalMatches");
				for (ClusterCityMapping clusterCityMappingl : clusterCityMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (clusterCityMappingl != null) {
						mapObj.put("cityId", clusterCityMappingl.getCityId());
						mapObj.put("clusterCityId", clusterCityMappingl.getClusterCityMappingId());
						mapObj.put("clusterId", clusterCityMappingl.getClusterId());
						mapObj.put("cityName", clusterCityMappingl.getMasCity().getCityName());
						mapObj.put("ClusterName", clusterCityMappingl.getMasCluster().getClusterName());
						mapObj.put("status", clusterCityMappingl.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDistrictCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			ClusterDistrictMapping districtCluster=new ClusterDistrictMapping();
			//masCluster.setClusterCode(jsondata.get("cityCode").toString());
			districtCluster.setDistrictId(Long.parseLong(jsondata.get("districtId").toString()));
			districtCluster.setClusterId(Long.parseLong(jsondata.getString("clusterId")));
			districtCluster.setLastChgBy(userId);
			districtCluster.setStatus("y");
			districtCluster.setLastChgDate(date);
			if(null!=jsondata.get("districtClusterId")&&!jsondata.get("districtClusterId").equals(""))
			{
				Long districtClusterId=Long.parseLong(jsondata.get("districtClusterId").toString());
				Long clusterId=Long.parseLong(jsondata.get("clusterId").toString());
				Long districtId=Long.parseLong(jsondata.get("districtId").toString());
				String status=jsondata.get("status").toString();
				Long userIdUpdate=Long.parseLong(jsondata.get("userId").toString());
				String result=md.updateMasDistrictClusterMap(districtClusterId,clusterId, districtId, status, userIdUpdate);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Update Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}

			else {
				String result = md.addDistrictCluster(districtCluster);

				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String getAllDistrictCluster(JSONObject jsondata, HttpServletRequest request,
										HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<ClusterDistrictMapping> clusterDistrictMappinglList = new ArrayList<ClusterDistrictMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<ClusterDistrictMapping>> clusterCity = md.getAllDistrictCluster(jsondata);
			List totalMatches = new ArrayList();
			if (clusterCity.get("districtClusterList") != null) {
				clusterDistrictMappinglList = clusterCity.get("districtClusterList");
				totalMatches = clusterCity.get("totalMatches");
				for (ClusterDistrictMapping clusterDistMappingl : clusterDistrictMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (clusterDistMappingl != null) {
						mapObj.put("districtId", clusterDistMappingl.getDistrictId());
						mapObj.put("clusterCityId", clusterDistMappingl.getClusterDistrictMappingId());
						mapObj.put("clusterId", clusterDistMappingl.getClusterId());
						mapObj.put("districtName", clusterDistMappingl.getMasDistrict().getDistrictName());
						mapObj.put("ClusterName", clusterDistMappingl.getMasCluster().getClusterName());
						mapObj.put("status", clusterDistMappingl.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getClusterByDistrict(JSONObject jsondata, HttpServletRequest request,
									   HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasCity> clusterDistrictMappinglList = new ArrayList<MasCity>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasCity>> clusterCity = md.getClusterByDistrict(jsondata);
			List totalMatches = new ArrayList();
			if (clusterCity.get("districtClusterList") != null) {
				clusterDistrictMappinglList = clusterCity.get("districtClusterList");
				totalMatches = clusterCity.get("totalMatches");
				for (MasCity clusterDistMappingl : clusterDistrictMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (clusterDistMappingl != null) {
						mapObj.put("districtId", clusterDistMappingl.getMasDistrict().getDistrictId());
						//mapObj.put("clusterCityId", clusterDistMappingl.getClusterDistrictMappingId());
						//mapObj.put("clusterId", clusterDistMappingl.getClusterId());
						mapObj.put("districtName", clusterDistMappingl.getMasDistrict().getDistrictName());
						//mapObj.put("ClusterName", clusterDistMappingl.getMasCluster().getClusterName());
						mapObj.put("cityId", clusterDistMappingl.getCityId() );
						mapObj.put("cityName", clusterDistMappingl.getCityName());
						
						mapObj.put("status", clusterDistMappingl.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getCityByCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasCity> clusterCityMappinglList = new ArrayList<MasCity>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasCity>> clusterCity = md.getClusterByCity(jsondata);
			List totalMatches = new ArrayList();
			if (clusterCity.get("cityClusterList") != null) {
				clusterCityMappinglList = clusterCity.get("cityClusterList");
				totalMatches = clusterCity.get("totalMatches");
				for (MasCity clusterDistMappingl : clusterCityMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (clusterDistMappingl != null) {
						mapObj.put("cityId", clusterDistMappingl.getCityId());
						///mapObj.put("clusterCityId", clusterDistMappingl.getClusterCityMappingId());
						//mapObj.put("clusterId", clusterDistMappingl.getClusterId());
						mapObj.put("cityName", clusterDistMappingl.getCityName());
						//mapObj.put("ClusterName", clusterDistMappingl.getClusterName());
						mapObj.put("status", clusterDistMappingl.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String updateClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
									  HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("clusterId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String clusterStatus = md.updateClusterStatus(Long.parseLong(statusPayload.get("clusterId").toString()),
					statusPayload.get("status").toString());

			if (clusterStatus != null && clusterStatus.equals("200")) {
				json.put("clusterStatus", clusterStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateDistrictClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
											  HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("districtClusterId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String clusterStatus = md.updateDistrictClusterStatus(Long.parseLong(statusPayload.get("districtClusterId").toString()),
					statusPayload.get("status").toString());

			if (clusterStatus != null && clusterStatus.equals("200")) {
				json.put("clusterStatus", clusterStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateCityClusterStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
										  HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("cityClusterId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String clusterStatus = md.updateCityClusterStatus(Long.parseLong(statusPayload.get("cityClusterId").toString()),
					statusPayload.get("status").toString());

			if (clusterStatus != null && clusterStatus.equals("200")) {
				json.put("clusterStatus", clusterStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String getAllSociety(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<MasSociety> societyList = new ArrayList<MasSociety>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasSociety>> mapMasSociety = md.getAllSociety(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasSociety.get("societyList") != null) {
				societyList = mapMasSociety.get("societyList");
				totalMatches = mapMasSociety.get("totalMatches");
				{
					societyList.forEach( society -> {

						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (society != null ) {
							mapObj.put("societyId", society.getSocietyId());
							mapObj.put("societyCode", society.getSocietyCode());
							mapObj.put("societyName", society.getSocietyName());
							mapObj.put("status", society.getStatus());
							list.add(mapObj);
						}
					});

				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}

	@Override
	public String addSociety(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);

			MasSociety masSociety=new MasSociety();
			masSociety.setSocietyCode(jsondata.get("societyCode").toString());
			masSociety.setSocietyName(jsondata.get("societyName").toString());
			masSociety.setLastChgBy(userId);
			masSociety.setStatus("y");
			masSociety.setLastChgDate(date);

			List<MasSociety> validate=md.validateMasSociety(jsondata.getString("societyCode").toString(), jsondata.getString("societyName").toString());
			if(validate !=null && validate.size()>0) {
				if(validate.get(0).getSocietyCode().equalsIgnoreCase(jsondata.getString("societyCode").toString())) {
					json.put("status", 0);
					json.put("msg", "Society Code Already Exists");
				}
				else if(validate.get(0).getSocietyName().equalsIgnoreCase(jsondata.getString("societyName").toString())) {
					json.put("status", 0);
					json.put("msg", "Society Name Already Exists");
				}
			}
			else {
				String result = md.addSociety(masSociety);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String updateSocietyStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("societyId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String societyStatus = md.updateSocietyStatus(Long.parseLong(statusPayload.get("societyId").toString()),
					statusPayload.get("status").toString());

			if (societyStatus != null && societyStatus.equals("200")) {
				json.put("societyStatus", societyStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateSociety(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {
			Long userId= Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasSociety masSociety=new MasSociety();
			masSociety.setSocietyId(Long.parseLong(jsondata.get("societyId").toString()));
			masSociety.setSocietyCode(jsondata.get("societyCode").toString());
			masSociety.setSocietyName(jsondata.get("societyName").toString());
			masSociety.setLastChgBy(userId);
			masSociety.setStatus("y");
			masSociety.setLastChgDate(date);

			String updateSociety = md.updateSociety(masSociety);
			if (updateSociety != null && updateSociety.equalsIgnoreCase("success")) {
				json.put("updateSociety", updateSociety);
				json.put("msg", "Record Updated Successfully");
				json.put("status", 1);
			} else {
				json.put("msg", "Record Not Updated.");
				json.put("status", 0);

			}

		} else {
			json.put("msg", "Data Not Found");
			json.put("status", 0);

		}


		return json.toString();

	}

	@Override
	public String getCityList(HashMap<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsondata=new JSONObject(payload);
		List totalMatches = new ArrayList();
		List list = new ArrayList();
		List<MasCity> cityList = new ArrayList<MasCity>();
		Map<String,List<MasCity>> map = md.getCityList(jsondata);
		if(map.get("cityList") != null) {
			cityList = map.get("cityList");
			totalMatches = map.get("totalMatches");

			cityList.forEach( cy -> {

				HashMap<String, Object> mapObj = new HashMap<String, Object>();
				if (cy != null ) {
					mapObj.put("cityId", cy.getCityId());
					mapObj.put("cityCode", cy.getCityCode());
					mapObj.put("cityName", cy.getCityName());
					mapObj.put("districtId", cy.getMasDistrict().getDistrictId());
					mapObj.put("status", cy.getStatus());
					list.add(mapObj);
				}
			});
		}
		if ((list != null && list.size() > 0)) {

			jsonObj.put("data", list);
			jsonObj.put("count", list.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", list);
			jsonObj.put("count", list.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	@Override
	public String getSocietyList(HashMap<String, Object> payload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsondata=new JSONObject(payload);
		Map<String,List<MasSociety>> map = md.getSocietyList(jsondata);
		List<MasSociety> societyList=map.get("societyList");
		if ((societyList != null && societyList.size() > 0)) {

			jsonObj.put("data", societyList);
			jsonObj.put("count", societyList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", societyList);
			jsonObj.put("count", societyList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		return jsonObj.toString();
	}

	@Override
	public String addSocietyCity(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonData != null) {
			long userId = Long.parseLong(jsonData.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			if(jsonData.get("societyCityId") != null && !jsonData.get("societyCityId").equals("")){
				Long societyCityMappingId=Long.parseLong(jsonData.get("societyCityId").toString());
				Long societyId=Long.parseLong(jsonData.get("societyId").toString());
				Long cityId=Long.parseLong(jsonData.get("cityId").toString());
				String status=jsonData.get("status").toString();

				String result=md.updateSocietyCity(societyCityMappingId,societyId, cityId, status, userId);

				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Update Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}else {
				SocietyCityMapping societyCityMap =new SocietyCityMapping();
				societyCityMap.setCityId(Long.parseLong(jsonData.getString("cityId")));
				societyCityMap.setSocietyId(Long.parseLong(jsonData.getString("societyId")));
				societyCityMap.setLastChgBy(userId);
				societyCityMap.setStatus("Y");
				societyCityMap.setLastChgDate(date);
				String result = md.addSocietyCity(societyCityMap);

				List<SocietyCityMapping> validate=md.validateSocietyCitymapping(Long.parseLong(jsonData.getString("cityId")), Long.parseLong(jsonData.getString("societyId")));
				if(validate !=null && validate.size()>0) {
					if(validate.get(0).getCityId() == Long.parseLong(jsonData.getString("cityId")) && validate.get(0).getSocietyId() == Long.parseLong(jsonData.getString("societyId"))) {
						json.put("status", 0);
						json.put("msg", "City Name and Society Name Already Mapped");
					}
				}else {
					if(result != null && result.equals("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					}else{
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}
			}
		}else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String getAllCitySociety(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<SocietyCityMapping> societyCityMappinglList = new ArrayList<SocietyCityMapping>();
		List list = new ArrayList();
		if (jsonData != null) {
			Map<String, List<SocietyCityMapping>> societyCity = md.getAllCitySociety(jsonData);
			List totalMatches = new ArrayList();
			if (societyCity.get("societyCityList") != null) {
				societyCityMappinglList = societyCity.get("societyCityList");
				totalMatches = societyCity.get("totalMatches");
				for (SocietyCityMapping societCityMappingl : societyCityMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (societCityMappingl != null) {
						mapObj.put("societyCityId", societCityMappingl.getSocietyCityMappingId());
						mapObj.put("cityId", societCityMappingl.getCityId());
						mapObj.put("cityName", societCityMappingl.getMasCity().getCityName());
						mapObj.put("societyId", societCityMappingl.getSocietyId());
						mapObj.put("societyName", societCityMappingl.getMasSociety().getSocietyName());
						mapObj.put("status", societCityMappingl.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String updateSocietyCityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("societyCityId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String societyStatus = md.updateSocietyCityStatus(Long.parseLong(statusPayload.get("societyCityId").toString()),
					statusPayload.get("status").toString());

			if (societyStatus != null && societyStatus.equals("200")) {
				json.put("societyStatus", societyStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String addFundSchemeMaster(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonData != null) {
			long userId = Long.parseLong(jsonData.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			if(jsonData.get("fundSchemeId") != null && !jsonData.get("fundSchemeId").equals("")){
				Long fundSchemeId=Long.parseLong(jsonData.get("fundSchemeId").toString());
				String fundSchemeCode=jsonData.get("fundSchemeCode").toString();
				String fundSchemeName=jsonData.get("fundSchemeName").toString();
				String status=jsonData.get("status").toString();

				String result=md.upateFundSchemeMaster(fundSchemeId,fundSchemeCode, fundSchemeName, status, userId);

				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Update Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
			}else {
				FundSchemeMaster fundSchemeMaster =new FundSchemeMaster();
				fundSchemeMaster.setFundSchemeCode(jsonData.get("fundSchemeCode").toString());
				fundSchemeMaster.setFundSchemeName(jsonData.getString("fundSchemeName").toString());
				fundSchemeMaster.setLastChgBy(userId);
				fundSchemeMaster.setStatus("Y");
				fundSchemeMaster.setLastChgDate(date);

				List<FundSchemeMaster> validate=md.validateFundSchemeMaster(jsonData.getString("fundSchemeCode").toString(), jsonData.getString("fundSchemeName").toString());
				if(validate !=null && validate.size()>0) {
					if(validate.get(0).getFundSchemeCode().equalsIgnoreCase(jsonData.getString("fundSchemeCode").toString())) {
						json.put("status", 0);
						json.put("msg", "Fund Scheme Code Already Exists");
					}
					else if(validate.get(0).getFundSchemeName().equalsIgnoreCase(jsonData.getString("fundSchemeName").toString())) {
						json.put("status", 0);
						json.put("msg", "Fund Scheme  Name Already Exists");
					}
				}else {
					String result = md.addFundSchemeMaster(fundSchemeMaster);

					if(result != null && result.equals("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					}else{
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					}
				}

			}
		}else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}


		return json.toString();
	}

	@Override
	public String getAllFundScheme(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<FundSchemeMaster> fundSchemeMasterList = new ArrayList<FundSchemeMaster>();
		List list = new ArrayList();
		if (jsonData != null) {
			Map<String, List<FundSchemeMaster>> fundSchemeMaster = md.getAllFundScheme(jsonData);
			List totalMatches = new ArrayList();
			if (fundSchemeMaster.get("fundSchemeMasterList") != null) {
				fundSchemeMasterList = fundSchemeMaster.get("fundSchemeMasterList");
				totalMatches = fundSchemeMaster.get("totalMatches");
				for (FundSchemeMaster fundSchemeMas : fundSchemeMasterList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (fundSchemeMas != null) {
						mapObj.put("fundSchemeId", fundSchemeMas.getFundSchemeId());
						mapObj.put("fundSchemeCode", fundSchemeMas.getFundSchemeCode());
						mapObj.put("fundSchemeName", fundSchemeMas.getFundSchemeName());
						mapObj.put("status", fundSchemeMas.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}


	@Override
	public String updateFundSchemeStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("fundSchemeId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {
			String fundSchemeStatus = md.updateFundSchemeStatus(Long.parseLong(statusPayload.get("fundSchemeId").toString()),
					statusPayload.get("status").toString());

			if (fundSchemeStatus != null && fundSchemeStatus.equals("200")) {
				json.put("societyStatus", fundSchemeStatus);
				json.put("msg", "Status Updated Successfully");
				json.put("status", 1);
			} else {

				json.put("msg", "Status Not Updated");
				json.put("status", 0);
			}

		} else {

			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String getMMUByCityCluster(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<CityMmuMapping> mmuCityMappinglList = new ArrayList<CityMmuMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<CityMmuMapping>> clusterCity = md.getMMUByCityCluster(jsondata);
			List totalMatches = new ArrayList();
			if (clusterCity.get("cityMMUList") != null) {
				mmuCityMappinglList = clusterCity.get("cityMMUList");
				//totalMatches = clusterCity.get("totalMatches");
				for (CityMmuMapping mmuCity : mmuCityMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (clusterCity != null) {
						mapObj.put("cityId", mmuCity.getCityId());
						mapObj.put("cityMMUMappingId", mmuCity.getCityMmuMappingId());
						mapObj.put("mmuId", mmuCity.getMmuId());
						mapObj.put("cityName", mmuCity.getMasCity().getCityName());
						mapObj.put("mmuName", mmuCity.getMasMmu().getMmuName());
						mapObj.put("status", mmuCity.getStatus());

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getMmuByCityMapping(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<CityMmuMapping> cityMmuMappinglList = new ArrayList<CityMmuMapping>();
		List<CityMmuInvoiceMapping> cityMmuInvoiceMappingList = new ArrayList<CityMmuInvoiceMapping>();
		List list = new ArrayList();
		if (jsonData != null) {
			if(jsonData.has("vendorInvoice"))
			{

				Map<String, List<CityMmuInvoiceMapping>> clusterCity = md.getMmuByCityMMUPhaseMapping(jsonData);
				List totalMatches = new ArrayList();
				if (clusterCity.get("cityMmuList") != null) {
					cityMmuInvoiceMappingList = clusterCity.get("cityMmuList");
					totalMatches = clusterCity.get("totalMatches");
					Collections.sort(cityMmuInvoiceMappingList, CityMmuInvoiceMapping.orderNoComparator);
					for (CityMmuInvoiceMapping cityMmuMapping : cityMmuInvoiceMappingList) {
						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (cityMmuMapping != null) {
							mapObj.put("cityId", cityMmuMapping.getCityId());
							mapObj.put("cityMmuMappingId",cityMmuMapping.getCityMmuInvoiceMappingId());
							mapObj.put("mmuId", cityMmuMapping.getMmuId());
							mapObj.put("cityName", cityMmuMapping.getMasCity().getCityName());
							mapObj.put("mmuName", cityMmuMapping.getMasMmu().getMmuName());
							mapObj.put("status", cityMmuMapping.getStatus());
							mapObj.put("regNo", cityMmuMapping.getMasMmu().getMmuNo() !=null ? cityMmuMapping.getMasMmu().getMmuNo() : "");

							list.add(mapObj);
						}
					}

					if (list != null && list.size() > 0) {
						json.put("data", list);
						json.put("count", totalMatches.size());
						json.put("msg", "successfully");
						json.put("status", 1);
					} else {
						json.put("data", list);
						json.put("count", totalMatches.size());
						json.put("msg", "No Record Found");
						json.put("status", 0);
					}

				} else {
					json.put("statut", 0);
					json.put("msg", "No Record Found");
				}
        		}else {

				Map<String, List<CityMmuMapping>> clusterCity = md.getMmuByCityMapping(jsonData);
				List totalMatches = new ArrayList();
				if (clusterCity.get("cityMmuList") != null) {
					cityMmuMappinglList = clusterCity.get("cityMmuList");
					totalMatches = clusterCity.get("totalMatches");
					Collections.sort(cityMmuMappinglList, CityMmuMapping.orderNoComparator);
					for (CityMmuMapping cityMmuMapping : cityMmuMappinglList) {
						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						if (cityMmuMapping != null) {
							mapObj.put("cityId", cityMmuMapping.getCityId());
							mapObj.put("cityMmuMappingId",cityMmuMapping.getCityMmuMappingId() );
							mapObj.put("mmuId", cityMmuMapping.getMmuId());
							mapObj.put("cityName", cityMmuMapping.getMasCity().getCityName());
							mapObj.put("mmuName", cityMmuMapping.getMasMmu().getMmuName());
							mapObj.put("status", cityMmuMapping.getStatus());
							mapObj.put("regNo", cityMmuMapping.getMasMmu().getMmuNo() !=null ? cityMmuMapping.getMasMmu().getMmuNo() : "");

							list.add(mapObj);
						}
					}

					if (list != null && list.size() > 0) {
						json.put("data", list);
						json.put("count", totalMatches.size());
						json.put("msg", "successfully");
						json.put("status", 1);
					} else {
						json.put("data", list);
						json.put("count", totalMatches.size());
						json.put("msg", "No Record Found");
						json.put("status", 0);
					}

				} else {
					json.put("statut", 0);
					json.put("msg", "No Record Found");
				}

			
			}
		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}
   	@Override
	public String getIndendeCityList(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasCity> mascityList = new ArrayList<MasCity>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasCity>> mapMascity = md.getIndendeCityList(jsondata);
			List totalMatches = new ArrayList();
			if (mapMascity.get("mascityList") != null) {
				mascityList = mapMascity.get("mascityList");
				totalMatches = mapMascity.get("totalMatches");
				 {
					 
					 mascityList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("cityId", ct.getCityId());
								mapObj.put("cityCode", ct.getCityCode());
								mapObj.put("cityName", ct.getCityName());
								mapObj.put("districtName", ct.getMasDistrict().getDistrictName());
								mapObj.put("districtId", ct.getMasDistrict().getDistrictId());
								mapObj.put("mmuOperational", ct.getMmuOperational());
								mapObj.put("status", ct.getStatus());
								mapObj.put("indentCity", ct.getIndentCity());
								if(null!=ct.getOrderNo())
								{
									mapObj.put("sequenceNo", ct.getOrderNo());
								}
								else
								{
									mapObj.put("sequenceNo", "");
								}
								list.add(mapObj);
							}	 
					 });
					
				}
				 if (list != null && list.size() > 0) {
						json.put("data", list);
						json.put("count", totalMatches.size());
						json.put("msg", "Get Record successfully");
						json.put("status", 1);
					} else {
						json.put("data", list);
						json.put("count", 0);
						json.put("msg", "No Record Found");
						json.put("status", 0);
					}

				} 

			} 
			return json.toString();
		}

	@Override
	public String addAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthority(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
		
					List<MasAuthority> masAuthorityData = md.getMasAuthorityList();
					if (masAuthorityData.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						json.put("masAuthorityData", masAuthorityData);
						json.put("msg", "authority List  get  sucessfull... ");
						json.put("status", "1");

					}

				
		        return json.toString();
			
		} finally {
			//System.out.println("Exception Occured");
		}
	}

	@Override
	public String updateAuthority(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateCityMMuStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("cityMMUMappingIdV").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String districtStatus = md.updateCityMMUStatus(Long.parseLong(statusPayload.get("cityMMUMappingIdV").toString()),
						statusPayload.get("status").toString(),null,null);

				if (districtStatus != null && districtStatus.equals("200")) {
					json.put("districtStatus", districtStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	
	@Override
	public String updateCityMMUMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
	
			List<CityMmuMapping> validate=md.validateCityMmuMapping(jsonObject.getString("cityId").toString(), jsonObject.getString("mmuId").toString());
			
			if(validate !=null && validate.size()>0) {
				if(validate.get(0).getCityId()== Long.parseLong(jsonObject.getString("cityId").toString())) {
					json.put("status", 0);
					json.put("msg", "MMU  Already Exists");
				}
			}else {
				
			String cityMappingUpdatee = md.updateCityMMUStatus(Long.parseLong(jsonObject.get("cityMMUMappingIdV").toString()),
					jsonObject.get("status").toString(),Long.parseLong(jsonObject.get("mmuId").toString()),Long.parseLong(jsonObject.getString("cityId").toString()));
			
					 
			if (cityMappingUpdatee != null && cityMappingUpdatee.equals("200")) {
					json.put("updateDistrict", cityMappingUpdatee);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}
	@Override
	public String getMmuByDistrictId(JSONObject jsonData, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasMMU> mmuMappinglList = new ArrayList<MasMMU>();
		List list = new ArrayList();
		if (jsonData != null) {
			Map<String, List<MasMMU>> clusterCity = md.getMmuByDistrictId(jsonData);
			List totalMatches = new ArrayList();
			if (clusterCity.get("mmuMmuList") != null) {
				mmuMappinglList = clusterCity.get("mmuMmuList");
				totalMatches = clusterCity.get("totalMatches");
				Collections.sort(mmuMappinglList, MasMMU.orderNoComparator);
				for (MasMMU masMMu : mmuMappinglList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (mmuMappinglList != null) {
						//mapObj.put("cityId", cityMmuMapping.getCityId());
					//	mapObj.put("cityMmuMappingId",cityMmuMapping.getCityMmuMappingId() );
						mapObj.put("mmuId", masMMu.getMmuId()); 
						mapObj.put("mmuName", masMMu.getMmuName());
						 

						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String getStoreFinancialYear(HashMap<String, String> payload, HttpServletRequest request) {
		JSONObject json = new JSONObject();
	
		List<Object> list = new ArrayList<Object>();
		try {
			     List<MasStoreFinancial> MasStoreFinancialData=null;
			       if(payload.containsKey("fyFilter"))
			       {
			    	   List getApprovingData=md.getFinancialYearFilter(payload.get("startDate"));
						try
						{
							String financialId = "";
							String startDate = "";
							String endDate = "";
							String yearDescription = "";
							String markFinancialYear = "";
							
							for (Iterator<?> it = getApprovingData.iterator(); it.hasNext();) {
								Map<String, Object> mapObj = new HashMap<String, Object>();
								Object[] row = (Object[]) it.next();
								if (row[0] != null) {
									financialId = row[0].toString();

								}

								if (row[1] != null) {

									startDate = row[1].toString();

								}

								if (row[2] != null) {

									endDate = row[2].toString();

								}
								if (row[7] != null) {

									yearDescription = row[7].toString();

								}
								if (row[8] != null) {

									markFinancialYear = row[8].toString();

								}
								
								mapObj.put("financialId", financialId);
								mapObj.put("startDate", startDate);
								mapObj.put("endDate", endDate);
								mapObj.put("yearDescription", yearDescription);
								mapObj.put("markFinancialYear", markFinancialYear);
								list.add(mapObj);
							}
							
													
						  
							}catch (Exception e) {
								json.put("msg", "Successfully logged in");
								json.put("status", "1");
								e.printStackTrace();
								// TODO: handle exception
							}
					     		             
			       }else {
					   MasStoreFinancialData = md.getMasStoreFinancial();
			       }
				  if (!payload.containsKey("fyFilter") && MasStoreFinancialData.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						if(payload.containsKey("fyFilter"))
					       {
							json.put("MasStoreFinancialData", list);
					       }else {
					    	   json.put("MasStoreFinancialData", MasStoreFinancialData);
					       }
						
						json.put("msg", "MasStoreFinancialData List  get  sucessfull... ");
						json.put("status", "1");

					}

				
		        return json.toString();
			
		} finally {
			//System.out.println("Exception Occured");
		}
	}

	@Override
	public String getMasHeadType(HashMap<String, String> payload, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
		
					List<MasHeadType> masHeadTypeData = md.getMasHadType();
					if (masHeadTypeData.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						json.put("masHeadTypeData", masHeadTypeData);
						json.put("msg", "authority List  get  sucessfull... ");
						json.put("status", "1");

					}

				
		        return json.toString();
			
		} finally {
			//System.out.println("Exception Occured");
		}
	}
	
	@Override
	public String getAllMasHead(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasHead> masHeadList = new ArrayList<MasHead>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasHead>> mapHead = md.getAllMasHead(jsondata);
			List totalMatches = new ArrayList();
			if (mapHead.get("masHeadList") != null) {
				masHeadList = mapHead.get("masHeadList");
				totalMatches = mapHead.get("totalMatches");
				 {
					 
					 masHeadList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("headTypeId", ct.getHeadTypeId());
								mapObj.put("headTypeCode", ct.getHeadTypeCode());
								mapObj.put("headTypeName", ct.getHeadTypeName());
								 
								mapObj.put("status", ct.getStatus());
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	
		@Override
	public String addHead(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasHead masHead=new MasHead();
			 
			masHead.setHeadTypeCode(jsondata.get("headCode").toString());	
			masHead.setHeadTypeName(jsondata.get("headName").toString());
		  	masHead.setStatus("y");
		  	masHead.setLastChangeDate(date);
		  	masHead.setLastChangeBy(userId);
			List<MasHead> validate=md.validateMasHead(jsondata.getString("headCode").toString(), jsondata.getString("headName").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getHeadTypeCode().equalsIgnoreCase(jsondata.getString("headCode").toString())) {
				json.put("status", 0);
				json.put("msg", "Head Type Code Already Exists");
			}
			else if(validate.get(0).getHeadTypeName().equalsIgnoreCase(jsondata.getString("headName").toString())) {
				json.put("status", 0);
				json.put("msg", "Head Type Name Already Exists");
			}
			}
			else {
			String result = md.addHead(masHead);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
		 
	@Override
	public String updateHeadStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("headId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String headStatus = md.updateHeadStatus(Long.parseLong(statusPayload.get("headId").toString()),
						statusPayload.get("status").toString());

				if (headStatus != null && headStatus.equals("200")) {
					json.put("headStatus", headStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String updateHead(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					MasHead  masHead=new MasHead();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					 
					 
					masHead.setHeadTypeId(Long.parseLong(jsonObject.get("headId").toString()));
					masHead.setHeadTypeCode(jsonObject.get("headCode").toString());	
					masHead.setHeadTypeName(jsonObject.get("headName").toString());
				 		masHead.setLastChangeBy(userId);
					masHead.setStatus("y");
					masHead.setLastChangeDate(date);
					 
					String updateCity = md.updateHead(masHead);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	@Override
	public String addApprovalAuthority(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			ApprovingAuthority approvindAuthority=new ApprovingAuthority();
			 
			approvindAuthority.setApprovingAuthorityCode(jsondata.get("approvingAuthorityCode").toString());	
			approvindAuthority.setApprovingAuthorityName(jsondata.get("approvingAuthorityName").toString());
			if(jsondata.get("levelOfUser")!=null && !jsondata.get("levelOfUser").toString().equalsIgnoreCase("0"))
			approvindAuthority.setLevelOfUser(jsondata.get("levelOfUser").toString());	
			if(jsondata.has("orderNumber") && jsondata.get("orderNumber")!=null && !jsondata.get("orderNumber").toString().equalsIgnoreCase("0"))
			approvindAuthority.setOrderNumber(Long.parseLong(jsondata.get("orderNumber").toString()));
			
			approvindAuthority.setStatus("y");
			approvindAuthority.setLastChangeDate(date);
			approvindAuthority.setLastChangeBy(userId);
			approvindAuthority.setFinalApprovingAuthority(jsondata.get("finalApproval").toString());
			
			List<ApprovingAuthority> validate=md.validateApprovingAuthority(jsondata.getString("approvingAuthorityCode").toString(), jsondata.getString("approvingAuthorityName").toString());
			List<ApprovingAuthority> validateOrder=null;
			if(!jsondata.get("orderNumber").toString().equalsIgnoreCase("0"))
			  validateOrder=md.validateOrderNumber(jsondata.get("orderNumber").toString());
			if(validate !=null && validate.size()>0) {
			if(validate.get(0).getApprovingAuthorityCode().equalsIgnoreCase(jsondata.getString("approvingAuthorityCode").toString())) {
				json.put("status", 0);
				json.put("msg", "Approving Authority Code Already Exists");
			}
			else if(validate.get(0).getApprovingAuthorityName().equalsIgnoreCase(jsondata.getString("approvingAuthorityName").toString())) {
				json.put("status", 0);
				json.put("msg", "Approving Authority Name Already Exists");
			}
			}
			else if(validateOrder!=null && validateOrder.size()>0) {
				json.put("status", 0);
				json.put("msg", "Order No. Already Exist");
			}
			else {
			String result = md.addApprovalAuthority(approvindAuthority);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	
 
	 
	@Override
	public String getAllApprovalAuthority(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<ApprovingAuthority> masApprovindAuthorityList = new ArrayList<ApprovingAuthority>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<ApprovingAuthority>> mapApprovindAuthorityList= md.getAllApprovalAuthority(jsondata);
			List totalMatches = new ArrayList();
			if (mapApprovindAuthorityList.get("masApprovingAuthorityList") != null) {
				masApprovindAuthorityList = mapApprovindAuthorityList.get("masApprovingAuthorityList");
				totalMatches = mapApprovindAuthorityList.get("totalMatches");
				 {
					 
					 masApprovindAuthorityList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("authorityId", ct.getAuthorityId());
								mapObj.put("approvingAuthorityCode", ct.getApprovingAuthorityCode());
								mapObj.put("approvingAuthorityName", ct.getApprovingAuthorityName());
								mapObj.put("levelOfUser", ct.getLevelOfUser());
			 					mapObj.put("orderNumber", ct.getOrderNumber());
								mapObj.put("status", ct.getStatus());
								mapObj.put("finalApproval", ct.getFinalApprovingAuthority());
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	 
	@Override
	public String getAllOrderNumber(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<ApprovingAuthority> approvingAuthorityList = new ArrayList<ApprovingAuthority>();
		List<MasUserType> masUserTypeList = new ArrayList<MasUserType>();
		List list = new ArrayList();
		List list1 = new ArrayList();
		if (jsondata != null) {
			//map.put("approvingAuthorityList", approvingAuthorityList);
			//map.put("masUserTypeList", masUserTypeList);
			
			Map<String,  Object> mapUserType = md.getAllOrderNumber(jsondata);
			List totalMatches = new ArrayList();
			if (mapUserType.get("approvingAuthorityList") != null || (mapUserType.get("masUserTypeList") != null)) {
				approvingAuthorityList = (List<ApprovingAuthority>) mapUserType.get("approvingAuthorityList");
				masUserTypeList = (List<MasUserType>) mapUserType.get("masUserTypeList");
				 {
					 approvingAuthorityList.forEach( ut -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ut != null ) {
								mapObj.put("authorityId", ut.getAuthorityId());
								mapObj.put("approvingAuthorityName", ut.getApprovingAuthorityName());
								 
								list.add(mapObj);
							}	 
					 });
					 masUserTypeList.forEach( ut -> {
						 
						 HashMap<String, Object> mapObj1 = new HashMap<String, Object>();
							if (ut != null ) {
								mapObj1.put("userTypeId", ut.getUserTypeId());
								mapObj1.put("userTypeName", ut.getUserTypeName());
								 
								list1.add(mapObj1);
							}	 
					 });
					
					 
				}

				if (list != null && list.size() > 0 ) {
					json.put("data", list);
					json.put("data1", list1);
					 
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("data1", list1);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	
	
	@Override
	public String updateApprovalAuthorityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("authorityid").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String headStatus = md.updateApprovalAuthorityStatus(Long.parseLong(statusPayload.get("authorityid").toString()),
						statusPayload.get("status").toString());

				if (headStatus != null && headStatus.equals("200")) {
					json.put("headStatus", headStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	 
	@Override
	public String updateApprovalAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					ApprovingAuthority  approvingAuthority=new ApprovingAuthority();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					 
					 
					approvingAuthority.setAuthorityId(Long.parseLong(jsonObject.get("authorityid").toString()));
					approvingAuthority.setApprovingAuthorityCode(jsonObject.get("approvingAuthorityCode").toString());	
					approvingAuthority.setApprovingAuthorityName(jsonObject.get("approvingAuthorityName").toString());
					
					if(jsonObject.get("levelOfUser")!=null && !jsonObject.get("levelOfUser").toString().equalsIgnoreCase("0"))
					approvingAuthority.setLevelOfUser(jsonObject.get("levelOfUser").toString());	
					if(jsonObject.get("orderNumber")!=null && !jsonObject.get("orderNumber").toString().equalsIgnoreCase("0"))
					approvingAuthority.setOrderNumber(Long.parseLong(jsonObject.get("orderNumber").toString()));
				
					
					approvingAuthority.setLastChangeBy(userId);
					approvingAuthority.setStatus("y");
					approvingAuthority.setLastChangeDate(date);
					approvingAuthority.setFinalApprovingAuthority(jsonObject.get("finalApproval").toString());
					String updateCity = md.updateApprovingAuthority(approvingAuthority);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	
	@Override
	public String checkFinalApproval(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
			String result = md.checkFinalApproval(jsondata);
			if(result !=null && result.equals("exists")) {
				json.put("msg", "Final Approving Already Exists");
				json.put("status", 1);
			}else {
				json.put("msg", "Reg. Not   Exists");
				json.put("status", 0);
			}
			
		return json.toString();
	}
	

	@Override
	public String addApprovalAuthorityMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			ApprovingMapping approvingMapping=new ApprovingMapping();
			 
			 
			if(jsondata.get("approvindAutorityNameList")!=null && !jsondata.get("approvindAutorityNameList").toString().equalsIgnoreCase("") 
					&& !jsondata.get("approvindAutorityNameList").toString().equalsIgnoreCase("0"))
				approvingMapping.setAuthorityId(Long.parseLong(jsondata.get("approvindAutorityNameList").toString()));	
			if(jsondata.has("userTypeList") && jsondata.get("userTypeList")!=null && !jsondata.get("userTypeList").toString().equalsIgnoreCase("0")
					&& !jsondata.get("userTypeList").toString().equalsIgnoreCase(""))
				approvingMapping.setUserTypeId(Long.parseLong(jsondata.get("userTypeList").toString()));
			
			approvingMapping.setStatus("y");
			 
			 
			
			List<ApprovingMapping> validate=md.validateApprovingMapping(Long.parseLong(jsondata.getString("approvindAutorityNameList").toString()), 
					Long.parseLong(jsondata.getString("userTypeList").toString()));
			 
			if(validate !=null && validate.size()>0) {
			 
				json.put("status", 0);
				json.put("msg", "Approving Authority   Already Exists");
			 
			 
			}
			 
			else {
			String result = md.addApprovalAuthorityMapping(approvingMapping);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	 
	@Override
	public String getAllApprovalAuthorityMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<ApprovingMapping> masApprovindAuthorityList = new ArrayList<ApprovingMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<ApprovingMapping>> mapApprovindAuthorityList= md.getAllApprovalAuthorityMapping(jsondata);
			List totalMatches = new ArrayList();
			if (mapApprovindAuthorityList.get("masApprovingAuthorityList") != null) {
				masApprovindAuthorityList = mapApprovindAuthorityList.get("masApprovingAuthorityList");
				totalMatches = mapApprovindAuthorityList.get("totalMatches");
				 {
					 
					 masApprovindAuthorityList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("authorityMappingId", ct.getAuthorityMappingId());
								 
								mapObj.put("approvingAuthorityName", ct.getApprovingAuthority().getApprovingAuthorityName());
								mapObj.put("userTypeName", ct.getMasUserType().getUserTypeName());
								mapObj.put("userTypeId", ct.getMasUserType().getUserTypeId());
								mapObj.put("approvingAuthorityId", ct.getApprovingAuthority().getAuthorityId());
								mapObj.put("status", ct.getStatus());
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	 
	@Override
	public String updateApprovalAuthorityMapping(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					ApprovingMapping  approvingAuthority=new ApprovingMapping();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					 
					 
					approvingAuthority.setAuthorityMappingId(Long.parseLong(jsonObject.get("authoritymappingid").toString()));
					 
					
					if(jsonObject.get("approvindAutorityNameList")!=null && !jsonObject.get("approvindAutorityNameList").toString().equalsIgnoreCase(""))
						approvingAuthority.setAuthorityId(Long.parseLong(jsonObject.get("approvindAutorityNameList").toString()));	
					if(jsonObject.get("userTypeList")!=null && !jsonObject.get("userTypeList").toString().equalsIgnoreCase(""))
					approvingAuthority.setUserTypeId(Long.parseLong(jsonObject.get("userTypeList").toString()));
				 	approvingAuthority.setStatus("y");
					 
					String updateCity = md.updateApprovingAuthorityMapping(approvingAuthority);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}

	@Override
	public String updateApprovalAuthorityMappingStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("authoritymappingid").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String headStatus = md.updateApprovalAuthorityMappingStatus(Long.parseLong(statusPayload.get("authoritymappingid").toString()),
						statusPayload.get("status").toString());

				if (headStatus != null && headStatus.equals("200")) {
					json.put("headStatus", headStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	 
	@Override
	public String getAllFinancialYear(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreFinancial> masMasStoreFinancialList = new ArrayList<MasStoreFinancial>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasStoreFinancial>> mapApprovindAuthorityList= md.getAllFinancialYear(jsondata);
			List totalMatches = new ArrayList();
			if (mapApprovindAuthorityList.get("masFinancialYearList") != null) {
				masMasStoreFinancialList = mapApprovindAuthorityList.get("masFinancialYearList");
				totalMatches = mapApprovindAuthorityList.get("totalMatches");
				 {
					 
					 masMasStoreFinancialList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("financialId", ct.getFinancialId());
								 
								mapObj.put("financialYear", ct.getFinancialYear());
								 String lastChangeDate = "";
								
								//Date s =  HMSUtil.convertStringDateToUtilDate(row[2].toString(), "yyyy-MM-dd");
						     	lastChangeDate = HMSUtil.convertDateToStringFormat(
						    		ct.getStartDate(), "dd/MM/yyyy");
						     	mapObj.put("startDate",lastChangeDate);
						     	 String endChangeDate = "";
						     	endChangeDate = HMSUtil.convertDateToStringFormat(
							    		ct.getEndDate(), "dd/MM/yyyy");
						     	
								mapObj.put("endDate",endChangeDate);
								 
								mapObj.put("status", ct.getStatus());
								if(ct.getMarkFinancialYear()!=null)
								mapObj.put("markFinancialYear", ct.getMarkFinancialYear());
								else
									mapObj.put("markFinancialYear", "");
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}

	 
	@Override
	public String addFinancialYear(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			MasStoreFinancial masStoreFinancial=new MasStoreFinancial();
			 
			 
			if(jsondata.get("financialYearDate")!=null && !jsondata.get("financialYearDate").toString().equalsIgnoreCase("") 
					&& !jsondata.get("financialYearDate").toString().equalsIgnoreCase("0")) {
				masStoreFinancial.setFinancialYear(jsondata.get("financialYearDate").toString());	
				masStoreFinancial.setYearDescription(jsondata.get("financialYearDate").toString());
			}
			if(jsondata.has("effectiveStartDate") && jsondata.get("effectiveStartDate")!=null && !jsondata.get("effectiveStartDate").toString().equalsIgnoreCase("0")
					&& !jsondata.get("effectiveStartDate").toString().equalsIgnoreCase(""))
				masStoreFinancial.setStartDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("effectiveStartDate").toString()));
			
			if(jsondata.has("effectiveEndDate") && jsondata.get("effectiveEndDate")!=null && !jsondata.get("effectiveEndDate").toString().equalsIgnoreCase("0")
					&& !jsondata.get("effectiveEndDate").toString().equalsIgnoreCase(""))
				masStoreFinancial.setEndDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("effectiveEndDate").toString()));
			
			
			masStoreFinancial.setStatus("y");
			masStoreFinancial.setMarkFinancialYear(jsondata.get("markFinancialYear").toString().trim());
			masStoreFinancial.setLastChgDate(date);
			Users user = new Users();
			user.setUserId(userId);
			masStoreFinancial.setLastChgBy(user);
			
			List<MasStoreFinancial> validate=md.validateFinancialYear(jsondata.getString("financialYearDate").toString());
					  
			 
			if(validate !=null && validate.size()>0) {
			 
				json.put("status", 0);
				json.put("msg", "Financial Year already exists");
			 
			 
			}
			 
			else {
			String result = md.addFinancialYear(masStoreFinancial);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	
	@Override
	public String updateFinancialYear(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {				
					MasStoreFinancial  masStoreFinancial=new MasStoreFinancial();					
					Long userId= Long.parseLong(jsondata.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					 
					 
					masStoreFinancial.setFinancialId(Long.parseLong(jsondata.get("financialid").toString()));
					 
					if(jsondata.get("financialYearDate")!=null && !jsondata.get("financialYearDate").toString().equalsIgnoreCase("") 
							&& !jsondata.get("financialYearDate").toString().equalsIgnoreCase("0")) {
						masStoreFinancial.setFinancialYear(jsondata.get("financialYearDate").toString());	
						masStoreFinancial.setYearDescription(jsondata.get("financialYearDate").toString());
					}
					if(jsondata.has("effectiveStartDate") && jsondata.get("effectiveStartDate")!=null && !jsondata.get("effectiveStartDate").toString().equalsIgnoreCase("0")
							&& !jsondata.get("effectiveStartDate").toString().equalsIgnoreCase(""))
						masStoreFinancial.setStartDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("effectiveStartDate").toString()));
					
					if(jsondata.has("effectiveEndDate") && jsondata.get("effectiveEndDate")!=null && !jsondata.get("effectiveEndDate").toString().equalsIgnoreCase("0")
							&& !jsondata.get("effectiveEndDate").toString().equalsIgnoreCase(""))
						masStoreFinancial.setEndDate(HMSUtil.convertStringDateToUtilDateForDatabase(jsondata.get("effectiveEndDate").toString()));
		
					masStoreFinancial.setMarkFinancialYear(jsondata.get("markFinancialYear").toString().trim());
					masStoreFinancial.setLastChgDate(date);
					Users user = new Users();
					user.setUserId(userId);
					masStoreFinancial.setLastChgBy(user);
					masStoreFinancial.setStatus("y");
					 
					String updateCity = md.updateFinancialYear(masStoreFinancial);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}

	@Override
	public String updateFinancialYearStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("financialid").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String headStatus = md.updateFinancialYearStatus(Long.parseLong(statusPayload.get("financialid").toString()),
						statusPayload.get("status").toString());

				if (headStatus != null && headStatus.equals("200")) {
					json.put("headStatus", headStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String checkFinancialYear(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		
			String result = md.checkFinancialYear(jsondata);
			String[] resultaa=null;
			if(result!="" && result.contains("##")) {
				resultaa=result.split("##");
			}
			if(resultaa!=null && resultaa[0].equals("exists")) {
				json.put("msg", "Financial Year Already Exists");
				json.put("status", 1);
				json.put("financialYear", resultaa[1]);
				
			}else {
				json.put("msg", "Reg. Not   Exists");
				json.put("status", 0);
				json.put("financialYear", "");
			}
			
		return json.toString();
	}	
	@Override
	public String addPenalityApprovalAuthority(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			 
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			PenaltyAuthorityConfig penaltyAuthorityConfig=new PenaltyAuthorityConfig();
			
			if(jsondata.get("upssNameData")!=null && !jsondata.get("upssNameData").toString().equalsIgnoreCase("0"))
			penaltyAuthorityConfig.setUppsId(Long.parseLong(jsondata.get("upssNameData").toString()));	
			
			if(jsondata.get("approvingAuthorityName")!=null && !jsondata.get("approvingAuthorityName").toString().equalsIgnoreCase("0"))
				penaltyAuthorityConfig.setAuthorityId(Long.parseLong(jsondata.get("approvingAuthorityName").toString()));	
			penaltyAuthorityConfig.setStatus("y");
			penaltyAuthorityConfig.setLastChangeDate(date);
			penaltyAuthorityConfig.setLastChangeBy(userId);
			 
			
			List<PenaltyAuthorityConfig> validate=md.validatePenaltyAuthorityConfig(jsondata.getString("upssNameData").toString(), jsondata.getString("approvingAuthorityName").toString());
			List<ApprovingAuthority> validateOrder=null;
			 
			if(validate !=null && validate.size()>0) {
			  	json.put("status", 0);
				json.put("msg", "UPSS- approving authority mapping already exists");
			
			}
			 
			else {
			String result = md.addPenalityApprovalAuthority(penaltyAuthorityConfig);
				if (result != null && result.equals("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Added");
				}
		}
		}	
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}
		

		return json.toString();
	}
	

	 
	@Override
	public String getAllPenalityApprovalAuthority(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<PenaltyAuthorityConfig> penaltyAuthorityConfigList = new ArrayList<PenaltyAuthorityConfig>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<PenaltyAuthorityConfig>> mapPenaltyAuthorityConfig= md.getAllPenalityApprovalAuthority(jsondata);
			List totalMatches = new ArrayList();
			if (mapPenaltyAuthorityConfig.get("masPenalityApprovingAuthorityList") != null) {
				penaltyAuthorityConfigList = mapPenaltyAuthorityConfig.get("masPenalityApprovingAuthorityList");
				totalMatches = mapPenaltyAuthorityConfig.get("totalMatches");
				 {
					 
					 penaltyAuthorityConfigList.forEach( ct -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (ct != null ) {
								mapObj.put("authorityConfigId", ct.getAuthorityConfigId());
								
								if(ct.getMasDistrict()!=null && ct.getMasDistrict().getDistrictId()!=0)
								mapObj.put("uppsId", ct.getMasDistrict().getDistrictId());
								else
									mapObj.put("uppsId", "");
								
								if(ct.getMasDistrict()!=null && ct.getMasDistrict().getUpss()!=null)
									mapObj.put("uppsName", ct.getMasDistrict().getUpss());
									else
										mapObj.put("uppsName", "");
									
								
								if(ct.getApprovingAuthority()!=null && ct.getApprovingAuthority().getAuthorityId()!=null)
									mapObj.put("authorityId", ct.getApprovingAuthority().getAuthorityId());
									else
										mapObj.put("authorityId", "");
									
								if(ct.getApprovingAuthority()!=null && ct.getApprovingAuthority().getApprovingAuthorityName()!=null)
									mapObj.put("authorityName", ct.getApprovingAuthority().getApprovingAuthorityName());
									else
										mapObj.put("authorityName", "");
								 mapObj.put("status", ct.getStatus());
								 
							 	list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	

	 
	@Override
	public String updatePenalityApprovalAuthority(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					PenaltyAuthorityConfig  penaltyAuthorityConfig=new PenaltyAuthorityConfig();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					Timestamp date = new Timestamp(d);
					 
					 
					penaltyAuthorityConfig.setAuthorityConfigId(Long.parseLong(jsonObject.get("authorityConfigid").toString()));
					//approvingAuthority.setApprovingAuthorityCode(jsonObject.get("approvingAuthorityCode").toString());	
					//approvingAuthority.setApprovingAuthorityName(jsonObject.get("approvingAuthorityName").toString());
					
					if(jsonObject.get("upssNameData")!=null && !jsonObject.get("upssNameData").toString().equalsIgnoreCase("0"))
						penaltyAuthorityConfig.setUppsId(Long.parseLong( jsonObject.get("upssNameData").toString()));	
					if(jsonObject.get("approvingAuthorityName")!=null && !jsonObject.get("approvingAuthorityName").toString().equalsIgnoreCase("0"))
						penaltyAuthorityConfig.setAuthorityId(Long.parseLong(jsonObject.get("approvingAuthorityName").toString()));
				
					
					penaltyAuthorityConfig.setLastChangeBy(userId);
					penaltyAuthorityConfig.setStatus("y");
					penaltyAuthorityConfig.setLastChangeDate(date);
					 
					String updateCity = md.updatePenalityApprovingAuthority(penaltyAuthorityConfig);
				if (updateCity != null && updateCity.equalsIgnoreCase("success")) {
					json.put("updateCity", updateCity);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			  
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}

	@Override
	public String updatePenalityApprovalAuthorityStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("authorityConfigid").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String headStatus = md.updatePenalityApprovalAuthorityStatus(Long.parseLong(statusPayload.get("authorityConfigid").toString()),
						statusPayload.get("status").toString());

				if (headStatus != null && headStatus.equals("200")) {
					json.put("headStatus", headStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	
	@Override
	public String getAllMasStoreSupplier(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasStoreSupplier> masmmutypeList = new ArrayList<MasStoreSupplier>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasStoreSupplier>> mapMMUType = md.getAllMasStoreSupplier(jsondata);
			List totalMatches = new ArrayList();
			if (mapMMUType.get("masStoreSupplierList") != null) {
				masmmutypeList = mapMMUType.get("masStoreSupplierList");
				totalMatches = mapMMUType.get("totalMatches");
				 {
					 masmmutypeList.forEach( type -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (type != null ) {
								mapObj.put("mmuTypeId", type.getSupplierId());
								mapObj.put("mmuTypeCode", type.getSupplierCode());
								mapObj.put("mmuTypeName", type.getSupplierName());
								mapObj.put("status", type.getStatus());
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}
	

	 
	@Override
	public String addMMUManufac(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			UpssManufacturerMapping mmu=new UpssManufacturerMapping();
			
			mmu.setSupplierId(Long.parseLong(jsondata.get("itemId").toString()));
			mmu.setDistrictId(Long.parseLong(jsondata.get("districtId").toString()));
			mmu.setLastChgBy(userId);
			mmu.setStatus("y");
			mmu.setLastChgDate(date);
			
			List<UpssManufacturerMapping> masCmd = md.validateUpssManufacturerMapping(mmu.getSupplierId(),
					mmu.getDistrictId());
			String result =null;
			if (masCmd.size() != 0) {
				if (masCmd != null && masCmd.size() > 0) {
					//if (masCmd.get(0).getCommandCode().equalsIgnoreCase(json.get("commandCode").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Record  already Exists\"}";
					//}
					 				}
			} else {
			  result = md.addMMUManufac(mmu);
			}
			if (result != null && result.equals("200")) {
				json.put("status", 1);
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}

		return json.toString();
	}

	 
	@Override
	public String updateUpssManu(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
					UpssManufacturerMapping masMMU=new UpssManufacturerMapping();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					
					Timestamp date = new Timestamp(d);
					masMMU.setUpssManuId(Long.parseLong(jsonObject.get("upssManuId").toString()));
					masMMU.setSupplierId(Long.parseLong(jsonObject.get("itemId").toString()));
				 			
					masMMU.setStatus(jsonObject.get("status").toString());
					masMMU.setLastChgBy(userId);
					masMMU.setLastChgDate(date);
					
				  	if(jsonObject.has("districtId") && !jsonObject.get("districtId").toString().equalsIgnoreCase("") 
							&& !jsonObject.get("districtId").toString().equalsIgnoreCase("0"))
						masMMU.setDistrictId(jsonObject.getLong("districtId"));
				
					
				String updateMMU = md.updateUppsManu(masMMU);
				if (updateMMU != null && updateMMU.equalsIgnoreCase("success")) {
					json.put("updateMMU", updateMMU);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}
	

	 
	@Override
	public String updateUpssManuStatus(HashMap<String, Object> statusPayload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("upssManuId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String mmuStatus = md.updateUpssManuStatus(Long.parseLong(statusPayload.get("upssManuId").toString()),
						statusPayload.get("status").toString() );

				if (mmuStatus != null && mmuStatus.equals("200")) {
					json.put("mmuStatus", mmuStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}
	

	 
	@Override
	public String getAllUpssManu(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<UpssManufacturerMapping> mmuList = new ArrayList<UpssManufacturerMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<UpssManufacturerMapping>> mapMasmmu = md.getAllUpssManu(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasmmu.get("mmuList") != null) {
				mmuList = mapMasmmu.get("mmuList");
				totalMatches = mapMasmmu.get("totalMatches");
				 {
					 mmuList.forEach( mmu -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (mmu != null ) {
								mapObj.put("upssManuId", mmu.getUpssManuId());
								mapObj.put("itemId", mmu.getSupplierId());
								mapObj.put("districtName", mmu.getMasDistrict().getDistrictName());
								mapObj.put("itemName", mmu.getMasStoreSupplier().getSupplierName());
								mapObj.put("status", mmu.getStatus());
								if(mmu.getDistrictId()!=null)
								mapObj.put("districtId", mmu.getDistrictId());
								else
									mapObj.put("districtId","");
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}

	@Override
	public String getMasPhase(JSONObject jsonObj, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasPhase> rankList = new ArrayList<MasPhase>();
		List list = new ArrayList();

		if (jsonObj != null) {
			Map<String, List<MasPhase>> masPhase = md.getMasPhase(jsonObj);
			List totalMatches = new ArrayList();
			if (masPhase.get("masPhaseList") != null) {
				rankList = masPhase.get("masPhaseList");
				totalMatches = masPhase.get("totalMatches");
				for (MasPhase rank : rankList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();

					mapObj.put("phaseId", rank.getPhaseId());
					mapObj.put("phaseValue", rank.getPhaseValue());
					mapObj.put("phaseName", rank.getPhaseName());
					mapObj.put("status", rank.getStatus());
					
					list.add(mapObj);
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} else {
				json.put("statut", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addUpssPhaseMapping(JSONObject jsondata, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsondata != null) {			
			long userId = Long.parseLong(jsondata.get("userId").toString());
			long d = System.currentTimeMillis();
			Timestamp date = new Timestamp(d);
			UpssPhaseMapping mmu=new UpssPhaseMapping();
			
			mmu.setPhaseId(Long.parseLong(jsondata.get("phaseId").toString()));
			mmu.setUpssId(Long.parseLong(jsondata.get("districtId").toString()));
			mmu.setLastChgBy(userId);
			mmu.setStatus("y");
			mmu.setLastChgDate(date);
			
			List<UpssPhaseMapping> masCmd = md.validateUpssPhaseMapping(mmu.getPhaseId(),
					mmu.getUpssId());
			String result =null;
			if (masCmd.size() != 0) {
				if (masCmd != null && masCmd.size() > 0) {
					//if (masCmd.get(0).getCommandCode().equalsIgnoreCase(json.get("commandCode").toString())) {

						return "{\"status\":\"2\",\"msg\":\"Record  already Exists\"}";
					//}
					 				}
			} else {
			  result = md.addUpssPhaseMapping(mmu);
			}
			if (result != null && result.equals("200")) {
				json.put("status", 1);
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", 0);
				json.put("msg", "Record Not Added");
			}
		}
		else {
			json.put("status", 0);
			json.put("msg", "Record JSON Data");
		}

		return json.toString();
	}

	@Override
	public String getAllUpssPhaseMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<UpssPhaseMapping> mmuList = new ArrayList<UpssPhaseMapping>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<UpssPhaseMapping>> mapMasmmu = md.getAllUpssPhaseMapping(jsondata);
			List totalMatches = new ArrayList();
			if (mapMasmmu.get("mmuList") != null) {
				mmuList = mapMasmmu.get("mmuList");
				totalMatches = mapMasmmu.get("totalMatches");
				 {
					 mmuList.forEach( mmu -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (mmu != null ) {
								mapObj.put("upssPhaseId", mmu.getUpssPhaseId());
								mapObj.put("phaseId", mmu.getPhaseId());
								mapObj.put("districtName", mmu.getMasDistrict().getUpss());
								mapObj.put("phaseName", mmu.getMasPhase().getPhaseName());
								mapObj.put("phaseValue", mmu.getMasPhase().getPhaseValue());
								mapObj.put("status", mmu.getStatus());
								if(mmu.getUpssId()!=null)
								mapObj.put("districtId", mmu.getUpssId());
								else
									mapObj.put("districtId","");
								list.add(mapObj);
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}

	@Override
	public String updateUpssPhaseStatus(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("upssPhaseId").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String mmuStatus = md.updateUpssPhaseStatus(Long.parseLong(statusPayload.get("upssPhaseId").toString()),
						statusPayload.get("status").toString() );

				if (mmuStatus != null && mmuStatus.equals("200")) {
					json.put("mmuStatus", mmuStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateUpssPhase(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {				
			        UpssPhaseMapping masMMU=new UpssPhaseMapping();					
					Long userId= Long.parseLong(jsonObject.get("userId").toString());
					long d = System.currentTimeMillis();
					
					Timestamp date = new Timestamp(d);
					masMMU.setUpssPhaseId(Long.parseLong(jsonObject.get("upssPhaseId").toString()));
					masMMU.setPhaseId(Long.parseLong(jsonObject.get("phaseId").toString()));
				 			
					masMMU.setStatus(jsonObject.get("status").toString());
					masMMU.setLastChgBy(userId);
					masMMU.setLastChgDate(date);
					
				  	if(jsonObject.has("districtId") && !jsonObject.get("districtId").toString().equalsIgnoreCase("") 
							&& !jsonObject.get("districtId").toString().equalsIgnoreCase("0"))
						masMMU.setUpssId(jsonObject.getLong("districtId"));
				
					
				String updateMMU = md.updateUppsPhase(masMMU);
				if (updateMMU != null && updateMMU.equalsIgnoreCase("success")) {
					json.put("updateMMU", updateMMU);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString();
	}

	@Override
	public String getStoreFutureFinancialYear(HashMap<String, String> payload, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		try {
		
					List<MasStoreFinancial> MasStoreFinancialData = md.getMasStoreFutureFinancial();
					if (MasStoreFinancialData.size() == 0) {
						return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
					} else {
						json.put("MasStoreFinancialData", MasStoreFinancialData);
						json.put("msg", "MasStoreFinancialData List  get  sucessfull... ");
						json.put("status", "1");

					}

				
		        return json.toString();
			
		} finally {
			//System.out.println("Exception Occured");
		}
	}

	@Override
	public String getAllUpssManufactureMapping(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Long districtId=0l;
		List<UpssManufacturerMapping> mmuList = new ArrayList<UpssManufacturerMapping>();
		Long supplierTypId=Long.parseLong(jsondata.get("id").toString());
		List list = new ArrayList();
		if (jsondata != null) {
			if(jsondata.get("levelOfUsers").equals("M"))
			{
				districtId=md.getMMUDistrictId(Long.parseLong(jsondata.get("mmuId").toString()));	
			}
			if(jsondata.get("levelOfUsers").equals("C"))
			{
				districtId=md.getCityDistrictId(Long.parseLong(jsondata.get("cityId").toString()));	
			}
			Map<String, List<UpssManufacturerMapping>> mapMasmmu = md.getAllUpssManufactureMapping(jsondata,districtId);
			List totalMatches = new ArrayList();
			if (mapMasmmu.get("mmuList") != null) {
				mmuList = mapMasmmu.get("mmuList");
				totalMatches = mapMasmmu.get("totalMatches");
				 {
					 mmuList.forEach( mmu -> {
						 
						 HashMap<String, Object> mapObj = new HashMap<String, Object>();
							if (mmu != null ) {
								mapObj.put("upssManuId", mmu.getUpssManuId());
								mapObj.put("supplierId", mmu.getSupplierId());
								mapObj.put("districtName", mmu.getMasDistrict().getUpss());
								mapObj.put("supplierName", mmu.getMasStoreSupplier().getSupplierName());
								mapObj.put("status", mmu.getStatus());
								if(supplierTypId==mmu.getMasStoreSupplier().getSupplierTypeId())
								{
										
								list.add(mapObj);
								}
							}	 
					 });
					
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			} 

		} 
		return json.toString();
	}

	@Override
	public String addCityMmuPhaseMapping(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		try{
			if (jsondata != null) {
				String mmus = jsondata.getString("mmus");
				long cityId = jsondata.getLong("cityId");
				long phaseId = jsondata.getLong("phaseId");
				//String status = jsondata.getString("status");
				String msg="";
				String result ="";
				Integer counter=0;
				Integer duplicatCounter=0;
				for(String mmu: mmus.split(",")){
					List<CityMmuInvoiceMapping> validate= md.validateCityMmuInvoiceMapping(jsondata.getString("cityId").toString(), mmu.trim().toString(),jsondata.getString("phaseId").toString());
				     if(validate!=null && validate.size()>0) {
				    	 duplicatCounter++;
				     }
				    else{
				    CityMmuInvoiceMapping cityMmuMapping = new CityMmuInvoiceMapping();
					cityMmuMapping.setCityId(cityId);
					cityMmuMapping.setPhaseId(phaseId);
					cityMmuMapping.setStatus("A");
					cityMmuMapping.setLastChangeBy(jsondata.getLong("lastChangeBy"));
					cityMmuMapping.setMmuId(Long.parseLong(mmu));
					cityMmuMapping.setLastChangeDate(new Date());
					result = md.createRecord(cityMmuMapping);
					counter++;
					}
					if (result != null && result.equals("200") && counter>0 && duplicatCounter==0) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully!");
					} 
					else if(duplicatCounter >0 && counter==0) {
						json.put("status", 1);
						json.put("msg", "Record already Exist!");
					}
					else if(duplicatCounter >0 && counter>0) {
						json.put("status", 1);
						json.put("msg", "Some Record Added and Some Record already Exist!");
					}
					else {
						json.put("status", 0);
						json.put("msg", "Record Not Added!");
					}
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Payload!");
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String getAllCityMmuPhaseMapping(JSONObject jsondata) {
		JSONObject json = new JSONObject();
		//List<CityMmuMapping> cityMmuList = new ArrayList<>();
		List<Object[]>listObject=null;
		List list = new ArrayList();
		if (jsondata != null) {
			Integer cityMapPage=0;
			if(!jsondata.has("cityMmuPage")) {
				cityMapPage	=1;
			}
			Map<String, List<Object[]>> cityMmuMappingPhaseMap = md.getAllCityMmuPhaseMapping(jsondata);
			List totalMatches = new ArrayList();
			if (cityMmuMappingPhaseMap.get("cityMmuMappingList") != null) {
				listObject = cityMmuMappingPhaseMap.get("cityMmuMappingList");
				totalMatches = cityMmuMappingPhaseMap.get("totalMatches");
				Integer count=0;   
				if(listObject!=null && listObject.size()>0) {
  					 
				    	 for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
				    		 HashMap<String, Object> mapObj = new HashMap<String, Object>();
				    		 Object[] row = (Object[]) it.next();
 								if(row[1]!= null) {
									if(cityMapPage==1) {
										Users user = (Users) md.read(Users.class, Long.parseLong(row[1].toString()));
										mapObj.put("lastChangeByName", user.getUserName());
										}
										}
								    if(row[0]!=null)
									mapObj.put("cityMmuInvoiceMappingId", Long.parseLong(row[0].toString()));
								    else
								    	mapObj.put("cityMmuInvoiceMappingId","");
								    if(row[3]!=null)	
									mapObj.put("cityId", Long.parseLong(row[3].toString()));
								    else
								    	mapObj.put("cityId","");
								    if(row[4]!=null) {
								    	mapObj.put("cityName",row[4].toString());
								    }
								    else
								    	mapObj.put("cityName","");
								    if(row[5]!=null) {
								    	mapObj.put("mmuId", Long.parseLong(row[5].toString()));	
								    }
								    else
								    	mapObj.put("mmuId", "");
									 
								    if(row[6]!=null) {
								    	mapObj.put("mmuName",  row[6].toString());	
								    }
								    else
								    	mapObj.put("mmuName", "");
									
								    if(row[7]!=null) {
								    	mapObj.put("status",  row[7].toString());	
								    }
								    else
								    	mapObj.put("status", "");
									  
								    if(row[7]!=null) {
								    	mapObj.put("status",  row[7].toString());	
								    }
								    else
								    	mapObj.put("status", "");
								    
								    if(row[8]!=null) {
								    	mapObj.put("phaseId",  row[8].toString());	
								    }
								    else
								    	mapObj.put("phaseId", "");
								    
								    if(row[9]!=null) {
								    	mapObj.put("phaseName",  row[9].toString());	
								    }
								    else
								    	mapObj.put("phaseName", "");
									  
								    if(row[1]!=null) {
								    	mapObj.put("lastChangeById",  Long.parseLong(row[1].toString()));	
								    }
								    else
								    	mapObj.put("lastChangeById", "");
									  
									 
								    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
								    String lastChangeDate = "";
								    if(row[2]!=null) {
								   
								    	Date s =  HMSUtil.convertStringDateToUtilDate(row[2].toString(), "yyyy-MM-dd");
								     	lastChangeDate = HMSUtil.convertDateToStringFormat(
								    		s, "dd/MM/yyyy");
								    
								    	mapObj.put("lastChangeDate",   lastChangeDate);
								    
								    }
								    	else
								    	mapObj.put("lastChangeDate","");
								    list.add(mapObj);
								    count++;
								    if(count==5)
										break;
						}
				     }
				 

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Get Record successfully");
					json.put("status", 1);
				} else {
					json.put("data", list);
					json.put("count", 0);
					json.put("msg", "No Record Found");
					json.put("status", 0);
				}

			}

		}
		return json.toString();
	}

	@Override
	public String updateCityMMuStatusPhaseMapping(HashMap<String, Object> statusPayload, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (statusPayload.get("cityMmuInvoiceMappingIdV").toString() != null && !statusPayload.get("status").toString().equalsIgnoreCase("")) {			
				String districtStatus = md.updateCityMMUPhaseStatus(Long.parseLong(statusPayload.get("cityMmuInvoiceMappingIdV").toString()),
						statusPayload.get("status").toString(),null,null,null);

				if (districtStatus != null && districtStatus.equals("200")) {
					json.put("districtStatus", districtStatus);
					json.put("msg", "Status Updated Successfully");
					json.put("status", 1);
				} else {
					
					json.put("msg", "Status Not Updated");
					json.put("status", 0);
				}
			
		} else {
			
			return "{\"status\":\"0\",\"msg\":\"id is not available\"}";
		}

		return json.toString();
	}

	@Override
	public String updateCityMMUPhaseMapping(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
	
			List<CityMmuInvoiceMapping> validate=md.validateCityMmuInvoiceMapping(jsonObject.getString("cityId").toString(), jsonObject.getString("mmuId").toString(),jsonObject.getString("phaseId").toString());
			
			if(validate !=null && validate.size()>0) {
				if(validate.get(0).getCityId()== Long.parseLong(jsonObject.getString("cityId").toString())) {
					json.put("status", 0);
					json.put("msg", "MMU  Already Exists");
				}
			}else {
				
			String cityMappingUpdatee = md.updateCityMMUPhaseStatus(Long.parseLong(jsonObject.get("cityMmuInvoiceMappingIdV").toString()),
					jsonObject.get("status").toString(),Long.parseLong(jsonObject.get("mmuId").toString()),Long.parseLong(jsonObject.getString("cityId").toString()),Long.parseLong(jsonObject.getString("phaseId").toString()));
			
					 
			if (cityMappingUpdatee != null && cityMappingUpdatee.equals("200")) {
					json.put("updateDistrict", cityMappingUpdatee);
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated.");
					json.put("status", 0);

				}
			}
			} else {
				json.put("msg", "Data Not Found");
				json.put("status", 0);

			}

		
		return json.toString(); 
	}




}
