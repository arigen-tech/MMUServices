package com.mmu.services.service.impl;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.dao.UserManagementDao;
import com.mmu.services.entity.EmployeeRegistration;
import com.mmu.services.entity.MasApplication;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasCity;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.entity.MasDesignation;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasMMU;
import com.mmu.services.entity.MasMmuVendor;
import com.mmu.services.entity.MasRole;
import com.mmu.services.entity.MasTemplate;
import com.mmu.services.entity.MasUserType;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.RoleDesignation;
import com.mmu.services.entity.RoleTemplate;
import com.mmu.services.entity.TemplateApplication;
import com.mmu.services.entity.UserApplication;
import com.mmu.services.entity.Users;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.UserManagementService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.JavaUtils;
import com.mmu.services.utils.ProjectUtils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Service("UserManagementService")
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	UserManagementDao userManagementDao;

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SystemAdminDao systemAdminDao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getAllUserApplication(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<UserApplication> userAppList = new ArrayList<UserApplication>();
		List list = new ArrayList();
		List totalMatches = new ArrayList();
		if (jsonObject != null) {
			Map<String, List<UserApplication>> map = userManagementDao.getAllUserApplication(jsonObject);

			if (map.get("userAppList") != null) {
				userAppList = map.get("userAppList");
				totalMatches = map.get("totalMatches");
				for (UserApplication userApplication : userAppList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (userApplication != null) {
						mapObj.put("id", userApplication.getId());
						mapObj.put("appName", userApplication.getAppName().trim());
						mapObj.put("url", userApplication.getUrl());
						mapObj.put("status", userApplication.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Record fetch successfully");
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
	public String updateUserApplication(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String userApp = userManagementDao.updateUserApplication(jsonObject);
		if (userApp != null && userApp.equalsIgnoreCase("200")) {
			json.put("userApp", userApp);
			json.put("msg", "Record Updated Successfully.");
			json.put("status", 1);
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getAllTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasTemplate> templateList = new ArrayList<>();
		List list = new ArrayList();
		if (jsonObject != null) {
			Map<String, List<MasTemplate>> map = userManagementDao.getAllTemplate(jsonObject);

			List totalMatches = new ArrayList();
			if (map.get("templateList") != null) {
				templateList = map.get("templateList");

				totalMatches = map.get("totalMatches");
				for (MasTemplate masTemplate : templateList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (masTemplate != null) {
						mapObj.put("templateId", masTemplate.getTemplateId());
						mapObj.put("templateCode", masTemplate.getTemplateCode());
						mapObj.put("templateName", masTemplate.getTemplateName());
						mapObj.put("status", masTemplate.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Record fetch successfully");
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
	public String getApplicationAutoComplete(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasApplication> applicationList = new ArrayList<MasApplication>();
		List userAppList = new ArrayList();

		List<Object> listObjModule = new ArrayList<Object>();
		Map<String, Object> userApplicationMap = userManagementDao.getApplicationAutoComplete(jsonObject);

		List<MasApplication> moduleList = userManagementDao.addFormsReportParentChildHierarchy(jsonObject);
		if (CollectionUtils.isNotEmpty(moduleList)) {
			for (Iterator<?> iterator = moduleList.iterator(); iterator.hasNext();) {
				Map<String, Object> mapObjModuleList = new HashMap<String, Object>();

				MasApplication applicationObj = (MasApplication) iterator.next();

				if (!applicationObj.getParentId().equalsIgnoreCase("0")) {
					mapObjModuleList.put("parentApplicationId", applicationObj.getParentId());
					List<MasApplication> parentMasAppList = userManagementDao
							.getParentApplicationName(applicationObj.getParentId());

					if (parentMasAppList != null && parentMasAppList.size() > 0) {
						for (Iterator<?> iterator2 = parentMasAppList.iterator(); iterator2.hasNext();) {
							MasApplication masApplication = (MasApplication) iterator2.next();
							mapObjModuleList.put("parentApplicationName", masApplication.getApplicationName());
						}
					}

				}

				if (applicationObj.getApplicationId() != null) {
					mapObjModuleList.put("applicationId", applicationObj.getApplicationId());
				}
				if (applicationObj.getApplicationName() != null) {
					mapObjModuleList.put("applicationName", applicationObj.getApplicationName());
				}
				listObjModule.add(mapObjModuleList);
			}
			if (listObjModule != null && listObjModule.size() > 0) {
				json.put("listObjModule", listObjModule);
				json.put("status", 1);
				json.put("msg", "successfully");
			} else {
				json.put("listObjModule", listObjModule);
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}

		} else {
			json.put("msg", "No Record Found");
			json.put("status", 0);
		}

		if (userApplicationMap.get("userApplicationsList") != null) {
			applicationList = (List<MasApplication>) userApplicationMap.get("maxAppIdlist");
			// userAppList = userApplicationMap.get("userApplicationsList");
			List<UserApplication> userAppList1 = (List<UserApplication>) userApplicationMap.get("userApplicationsList");
			if (CollectionUtils.isNotEmpty(userAppList1)) {
				for (Iterator<?> iterator = userAppList1.iterator(); iterator.hasNext();) {
					Map<String, Object> map = new HashMap<String, Object>();
					UserApplication userapplication = (UserApplication) iterator.next();
					map.put("appName", userapplication.getAppName());
					map.put("id", userapplication.getId());
					map.put("status", userapplication.getStatus());
					map.put("url", userapplication.getUrl());
					userAppList.add(map);
				}

			}
			json.put("max_app_id", applicationList.get(0).getApplicationId());
			/*
			 * for (int i = 0; i < applicationList.size(); i++) { json.put("max_app_id",
			 * applicationList.get(i)); }
			 */

			if (userAppList != null && userAppList.size() > 0) {
				json.put("data", userAppList);
				json.put("count", userAppList.size());
				json.put("status", 1);
			} else {
				json.put("data", "No Record Found");
				json.put("count", userAppList.size());
				json.put("status", 0);
			}

		} else {
			json.put("statut", 0);
			json.put("msg", "No Record Found");
		}

		return json.toString();
	}

	@Override
	public String addUserApplication(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		UserApplication userApplication = new UserApplication();
		if (jsonObject != null) {
			userApplication.setAppName(jsonObject.get("appName").toString());// .toUpperCase());
			userApplication.setUrl(jsonObject.get("url").toString());
			userApplication.setStatus("Y");
			Users users = new Users();
			users.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
			userApplication.setUsers(users);

			Timestamp lastChgDate = new Timestamp(new Date().getTime());
			userApplication.setLastChgDate(lastChgDate);

			List<UserApplication> validateApplication = userManagementDao
					.validateUserApplication(userApplication.getAppName(), userApplication.getUrl());
			if (validateApplication != null && validateApplication.size() > 0) {

				if (validateApplication.get(0).getAppName().equalsIgnoreCase(jsonObject.get("appName").toString())) {
					json.put("status", 0);
					json.put("msg", "Menu Name Already Existing");
				}
				if (validateApplication.get(0).getUrl().equalsIgnoreCase(jsonObject.get("url").toString())) {
					json.put("status", 2);
					json.put("msg", "Url Already Existing with other Application");
				}

			} else {
				String savedRecord = userManagementDao.saveUserApplication(jsonObject);
				if (savedRecord != null && savedRecord.equalsIgnoreCase("200")) {
					json.put("status", 1);
					json.put("msg", "Record Added Successfully");
				} else if (savedRecord != null && savedRecord.equalsIgnoreCase("300")) {
					json.put("status", 2);
					json.put("msg", "Record Not Added");
				} else {
					json.put("status", 0);
					json.put("msg", "Record Not Found");
				}
			}
		}
		// }
		return json.toString();

	}

	@Override
	public String updateTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String updatetemp = userManagementDao.updateTemplate(jsonObject);
		if (updatetemp != null && updatetemp.equalsIgnoreCase("200")) {
			json.put("updatetemp", updatetemp);
			json.put("msg", "Record Updated Successfully.");
			json.put("status", 1);
		} else if (updatetemp != null && updatetemp.equalsIgnoreCase("201")) {
			json.put("msg", "Status Updated Successfully.");
			json.put("status", 1);
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addTemplate(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			long hospitalId = 1;
			long departmentId = 1;
			long templateId = 2;
			MasTemplate masTemplate = new MasTemplate();
			if (jsonObject != null) {
				String templateCode = jsonObject.get("templateCode").toString();
				String templateName = jsonObject.get("templateName").toString();
				String flag = "Y";
				Timestamp lastChgDate = new Timestamp(new Date().getTime());

				masTemplate.setTemplateCode(templateCode);
				masTemplate.setTemplateName(templateName);
				masTemplate.setStatus(flag);
				masTemplate.setLastChgDate(lastChgDate);

				MasHospital masHospital = new MasHospital();
				masHospital.setHospitalId(hospitalId);
				masTemplate.setMasHospital(masHospital);

				/*
				 * MasTemplate template = new MasTemplate(); template.setTemplateId(templateId);
				 * masTemplate.setMasTemplate(masTemplate);
				 */
				List<MasTemplate> validateTemplt = userManagementDao.validateTemplate(masTemplate.getTemplateCode(),
						masTemplate.getTemplateName());
				if (validateTemplt != null && validateTemplt.size() > 0) {

					if (validateTemplt.get(0).getTemplateCode()
							.equalsIgnoreCase(jsonObject.get("templateCode").toString())) {
						json.put("status", 2);
						json.put("msg", "Template Code Already Existing");
					}
					if (validateTemplt.get(0).getTemplateName()
							.equalsIgnoreCase(jsonObject.get("templateName").toString())) {
						json.put("status", 2);
						json.put("msg", "Template Name Already Existing");
					}

				} else {
					// String savedRecord = userManagementDao.saveTemplate(masTemplate);
					String savedRecord = userManagementDao.saveTemplate(jsonObject);
					if (savedRecord != null && savedRecord.equalsIgnoreCase("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					} else if (savedRecord != null && savedRecord.equalsIgnoreCase("300")) {
						json.put("status", 2);
						json.put("msg", "Record Not Added");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Found");
					}
				}
			} else {
				json.put("status", 2);
				json.put("msg", "Invalid Input");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return json.toString();
	}

	@Override
	public String getTemplateList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {

			List<MasTemplate> tempList = userManagementDao.getTemplateList();
			if (tempList != null && tempList.size() > 0) {
				json.put("data", tempList);
				json.put("count", tempList.size());
				json.put("status", 1);
			} else {
				json.put("data", tempList);
				json.put("count", tempList.size());
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getModuleNameTemplateWise(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			List<Object> listObj = new ArrayList<Object>();
			List<Object> listObjModule = new ArrayList<Object>();
			List<TemplateApplication> listOfTempApp = new ArrayList<TemplateApplication>();
			Map<String, List<TemplateApplication>> appTemplateMap = userManagementDao
					.getModuleNameTemplateWise(jsonObject);

			List<MasApplication> moduleList = userManagementDao.getModuleListForTemplate();
			if (CollectionUtils.isNotEmpty(moduleList)) {
				for (Iterator<?> iterator = moduleList.iterator(); iterator.hasNext();) {
					Map<String, Object> mapObjModuleList = new HashMap<String, Object>();
					MasApplication applicationObj = (MasApplication) iterator.next();
					if (applicationObj.getApplicationId() != null) {
						mapObjModuleList.put("applicationId", applicationObj.getApplicationId());
					}
					if (applicationObj.getApplicationName() != null) {
						mapObjModuleList.put("applicationName", applicationObj.getApplicationName());
					}
					listObjModule.add(mapObjModuleList);
				}
				if (listObjModule != null && listObjModule.size() > 0) {
					json.put("listObjModule", listObjModule);
					json.put("status", 1);
					json.put("msg", "successfully");
				} else {
					json.put("listObjModule", listObjModule);
					json.put("status", 0);
					json.put("msg", "No Record Found");
				}

			} else {
				json.put("msg", "No Record Found");
				json.put("status", 0);
			}
			if (appTemplateMap.get("templateAppList") != null) {
				listOfTempApp = appTemplateMap.get("templateAppList");
				if (CollectionUtils.isNotEmpty(listOfTempApp)) {
					for (Iterator<?> iterator = listOfTempApp.iterator(); iterator.hasNext();) {
						Map<String, Object> mapdata = new HashMap<String, Object>();
						TemplateApplication itrObject = (TemplateApplication) iterator.next();
						if (itrObject.getTempAppId() != 0) {
							mapdata.put("tempappId", itrObject.getTempAppId());
						}
						if (itrObject.getMasTemplate().getTemplateId() != 0) {
							mapdata.put("templateId", itrObject.getMasTemplate().getTemplateId());
						}
						if (itrObject.getMasApplication().getApplicationId() != null) {
							mapdata.put("appId", itrObject.getMasApplication().getApplicationId());
						}
						if (itrObject.getMasApplication().getApplicationName() != null) {
							mapdata.put("name", itrObject.getMasApplication().getApplicationName());
						}

						listObj.add(mapdata);
					}
				} else {
					json.put("error", "No Record Found");
					json.put("status", 0);
				}
			}
			if (listObj != null && listObj.size() > 0) {
				json.put("data", listObj);
				json.put("count", listObj.size());
				json.put("msg", "successfully");
				json.put("status", 1);
			} else {
				json.put("msg", "No Record Found");
				json.put("data", listObj);
				json.put("count", listObj.size());
				json.put("status", 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getApplicationListForTemplate(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object> listObj = new ArrayList<Object>();
		List<MasApplication> masappList = new ArrayList<MasApplication>();
		try {
			Map<String, Object> mapObj = userManagementDao.populateApplications(jsonObject);
			if (mapObj.get("masAppList") != null) {
				masappList = (List<MasApplication>) mapObj.get("masAppList");
				if (CollectionUtils.isNotEmpty(masappList)) {
					for (Iterator<?> iterator = masappList.iterator(); iterator.hasNext();) {
						MasApplication applicationList = (MasApplication) iterator.next();
						Map<String, Object> map = new HashMap<String, Object>();
						if (applicationList.getApplicationName() != null) {
							map.put("appName", applicationList.getApplicationName());
						}
						if (applicationList.getApplicationName() != null) {
							map.put("parentId", applicationList.getParentId());
						}
						if (applicationList.getApplicationName() != null) {
							map.put("appId", applicationList.getApplicationId());
						}
						if (applicationList.getApplicationName() != null) {
							map.put("url", applicationList.getUrl());
						}
						listObj.add(map);
					}
				}
			}
			if (listObj != null && listObj.size() > 0) {
				json.put("listObj", listObj);
				json.put("count", listObj.size());
				json.put("status", 1);
			} else {
				json.put("listObj", listObj);
				json.put("count", listObj.size());
				json.put("status", 0);
				json.put("msg", "No Record Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String addFormAndReports(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		MasApplication masApplication = new MasApplication();
		try {
			if (jsonObject != null) {
				masApplication.setApplicationId(jsonObject.get("applicationId").toString());
				String applicationName = jsonObject.get("applicationName").toString();
				
				int index1 = applicationName.lastIndexOf("[");
				int index2 = applicationName.lastIndexOf("]");
				index1++;
				long appID = Long.parseLong(applicationName.substring(index1, index2));
				String appName1 = JavaUtils.getReplaceString(applicationName);
				String[] appName = appName1.split(",");
				masApplication.setApplicationName(appName[0].trim());
				

				Long maxSequenceCount = userManagementDao.getMaxCountOfChild(jsonObject.get("parentId").toString());
				Long newCount = maxSequenceCount + 1;
				

				String parentIdd = jsonObject.get("parentId").toString();
				if (!parentIdd.equalsIgnoreCase("0")) {
					int pidIndex1 = parentIdd.lastIndexOf("[");
					int pidIndex2 = parentIdd.lastIndexOf("]");
					pidIndex1++;

					String parId = JavaUtils.getReplaceString(parentIdd);
					String[] pid = parId.split(",");
					/*
					 * if(!pid[3].equalsIgnoreCase("0")) { String appId =
					 * userManagementDao.getApplicationIdParent(pid[3]);
					 * masApplication.setParentId(appId); }else {
					 * 
					 * masApplication.setParentId(pid[1]); }
					 */
					masApplication.setParentId(pid[1]);
					masApplication.setAppSequenceNo(newCount);
				} else {
					masApplication.setParentId(jsonObject.get("parentId").toString());
				}
				masApplication.setUrl(jsonObject.get("url").toString());
				masApplication.setStatus("y");
				
				masApplication.setOrderNo(userManagementDao.getAllMAsApplicationMax()+1);			
				 
				boolean flag = userManagementDao.validateAddFormAndReports(masApplication);

				if (flag == false) {

					String saveFormAndReports = userManagementDao.addFormAndReports(masApplication,appID);
					//String updateUserAppStatus = userManagementDao.updateUserApplicationStatus(appID);
					if (saveFormAndReports != null && saveFormAndReports.equalsIgnoreCase("200")) {
						json.put("msg", "Record Added Successfully");
						json.put("status", 1);
					} else if (saveFormAndReports != null && saveFormAndReports.equalsIgnoreCase("201")) {
						json.put("msg", "Record Not Added");
						json.put("status", 0);
					}
				} else {
					json.put("msg", "Application is Already Existing");
					json.put("status", 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getAllApplicationAndTemplates(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<Object> list = new ArrayList<Object>();
		List<Object> tempList = new ArrayList<Object>();
		String parentId = jsonObject.get("parentId").toString();
		long templateId = Long.parseLong(jsonObject.get("templateId").toString());

		Map<String, Object> applicationMap = userManagementDao.getAllApplicationAndTemplates(jsonObject, request,
				response);

		List<String[]> applicationList;
		List<String[]> existingtemplateList;

		if (applicationMap.get("listApp") != null) {
			applicationList = (List<String[]>) applicationMap.get("listApp");
			if (CollectionUtils.isNotEmpty(applicationList)) {
				for (Iterator<?> iterator = applicationList.iterator(); iterator.hasNext();) {
					Map<String, Object> mapObject = new HashMap<String, Object>();
					Object[] application = (Object[]) iterator.next();
					mapObject.put("applicationName", application[0]);
					mapObject.put("parentId", application[1]);
					mapObject.put("applicationId", application[2]);
					mapObject.put("url", application[3]);
					mapObject.put("applicationName2", application[4]);
					mapObject.put("status", application[5]);
					if (application[6] != null) {
						mapObject.put("templateId", application[6]);
					} else {
						mapObject.put("templateId", 0);
					}
					if (application[1].equals("0")) {
						mapObject.put("name2", application[0]);
					}
					list.add(mapObject);
				}
			}

			if (list != null && list.size() > 0) {
				json.put("data", list);
				json.put("count", list.size());
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", list.size());
				json.put("status", 0);
			}
		}
		if (applicationMap.get("existintemplateAppList") != null) {
			existingtemplateList = (List<String[]>) applicationMap.get("existintemplateAppList");
			if (CollectionUtils.isNotEmpty(existingtemplateList)) {
				for (Iterator<?> iterator = existingtemplateList.iterator(); iterator.hasNext();) {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					Object[] templateObject = (Object[]) iterator.next();
					tempMap.put("parentId", templateObject[0]);
					tempMap.put("appId", templateObject[1]);
					tempMap.put("templateId", templateObject[2]);
					tempMap.put("status", templateObject[3]);
					tempList.add(tempMap);

				}
			}
			if (tempList != null && tempList.size() > 0) {
				json.put("tempList", tempList);
				json.put("count", tempList.size());
				json.put("jsonStatus", 1);
			} else {
				json.put("tempList", tempList);
				json.put("count", tempList.size());
				json.put("jsonStatus", 0);
			}
		}
		return json.toString();
	}

	@Override
	public String addTemplateApplication(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (jsonObject != null) {
			try {
				String addTemplateApplication = "";
				// TemplateApplication templateApplication=null;

				// JSONObject jsonObject= new JSONObject(jsonObject1);
				String applicationId = jsonObject.get("applicationIdAarray").toString();
				String parentId = jsonObject.get("parentId").toString();

				applicationId = JavaUtils.getReplaceString1(applicationId);
				applicationId = applicationId.replaceAll("\"", "");
				String[] applicationValueArray = applicationId.split(",");

				String checkBoxArray = jsonObject.get("checkBoxArray").toString();
				checkBoxArray = JavaUtils.getReplaceString1(checkBoxArray);
				checkBoxArray = checkBoxArray.replaceAll("\"", "");
				String[] checkBoxValueArray = checkBoxArray.split(",");

				String templateid = jsonObject.get("templateIdArray").toString();
				templateid = JavaUtils.getReplaceString1(templateid);
				templateid = templateid.replaceAll("\"", "");
				String[] templateIdArray = templateid.split(",");

				long tempId = Long.parseLong(jsonObject.get("templateId").toString());
				

				// String[] applicationValueArray1= applicationValueArray.s
				for (int i = 0; i < applicationValueArray.length; i++) {
					TemplateApplication templateApplication = new TemplateApplication();
					

					if (templateIdArray.length == 0 || templateIdArray[i].equals("0")) {
						// save
						

						MasApplication masApplication = new MasApplication();
						masApplication.setApplicationId(applicationValueArray[i]);
						templateApplication.setMasApplication(masApplication);

						MasTemplate masTemplate = new MasTemplate();
						masTemplate.setTemplateId(tempId);
						templateApplication.setMasTemplate(masTemplate);

						if (!checkBoxValueArray[i].equals("")) {
							templateApplication.setStatus(checkBoxValueArray[i]);
						}
						addTemplateApplication = userManagementDao.addTemplateApplication(templateApplication);
					}

					else {

						// update

						Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
						templateApplication = (TemplateApplication) session.get(TemplateApplication.class,
								Long.parseLong(templateIdArray[i].toString()));
						if (tempId != 0) {
							MasTemplate masTemplate = new MasTemplate();
							masTemplate.setTemplateId(tempId);
							templateApplication.setMasTemplate(masTemplate);
						}
						if (!applicationValueArray[i].equals("")) {
							MasApplication masApplication = new MasApplication();
							masApplication.setApplicationId(applicationValueArray[i]);
							templateApplication.setMasApplication(masApplication);
						}

						if (!checkBoxValueArray[i].equals("")) {
							templateApplication.setStatus(checkBoxValueArray[i]);
						}

						addTemplateApplication = userManagementDao.addTemplateApplication(templateApplication);

					}
				}

				if (addTemplateApplication != null && addTemplateApplication.equals("200")) {
					json.put("msg", "Record Added Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Added");
					json.put("status", 0);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}

	@Override
	public String getRoleRightsList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasRole> roleList = userManagementDao.getRoleRightsList();
		if (roleList != null && roleList.size() > 0) {

			jsonObj.put("data", roleList);
			jsonObj.put("count", roleList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", roleList);
			jsonObj.put("count", roleList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		
		return jsonObj.toString();
	}

	@Override
	public String getTemplateNameList(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasTemplate> tempNameList = userManagementDao.getTemplateNameList();
		if (tempNameList != null && tempNameList.size() > 0) {

			jsonObj.put("data", tempNameList);
			jsonObj.put("count", tempNameList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", tempNameList);
			jsonObj.put("count", tempNameList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);
		}
		
		return jsonObj.toString();
	}

	@Override
	public String getAssingedTemplateNameList(JSONObject json, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<Map<String, Object>> respList = new ArrayList<Map<String, Object>>();
		List<RoleTemplate> rolelist = userManagementDao.getAssingedTemplateNameList(json);
		if (rolelist != null && rolelist.size() > 0) {

			for (RoleTemplate list : rolelist) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", list.getMsTemplate().getTemplateId());
				map.put("status", list.getStatus());
				map.put("count", rolelist.size());
				respList.add(map);
			}
			jsonObj.put("data", respList);

		} else {
			jsonObj.put("data", rolelist);
			jsonObj.put("count", rolelist.size());
			jsonObj.put("msg", "No Record Found");

		}
		
		return jsonObj.toString();

	}

	@Override
	public String saveRolesRight(JSONObject json, HttpServletRequest request, HttpServletResponse response) {

		JSONObject jsonObj = new JSONObject();
		if (json != null) {

			String roleObj = userManagementDao.saveRolesRight(json);

			if (roleObj != null && roleObj.equalsIgnoreCase("200")) {
				jsonObj.put("status", 1);
				jsonObj.put("msg", "Roles right saved Successfully");

			} else if (roleObj != null && roleObj.equalsIgnoreCase("500")) {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "Roles rights not saved");

			} else if (roleObj != null && roleObj.equalsIgnoreCase("403")) {
				jsonObj.put("status", 0);
				jsonObj.put("msg", "You are not authorized person!!!");

			} else {
				jsonObj.put("msg", roleObj);
				jsonObj.put("status", 0);
			}

		} else {
			jsonObj.put("msg", "Cannot Contains Any Data!!!");
			jsonObj.put("status", 0);
		}
		
		return jsonObj.toString();
	}

	@Override
	public String getApplicationNameFormsAndReport(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List listObjModule = new ArrayList();
		try {
			if (jsonObject != null) {
				List<MasApplication> listofApplications = userManagementDao
						.getApplicationNameFormsAndReport(jsonObject);
				List<MasApplication> app_id_list = userManagementDao.getAllApp_Id();

				List<Object> listObject = new ArrayList<Object>();
				if (CollectionUtils.isNotEmpty(listofApplications)) {
					for (Iterator<?> iterator = listofApplications.iterator(); iterator.hasNext();) {
						Map<String, Object> mapObject = new HashMap<String, Object>();
						MasApplication applicationObject = (MasApplication) iterator.next();

						if (applicationObject != null) {
							mapObject.put("applicationId", applicationObject.getApplicationId());
							mapObject.put("applicationName", applicationObject.getApplicationName());
							mapObject.put("parentId", applicationObject.getParentId());

							for (Iterator<?> iterator2 = app_id_list.iterator(); iterator2.hasNext();) {
								MasApplication masapp = (MasApplication) iterator2.next();
								if (masapp.getApplicationId().equals(applicationObject.getParentId())) {

									mapObject.put("parentIdAppIdName", masapp.getApplicationName().concat("[")
											.concat(masapp.getApplicationId()).concat("]").trim());
								}
							}

							mapObject.put("applicationUrl", applicationObject.getUrl());
							// mapObject.put("orderNo", applicationObject.getOrderNo());
							mapObject.put("applicationStatus", applicationObject.getStatus());
						}

						// code added by rajdeo
						if (!applicationObject.getParentId().equalsIgnoreCase("0")) {
							mapObject.put("parentApplicationId", applicationObject.getParentId());
							List<MasApplication> parentMasAppList = userManagementDao
									.getParentApplicationName(applicationObject.getParentId());

							if (parentMasAppList != null && parentMasAppList.size() > 0) {
								for (Iterator<?> iterator2 = parentMasAppList.iterator(); iterator2.hasNext();) {
									MasApplication masApplication = (MasApplication) iterator2.next();
									mapObject.put("parentApplicationName", masApplication.getApplicationName());
								}
							}

						}
						listObject.add(mapObject);
					}
				}

				if (listObject != null) {
					json.put("data", listObject);
					json.put("count", listObject.size());
					json.put("status", 1);
				}

			} else {
				json.put("msg", "Invalid Request");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	@Override
	public String updateAddFormsAndReport(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		MasApplication masApplication = new MasApplication();
		try {
			if (jsonObject != null) {
				masApplication.setApplicationId(jsonObject.get("applicationId").toString());
				masApplication.setApplicationName(jsonObject.get("applicationName").toString());
				masApplication.setUrl(jsonObject.get("applicationUrl").toString());
				// masApplication.setParentId(jsonObject.get("parentId").toString());

				
				String parentIdd = jsonObject.get("parentId").toString();
				if (!parentIdd.equalsIgnoreCase("0")) {
					int pidIndex1 = parentIdd.lastIndexOf("[");
					int pidIndex2 = parentIdd.lastIndexOf("]");
					pidIndex1++;

					String parId = JavaUtils.getReplaceString(parentIdd);
					String[] pid = parId.split(",");
					masApplication.setParentId(pid[1]);
				} else {
					masApplication.setParentId(jsonObject.get("parentId").toString());
				}

				masApplication.setStatus(jsonObject.get("applicationStatus").toString());

				String updateFormsAndReport = userManagementDao.updateAddFormsAndReport(masApplication);
				if (updateFormsAndReport != null && updateFormsAndReport.equalsIgnoreCase("200")) {
					json.put("msg", "Record Updated Successfully");
					json.put("status", 1);
				} else {
					json.put("msg", "Record Not Updated");
					json.put("status", 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public Map<String, Object> getApplicationNameBasesOnRole(Map<String, Object> requestData) {
		List<Users> userList = new ArrayList<Users>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Map<String, Object>> respUserList = new ArrayList<Map<String, Object>>();

		JSONObject json = new JSONObject(requestData);
		String userId = json.getString("userId");
		
		Long nUserId= Long.parseLong(userId);

		
		if (userId != null && !userId.isEmpty()) {
			userList = (List<Users>) userManagementDao.getUserAndHospitalFromServiceNo(nUserId);
			if (userList != null && userList.size() > 0) {
				for (Users user : userList) {
					Map<String, Object> responseUserMap = new HashMap<String, Object>();

					
					responseUserMap.put("userId", user.getUserId());
					
					responseUserMap.put("user_Name", user.getUserName());
					

					respUserList.add(responseUserMap);
				}

				if (respUserList != null && respUserList.size() > 0) {
					responseMap.put("respUserList", respUserList);
					responseMap.put("msg", "Data found");
					responseMap.put("status", 1);
				} else {
					responseMap.put("respUserList", respUserList);
					responseMap.put("msg", "Data not found");
					responseMap.put("status", 0);
				}
			} else {
				responseMap.put("respUserList", respUserList);
				responseMap.put("msg", "User Does Not Exist.");
				responseMap.put("status", 0);
			}
			
		} else {
			responseMap.put("status", 0);
			responseMap.put("msg", "Data not found");
		}

		return responseMap;
	}

	@Override
	public String getRoleAndDesignationMappingList(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<RoleDesignation> roleDesgList = new ArrayList<RoleDesignation>();
		List list = new ArrayList();
		List<Object> responseList = new ArrayList<Object>();
		List totalMatches = new ArrayList();
		if (jsonObject != null) {
			Map<String, List<RoleDesignation>> map = userManagementDao.getRoleAndDesignationMappingList(jsonObject);

			if (map.get("roleDesgList") != null) {
				roleDesgList = map.get("roleDesgList");
				totalMatches = map.get("totalMatches");
				if (CollectionUtils.isNotEmpty(roleDesgList)) {
					for (Iterator<?> iterator = roleDesgList.iterator(); iterator.hasNext();) {
						Map<String, Object> mapObjList = new HashMap<String, Object>();
						RoleDesignation roleDesignation = (RoleDesignation) iterator.next();

						HashMap<String, Object> jsonMap = new HashMap<String, Object>();
						String designationName = "";

						if (roleDesignation != null) {
							jsonMap.put("designationId", roleDesignation.getMasDesignation().getDesignationId());
							jsonMap.put("designationName", roleDesignation.getMasDesignation().getDesignationName());
							jsonMap.put("roleDesigId", roleDesignation.getRoleDesignationId());
						}
						
						else {
							jsonMap.put("designationId", "");
							jsonMap.put("designationName", "");

						}
						jsonMap.put("status", roleDesignation.getStatus());

						if (roleDesignation.getRoleId() != null || roleDesignation.getRoleId() != "") {

							String roleIds = roleDesignation.getRoleId();
							
							
							String roleNames = "";
							if (StringUtils.isNotEmpty(roleDesignation.getRoleId()))
								roleNames = userManagementDao.getMasRoleByRoleId(roleDesignation.getRoleId());
							if (StringUtils.isNotEmpty(roleNames)) {
								String[] rolesArray = roleNames.split("##");

								jsonMap.put("roleNames", rolesArray[0]);
								jsonMap.put("rolesId", rolesArray[1]);
							} else {
								jsonMap.put("roleNames", "");
								jsonMap.put("rolesId", "");

							}
							responseList.add(jsonMap);
						}

						// list.add(mapObjList);
					}
				}

				if (responseList != null && responseList.size() > 0) {
					json.put("data", responseList);
					json.put("count", totalMatches.size());
					json.put("msg", "Record fetch successfully");
					json.put("status", 1);
				} else {
					json.put("data", responseList);
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
	public String getDesignationList(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		List<MasDesignationMapping> designationList = userManagementDao.getDesignationList(jsonObject);
		if (!designationList.isEmpty() && designationList.size() > 0) {

			jsonObj.put("data", designationList);
			jsonObj.put("count", designationList.size());
			jsonObj.put("status", 1);
		} else {
			jsonObj.put("data", designationList);
			jsonObj.put("count", designationList.size());
			jsonObj.put("msg", "No Record Found");
			jsonObj.put("status", 0);

		}
		
		return jsonObj.toString();
	}

	@Override
	public String roleAndDesignationMapping(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		JSONObject jsonObj = new JSONObject();
		String roledesignationMapping = "";
		RoleDesignation roleDesignation = new RoleDesignation();
		MasRole masRole = new MasRole();
		
		String rolesArr = jsonObject.get("roleId").toString();
		String rolessId = "";

		int pidIndex1 = rolesArr.lastIndexOf("[");
		int pidIndex2 = rolesArr.lastIndexOf("]");
		pidIndex1++;
		rolessId = JavaUtils.getReplaceString1(rolesArr);
		String roleIds = rolessId.replaceAll("\"", "");
		

		roleDesignation.setRoleId(roleIds);
		MasDesignation masDesignation = new MasDesignation();
		masDesignation.setDesignationId(Long.parseLong(jsonObject.get("designationId").toString()));
		roleDesignation.setMasDesignation(masDesignation);
		roleDesignation.setStatus("Y");

		Users user = new Users();
		user.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
		roleDesignation.setUser(user);

		MasHospital masHospital = new MasHospital();
		masHospital.setHospitalId(Long.parseLong(jsonObject.get("hospitalId").toString()));

		roleDesignation.setLocationId(Long.parseLong(jsonObject.get("hospitalId").toString()));
		roleDesignation.setLastChgDate(timestamp);

		String ifexist = userManagementDao.existDesignationInMasRoleDesignationMapping(
				roleDesignation.getMasDesignation().getDesignationId(), roleDesignation.getLocationId());
		
		if (ifexist.equalsIgnoreCase("success")) {
			jsonObj.put("status", 2);
			jsonObj.put("msg", "Designation Name is Already exists");
		} else {
			roledesignationMapping = userManagementDao.roleAndDesignationMapping(roleDesignation);
		}

		if (roledesignationMapping != null && roledesignationMapping.equalsIgnoreCase("200")) {
			jsonObj.put("status", 1);
			jsonObj.put("msg", "Record Mapped Successfully");
		} else if (roledesignationMapping != null && roledesignationMapping.equalsIgnoreCase("201")) {
			jsonObj.put("status", 0);
			jsonObj.put("msg", "Designation Name is Already exists");
		} else {
			jsonObj.put("status", 0);
			jsonObj.put("msg", "Designation Name is Already exists");

		}
		return jsonObj.toString();
	}

	@Override
	public String updateRoleAndDesignationMapping(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String roleDesig = userManagementDao.updateRoleAndDesignationMapping(jsonObject);
		if (roleDesig != null && roleDesig.equalsIgnoreCase("200")) {
			json.put("roleDesig", roleDesig);
			json.put("msg", "Record Updated Successfully.");
			json.put("status", 1);
		} else if (roleDesig != null && roleDesig.equalsIgnoreCase("statusupdated")) {
			json.put("roleDesig", roleDesig);
			json.put("msg", "Status Updated Successfully.");
			json.put("status", 1);
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	// mas designation
	@Override
	public String getAllDesignations(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasDesignation> designationList = new ArrayList<MasDesignation>();
		List list = new ArrayList();
		if (jsonObject != null) {
			Map<String, List<MasDesignation>> map = userManagementDao.getAllDesignations(jsonObject);

			List totalMatches = new ArrayList();
			if (map.get("designationList") != null) {
				designationList = map.get("designationList");

				totalMatches = map.get("totalMatches");
				for (MasDesignation masDesignation : designationList) {
					HashMap<String, Object> mapObj = new HashMap<String, Object>();
					if (masDesignation != null) {
						mapObj.put("designationId", masDesignation.getDesignationId());
						mapObj.put("designationCode", masDesignation.getDesignationCode());
						mapObj.put("designationName", masDesignation.getDesignationName());
						mapObj.put("status", masDesignation.getStatus());
						list.add(mapObj);
					}
				}

				if (list != null && list.size() > 0) {
					json.put("data", list);
					json.put("count", totalMatches.size());
					json.put("msg", "Record fetch successfully");
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
	public String updateDesignationDetails(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String updateDesig = userManagementDao.updateDesignationDetails(jsonObject);
		if (updateDesig != null && updateDesig.equalsIgnoreCase("200")) {
			json.put("updateDesig", updateDesig);
			json.put("msg", "Record Updated Successfully.");
			json.put("status", 1);
		} else if (updateDesig != null && updateDesig.equalsIgnoreCase("201")) {
			json.put("msg", "Status Updated Successfully.");
			json.put("status", 1);
		} else {
			json.put("msg", "Record Not Updated.");
			json.put("status", 0);
		}
		return json.toString();
	}

	@Override
	public String addDesignation(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			MasDesignation masDesignation = new MasDesignation();
			if (jsonObject != null) {
				masDesignation.setDesignationCode(jsonObject.get("designationCode").toString().toUpperCase());
				masDesignation.setDesignationName(jsonObject.get("designationName").toString().toUpperCase());
				masDesignation.setStatus("Y");

				long d = System.currentTimeMillis();
				Timestamp date = new Timestamp(d);
				long userId = Long.parseLong(jsonObject.get("userId").toString());
				Users users = new Users();
				masDesignation.setUser(users);
				masDesignation.setLastChgDate(date);
				users.setUserId(userId);
				// masDesignation.setGroupId(new Long(1));
				// masDesignation.setId(new Long(1));

				List<MasDesignation> validateDesig = userManagementDao.validateMasDesignation(
						masDesignation.getDesignationCode(), masDesignation.getDesignationName());
				if (validateDesig != null && validateDesig.size() > 0) {

					if (validateDesig.get(0).getDesignationCode()
							.equalsIgnoreCase(jsonObject.get("designationCode").toString())) {
						json.put("status", 2);
						json.put("msg", "Designation Code Already Existing");
					}
					if (validateDesig.get(0).getDesignationName()
							.equalsIgnoreCase(jsonObject.get("designationName").toString())) {
						json.put("status", 2);
						json.put("msg", "Designation Name Already Existing");
					}

				} else {
					String savedRecord = userManagementDao.addDesignation(masDesignation);
					if (savedRecord != null && savedRecord.equalsIgnoreCase("200")) {
						json.put("status", 1);
						json.put("msg", "Record Added Successfully");
					} else if (savedRecord != null && savedRecord.equalsIgnoreCase("300")) {
						json.put("status", 0);
						json.put("msg", "Record Not Added");
					} else {
						json.put("status", 0);
						json.put("msg", "Record Not Found");
					}
				}
			} else {
				json.put("status", 0);
				json.put("msg", "Invalid Input");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return json.toString();
	}

	@Override
	public String submitRoleAndDesignation(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			RoleDesignation roleDesignation = new RoleDesignation();
			MasRole masRole = new MasRole();
			roleDesignation.setRoleId(jsonObject.get("masRoleIdValues").toString());
			Users users = new Users();
			users.setUserId(new Long(1));
			roleDesignation.setUser(users);

			roleDesignation.setStatus("Y");

			Timestamp lastChgDate = new Timestamp(new Date().getTime());
			roleDesignation.setLastChgDate(lastChgDate);
			Long id = userManagementDao.saveRoleAndDesignation(roleDesignation);
			
			if (id != 0) {
				json.put("status", "success");
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", "fail");
				json.put("msg", "Record Nout Added");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getMultipleRoleAndDesignation(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		List<Object> responseList = new ArrayList<Object>();
		List<RoleDesignation> listOfRoleDesignation = null;
		JSONObject json = new JSONObject();

		listOfRoleDesignation = userManagementDao.getMultipleRoleAndDesignation(jsonObject);
		for (RoleDesignation roleDesignation : listOfRoleDesignation) {
			HashMap<String, Object> jsonMap = new HashMap<String, Object>();

			// jsonMap.put("designationId", roleDesignation.getDesignationId());

			String designationName = "";
			// if(StringUtils.isNotEmpty(roleDesignation.getDesignationId()))
			// designationName=
			// userManagementDao.getMasDesignationByDesignationId(roleDesignation.getDesignationId());

			if (StringUtils.isNotEmpty(designationName)) {
				String[] desinationArray = designationName.split("##");
				jsonMap.put("designationId", desinationArray[1]);
				jsonMap.put("designamtionName", desinationArray[0]);
			} else {
				jsonMap.put("designationId", "");
				jsonMap.put("designamtionName", "");

			}
			jsonMap.put("status", roleDesignation.getStatus());
			String roleNames = "";
			if (StringUtils.isNotEmpty(roleDesignation.getRoleId()))
				roleNames = userManagementDao.getMasRoleByRoleId(roleDesignation.getRoleId());
			if (StringUtils.isNotEmpty(roleNames)) {
				String[] rolesArray = roleNames.split("##");

				jsonMap.put("roleNames", rolesArray[0]);
				jsonMap.put("rolesId", rolesArray[1]);
			} else {
				jsonMap.put("roleNames", "");
				jsonMap.put("rolesId", "");

			}
			responseList.add(jsonMap);
		}
		if (responseList != null && responseList.size() > 0) {
			json.put("dataUserList", responseList);
			json.put("status", 1);
		} else {
			json.put("data", responseList);
			json.put("msg", "Data not found");
			json.put("status", 0);
		}

		return json.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> getApplicationNameBasesOnRoleNew(Map<String, Object> requestData) {
		List<Users> userList = new ArrayList<Users>();
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<Map<String, Object>> respUserList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> respApplicationList = null;

		JSONObject json = new JSONObject(requestData);
		String suserId = json.getString("userId");
		JSONArray roles = json.getJSONArray("roles");
		Long userId = Long.parseLong(json.getString("userId").toString());
		String hospitalIdForMas = json.getString("hospitalIdForMas");
		/*
		 * String serviceNo="123457"; String[] roles =new String[3];
		 * 
		 * roles[0] = "RECEPTIONIST"; roles[1] = "DOCTOR"; roles[2] = "MEDICAL OFFICER";
		 */
		/*
		 * if (requestData.containsKey("importToShip") &&
		 * requestData.get("importToShip") != null && requestData.get("importToShip") !=
		 * "" &&
		 * requestData.get("importToShip").toString().equalsIgnoreCase("importToShip"))
		 * { responseMap.put("respApplicationList", respApplicationList);
		 * responseMap.put("status", 0); responseMap.put("msg", "Role not found");
		 * responseMap.put("listOfKeyyy", null);
		 * responseMap.put("applicationListSubchild", null);
		 * responseMap.put("keyOfRole", null); responseMap.put("serviceNo", "");
		 * responseMap.put("importToSip", "yes"); return responseMap; }
		 */
		if (userId != null && !suserId.isEmpty()) {
			userList = (List<Users>) userManagementDao.getUserAndHospitalFromServiceNo(userId);

			if (userList != null && userList.size() > 0) {
				for (Users user : userList) {
					Map<String, Object> responseUserMap = new HashMap<String, Object>();
					responseUserMap.put("userId", user.getUserId());
					//responseUserMap.put("hospitalId", user.getMasHospital().getHospitalId());
					respUserList.add(responseUserMap);
				}

				if (respUserList != null && respUserList.size() > 0) {
					responseMap.put("respUserList", respUserList);
					responseMap.put("status", 1);
				} else {
					responseMap.put("respUserList", respUserList);
					responseMap.put("msg", "Data not found");
					responseMap.put("status", 0);
				}
			} else {
				responseMap.put("respUserList", respUserList);
				responseMap.put("msg", "User Does not exist.");
				responseMap.put("status", 0);
			}
			Map<String, List<Map<String, Object>>> listOfKeyyy = new HashMap<>();
			
			//System.out.println("A="+roles != null && roles.length());
			if (roles != null && roles.length() > 0) {
				Users users = systemAdminDao.getUserbyUserId(userId);
				//Users users = null;
				List<Long> listRoles = null;
				if (users != null)
					listRoles = userManagementDao.getRolesList(users, hospitalIdForMas);
				Map<String, Object> hashMap11 = userManagementDao.getApplicationNameBasesOnRoleNew(listRoles,
						hospitalIdForMas);
				List applicationList = (List) hashMap11.get("applicationNameList");
				List applicationNameList1 = (List) hashMap11.get("applicationNameList1");

				List applicationListSubchild = (List) hashMap11.get("applicationListSubchild");

				Map<String, String> keyOfRole = new TreeMap<>();
				List<MasApplication> listMasApplication = (List<MasApplication>) hashMap11.get("listMasApplication");
				List<MasApplication> listMasApplication1 = (List<MasApplication>) hashMap11.get("listMasApplication1");

				if (listMasApplication != null && listMasApplication.size() > 0) {

					/*
					 * if (applicationNameList1 != null && applicationNameList1.size() > 0) for
					 * (Object object1 : applicationNameList1) { respApplicationList = new
					 * ArrayList<Map<String, Object>>(); Object[] rows1 = (Object[]) object1; for
					 * (Object object : applicationList) { Object[] rows = (Object[]) object;
					 * if(rows1[0].toString().equalsIgnoreCase(rows[3].toString())) { Map<String,
					 * Object> responseAppMap = new HashMap<String, Object>();
					 * responseAppMap.put("appId",rows[0].toString()); responseAppMap.put("appName",
					 * rows[2].toString()); responseAppMap.put("url", rows[1].toString());
					 * responseAppMap.put("orderNo", Long.parseLong(rows[4].toString()));
					 * respApplicationList.add(responseAppMap); }
					 * 
					 * }
					 * 
					 * Collections.sort(respApplicationList, new Comparator<Map<String, Object>> ()
					 * { public int compare(Map<String, Object> m1, Map<String, Object> m2) { return
					 * ((Long) m1.get("orderNo")).compareTo((Long)m2.get("orderNo")); //ascending
					 * order //return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num"));
					 * //descending order } });
					 * 
					 * 
					 * listOfKeyyy.put(rows1[0].toString(),respApplicationList); }
					 */

					if (listMasApplication1 != null && listMasApplication1.size() > 0)
						for (MasApplication masApplication1 : listMasApplication1) {
							
							System.out.println("keyid="+masApplication1.getApplicationId().toString());
							
							keyOfRole.put(masApplication1.getAppSequenceNo().toString(),
									masApplication1.getApplicationId().toString());
							respApplicationList = new ArrayList<Map<String, Object>>();
							// Object[] rows1 = (Object[]) object1;
							for (MasApplication masApplication : listMasApplication) {
								// Object[] rows = (Object[]) object;
								if (masApplication1.getApplicationId().toString()
										.equalsIgnoreCase(masApplication.getParentId().toString())) {
									Map<String, Object> responseAppMap = new HashMap<String, Object>();
									responseAppMap.put("appId", masApplication.getApplicationId().toString());
									responseAppMap.put("appName", masApplication.getApplicationName().toString());
									responseAppMap.put("url", masApplication.getUrl().toString());
									responseAppMap.put("orderNo",
											Long.parseLong(masApplication.getOrderNo().toString()));
									responseAppMap.put("appSequenceNo",
											Long.parseLong(masApplication.getAppSequenceNo().toString()));

									respApplicationList.add(responseAppMap);
								}

							}
							// keyOfRole.put(masApplication1.getOrderNo().toString(),
							// masApplication1.getApplicationId().toString());
							
							// add in main metjhod above
							
							/*keyOfRole.put(masApplication1.getAppSequenceNo().toString(),
									masApplication1.getApplicationId().toString());*/
							System.out.println("size of keyOfRole="+keyOfRole.size());
							

							/*
							 * Collections.sort(respApplicationList, new Comparator<Map<String, Object>> ()
							 * { public int compare(Map<String, Object> m1, Map<String, Object> m2) { return
							 * ((Long) m1.get("orderNo")).compareTo((Long)m2.get("orderNo")); //ascending
							 * order //return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num"));
							 * //descending order } });
							 */
							Collections.sort(respApplicationList, new Comparator<Map<String, Object>>() {
								public int compare(Map<String, Object> m1, Map<String, Object> m2) {

									return ((Long) m1.get("appSequenceNo")).compareTo((Long) m2.get("appSequenceNo")); // ascending
																														// order

									// return ((Integer) m2.get("num")).compareTo((Integer) m1.get("num"));
									// //descending order
								}
							});

							listOfKeyyy.put(masApplication1.getApplicationId().toString(), respApplicationList);

						}

					List<Map<String, Object>> c = new ArrayList<Map<String, Object>>();
					if (CollectionUtils.isNotEmpty(applicationListSubchild))
						for (Iterator<?> it = applicationListSubchild.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();
							Map<String, Object> pt = new HashMap<String, Object>();
							String appId = "";
							String url = "";
							String parentId = "";
							String applicationName = "";
							if (row[0] != null) {
								pt.put("appId", row[0].toString());
							} else {
								pt.put("appId", "");
							}

							if (row[1] != null) {
								pt.put("url", row[1].toString());
							} else {
								pt.put("url", "");
							}

							if (row[2] != null) {
								pt.put("appName", row[2].toString());
							} else {
								pt.put("appName", "");
							}
							if (row[3] != null) {
								pt.put("parentId", row[3].toString());
							} else {
								pt.put("parentId", "");
							}
							if (row[4] != null) {
								pt.put("orderNo", Long.parseLong(row[4].toString()));
							} else {
								pt.put("orderNo", 0);
							}

							if (row[5] != null) {
								pt.put("appSequenceNo", Long.parseLong(row[5].toString()));
							} else {
								pt.put("appSequenceNo", 0);
							}

							c.add(pt);

							/*
							 * Collections.sort(c, new Comparator<Map<String, Object>> () { public int
							 * compare(Map<String, Object> m1, Map<String, Object> m2) { return ((Long)
							 * m1.get("orderNo")).compareTo((Long)m2.get("orderNo")); //ascending order
							 * 
							 * } });
							 */
							Collections.sort(c, new Comparator<Map<String, Object>>() {
								public int compare(Map<String, Object> m1, Map<String, Object> m2) {

									return ((Long) m1.get("appSequenceNo")).compareTo((Long) m2.get("appSequenceNo")); // ascending
																														// order

								}
							});

						}

					if (listOfKeyyy != null && listOfKeyyy.size() > 0) {
						responseMap.put("respApplicationList", respApplicationList);
						responseMap.put("listOfKeyyy", listOfKeyyy);
						responseMap.put("applicationListSubchild", c);
						responseMap.put("keyOfRole", keyOfRole);
						responseMap.put("status", 1);
						responseMap.put("importToSip", "no");
					} else {
						responseMap.put("respApplicationList", respApplicationList);
						responseMap.put("status", 0);
						responseMap.put("msg", "Role not found1");
						responseMap.put("listOfKeyyy", listOfKeyyy);
						responseMap.put("applicationListSubchild", c);
						responseMap.put("keyOfRole", keyOfRole);
						responseMap.put("importToSip", "no");
					}

				} else {
					responseMap.put("respApplicationList", respApplicationList);
					responseMap.put("status", 0);
					responseMap.put("msg", "Role not found");
					responseMap.put("listOfKeyyy", listOfKeyyy);
					responseMap.put("applicationListSubchild", null);
					responseMap.put("keyOfRole", keyOfRole);
					responseMap.put("serviceNo", "");
					responseMap.put("importToSip", "no");
				}
			}
		} else {
			responseMap.put("status", 0);
			responseMap.put("msg", "Data not found");
			responseMap.put("importToSip", "no");
		}

		return responseMap;
	}

	
	@Override
	public JSONObject getLoginDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
	
		   Users userLoginEntity = userManagementDao.checkLoginCredential(jsondata.get("userName").toString(), true);
			if (userLoginEntity == null) {
				return ProjectUtils.getReturnMsg("0", "Sorry, we couldn't find an account with that Username");
			}
			
			boolean isValidPassword = ProjectUtils.checkPass(jsondata.get("password").toString(),
					userLoginEntity.getPassword());
			if (isValidPassword != true) {
				return ProjectUtils.getReturnMsg("0", "Sorry, that password is incorrect");
			}
			
			/*if(userLoginEntity.getUserFlag()==0)
			{
				return ProjectUtils.getReturnMsg("0", "Your verification is pending please check system administartor");
			}*/
			if(userLoginEntity.getUserFlag()==2)
			{
				return ProjectUtils.getReturnMsg("0", "User credential is deactivated. Please contact administrator");
			}

			Map<String, Object> map = userManagementDao.getLoginDetails(jsondata);
			//List<MasEmployee> getUsersDetails = (List<MasEmployee>) map.get("list");
			List<HashMap<String, Object>> p = new ArrayList<HashMap<String, Object>>();
			//List<HashMap<String, Object>> pDigi = new ArrayList<HashMap<String, Object>>();
			@SuppressWarnings("unchecked")
			List<Users> getUsers = (List<Users>) map.get("listUser");
			JSONObject object = new JSONObject();
			try {

				for (Users userDetails : getUsers) {
					object.put("loginName", userDetails.getLoginName().trim());
					object.put("password", userDetails.getPassword().trim());
					if(userDetails.getEmployeeId()!=null)
					{	
					object.put("employeeId", userDetails.getEmployeeIdValue());
					}
					else
					{
						object.put("employeeId", "");
					}
					if(userDetails.getEmployeeId()!=null && userDetails.getEmployeeId().getIdentificationTypeId()!=null)
					{
						object.put("identificationTypeId", userDetails.getEmployeeId().getIdentificationTypeId());
					}
					else
					{
						object.put("identificationTypeId", "");
					}
					if(userDetails.getCityId()!=null)
					{	
					object.put("cityIdUsers", userDetails.getCityId());
					}
					if(userDetails.getDistrictId()!=null)
					{
						String districtId=userDetails.getDistrictId();
						if(districtId.endsWith(","))
						{
							districtId = districtId.substring(0,districtId.length() - 1);
						}
					object.put("distIdUsers", districtId);
					}
					if(userDetails.getStateId()!=null)
					{	
					object.put("stateIdUsers", userDetails.getStateId());
					}
					if(userDetails.getVendorId()!=null)
					{	
					    
						object.put("vendorIdUsers", userDetails.getVendorId().replaceAll(",$", ""));
					}
					if(userDetails.getUserFlag()!=null)
					{	
					object.put("userFlag", userDetails.getUserFlag());
					}
					if(userDetails.getUserTypeId()!=null)
					{	
					object.put("userTypeDesignation", userDetails.getMasUserType().getUserTypeName());
					}
					object.put("userId", userDetails.getUserId());
					object.put("levelOfUser", userDetails.getLevelOfUser());
					if(userDetails.getUserTypeId()!=null)
					{
						object.put("userTypeName", userDetails.getMasUserType().getUserTypeName());
						Long userTypeId=userDetails.getUserTypeId();
						List getApprovingData=userManagementDao.getApprovingDetais(userTypeId);
						try
						{
						if (getApprovingData != null && !CollectionUtils.isEmpty(getApprovingData)) {

							String authorityId = "";
							String authorityCode = "";
							String authorityName = "";
							String levelNo = "";
							String finalApproval = "";
							String orderNo = "";
							
							
							for (Iterator<?> it = getApprovingData.iterator(); it.hasNext();) {
								Object[] row = (Object[]) it.next();
								if (row[0] != null) {
									authorityId = row[0].toString();

								}

								if (row[1] != null) {

									authorityCode = row[1].toString();

								}

								if (row[2] != null) {

									authorityName = row[2].toString();

								}
								if (row[3] != null) {

									levelNo = row[3].toString();

								}
								if (row[4] != null) {

									finalApproval = row[4].toString();

								}
								if (row[5] != null) {

									orderNo = row[5].toString();

								}
								
							}
							object.put("authorityId", authorityId);
							object.put("authorityCode", authorityCode);
							object.put("authorityName", authorityName);
							object.put("approvinglevelNo", levelNo);
							object.put("finalApproval", finalApproval);
							object.put("approvalOrderNo", orderNo);
							
						  }
							}catch (Exception e) {
								object.put("msg", "Successfully logged in");
								object.put("status", "1");
								e.printStackTrace();
								// TODO: handle exception
							}
					}
					object.put("userName", userDetails.getUserName());
					String mmuid=null;
					String[] mmuSplit = null;
					if(userDetails.getMmuId()!=null && !userDetails.getMmuId().equals(""))
					{
						mmuid=userDetails.getMmuId().replaceAll(",$", "");
						 mmuSplit = mmuid.split(",");
						    for (int i=0; i < mmuSplit.length; i++)
						    {
						      System.out.println(mmuSplit[i]);
						    }
					}
					if(mmuid!=null && mmuSplit.length==1)
					{
						MasMMU masMMU = userManagementDao.getCityIdAndName(mmuid);
						object.put("dispensaryCityId",masMMU.getCityId());
						List mc=userManagementDao.getCampDetais(mmuid);
						object.put("mmuId", userDetails.getMmuId().replaceAll(",$", ""));
					
					try
					{
					if (mc != null && !CollectionUtils.isEmpty(mc)) {

						String campId = "";
						String campLocation = "";
						String cityId = "";
						String cityName = "";
						String departmentId = "";
						String departmentName = "";
						String mmuName = "";
						String campOrOff = "";
						
						
						for (Iterator<?> it = mc.iterator(); it.hasNext();) {
							Object[] row = (Object[]) it.next();
							if (row[0] != null) {
								campId = row[0].toString();

							}

							if (row[1] != null) {

								campLocation = row[1].toString();

							}

							if (row[2] != null) {

								cityId = row[2].toString();

							}
							if (row[3] != null) {

								departmentId = row[3].toString();

							}
							if (row[4] != null) {

								mmuName = row[4].toString();

							}
							if (row[5] != null) {

								departmentName = row[5].toString();

							}
							if (row[6] != null) {

								cityName = row[6].toString();

							}
							
							if (row[7] != null) {

								campOrOff = row[7].toString();

							}
						}
						object.put("campId", campId);
						object.put("campLocation", campLocation);
						object.put("cityId", cityId);
						object.put("departmentId", departmentId);
						object.put("mmuName", mmuName);
						object.put("departmentName", departmentName);
						object.put("cityName", cityName);
						object.put("campOrOff", campOrOff);
					  }
						}catch (Exception e) {
							object.put("msg", "Successfully logged in");
							object.put("status", "1");
							e.printStackTrace();
							// TODO: handle exception
						}
				   }
				   else if(mmuid!=null && mmuSplit.length>=1)
				   {
					   object.put("mmuIdMultiple", userDetails.getMmuId());
					   System.out.println("mmuIdMultiple="+userDetails.getMmuId());
				   }
					
					//pt.put("cityId", userDetails.getMasMMU().);
					// pt.put("loginName", userDetails.getNameDescr());
					//p.add(pt);
					
				}
				
				object.put("msg", "Successfully logged in");
				object.put("status", "1");
			
				
			} catch (Exception e) {
				System.out.println(e);
			}
			return object;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAllApplicationOfSelectedParent(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {

		JSONObject json = new JSONObject();
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> applicationMap = userManagementDao.getAllApplicationOfSelectedParent(jsonObject, request,
				response);
		List<String[]> applicationList;
		if (applicationMap.get("listApp") != null) {
			applicationList = (List<String[]>) applicationMap.get("listApp");
			if (CollectionUtils.isNotEmpty(applicationList)) {
				for (Iterator<?> iterator = applicationList.iterator(); iterator.hasNext();) {
					Map<String, Object> mapObject = new HashMap<String, Object>();
					Object[] application = (Object[]) iterator.next();
					mapObject.put("applicationName", application[0]);
					mapObject.put("parentId", application[1]);
					mapObject.put("applicationId", application[2]);
					mapObject.put("url", application[3]);
					mapObject.put("partentApplicationName", application[4]);
					mapObject.put("sequenceNo", application[5] != null ? application[5] : "0");
					list.add(mapObject);
				}
			}

			if (list != null && list.size() > 0) {
				json.put("data", list);
				json.put("count", list.size());
				json.put("status", 1);
			} else {
				json.put("data", list);
				json.put("count", list.size());
				json.put("status", 0);
			}
		}

		return json.toString();

	}

	@Override
	public String setSequenceToApplication(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		boolean flag = false;
		JSONObject json = new JSONObject();

		flag = userManagementDao.setSequenceToApplication(jsonObject);
		if (flag) {
			json.put("msg", "Record Added Successfully");
			json.put("status", 1);
		} else {
			json.put("msg", "Some error occurred");
			json.put("status", 0);
		}

		return json.toString();

	}

	@Override
	public Map<String, Object> getDistrictList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> districtList = new ArrayList<Map<String, Object>>();
		try {
			
			List<MasDistrict> list = (List<MasDistrict>) userManagementDao.getDistrictList(requestData);
			if (!list.isEmpty()) {
				for (MasDistrict district : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", district.getDistrictId());
					map2.put("code", district.getDistrictCode());
					map2.put("name", district.getDistrictName());
					districtList.add(map2);
				}
				response.put("status", true);
				response.put("list", districtList);
			} else {
				response.put("status", true);
				response.put("list", districtList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", districtList);
		return response;
	}

	@Override
	public Map<String, Object> getCityList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> cityList = new ArrayList<Map<String, Object>>();
		try {
			List<MasCity> list = userManagementDao.getCityList(requestData);
			if (!list.isEmpty()) {
				for (MasCity city : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", city.getCityId());
					map2.put("code", city.getCityCode());
					map2.put("name", city.getCityName());
					cityList.add(map2);
				}
				response.put("status", true);
				response.put("list", cityList);
			} else {
				response.put("status", true);
				response.put("list", cityList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.put("status", false);
		response.put("list", cityList);
		return response;
	}

	@Override
	public Map<String, Object> getMMUList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> mmuList = new ArrayList<Map<String, Object>>();
		try {

			List<MasMMU> list = userManagementDao.getMMUList(requestData);
			if (!list.isEmpty()) {
				for (MasMMU mmu : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", mmu.getMmuId());
					map2.put("code", mmu.getMmuCode());
					map2.put("name", mmu.getMmuName());
					mmuList.add(map2);
				}
				response.put("status", true);
				response.put("list", mmuList);
			} else {
				response.put("status", true);
				response.put("list", mmuList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", mmuList);
		return response;
	}

	@Override
	public String submitRoleAndUsersType(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String msg="";
		try {
			
			Users userLoginEntity = userManagementDao.checkLoginCredential(jsonObject.get("mobileNo").toString(), true);
			if (userLoginEntity == null) {	
			
			Users user = new Users();
				
			user.setRoleId(jsonObject.get("masRoleIdValues").toString());
			if(jsonObject.get("levelUsers").toString().equals("D"))
			{
				user.setDistrictId(jsonObject.get("userTypeValues").toString());
			}
			if(jsonObject.get("levelUsers").toString().equals("C"))
			{
				user.setCityId(jsonObject.get("userTypeValues").toString());
			}
			if(jsonObject.get("levelUsers").toString().equals("M"))
			{
				user.setMmuId(jsonObject.get("userTypeValues").toString());
			}
			if(jsonObject.get("levelUsers").toString().equals("S"))
			{
				user.setStateId(jsonObject.get("userTypeValues").toString());
			}
			if(jsonObject.get("levelUsers").toString().equals("V"))
			{
				user.setVendorId(jsonObject.get("userTypeValues").toString());
			}
			String passwordGenerate=generatePassword(6);
			user.setUserTypeId(Long.parseLong(jsonObject.get("userTypeVal").toString()));
			user.setLevelOfUser(jsonObject.get("levelUsers").toString());
			user.setUserName(jsonObject.get("nameOfUser").toString());
			user.setLoginName(jsonObject.get("mobileNo").toString());
			user.setMobileNo(jsonObject.get("mobileNo").toString());
			user.setPassword(passwordGenerate);
			user.setEmailAddress(jsonObject.get("emailId").toString());
			user.setAdminFlag("U");
			user.setUserFlag(Long.parseLong("0"));
			if(jsonObject.get("employeeId")!=null && jsonObject.get("employeeId")!="")
			{
			  user.setEmployeeIdValue(Long.parseLong(jsonObject.get("employeeId").toString()));	
			  if(jsonObject.get("levelUsers").toString().equals("M"))
			  {
				   msg=userManagementDao.updateEmpDetails(jsonObject);
			  }
			}

			Timestamp lastChgDate = new Timestamp(new Date().getTime());
			user.setLastChgDate(lastChgDate);
			Long id = userManagementDao.saveRoleAndTypeOfUsers(user);
			
			if (id != 0 || msg=="statusUpdated") {
				//String messageVal=""+jsonObject.get("nameOfUser").toString()+" Your Registration has been successfully completed in MMSSY and your username is :"+jsonObject.get("mobileNo").toString()+" and your password is :"+passwordGenerate+" Thank You";
				sendSMS(jsonObject.get("mobileNo").toString(),jsonObject.get("nameOfUser").toString(), passwordGenerate);
				//sendSMS(jsonObject.get("mobileNo").toString(), messageVal);
				json.put("status", "success");
				json.put("msg", "Record Added Successfully");
			} else {
				json.put("status", "fail");
				json.put("msg", "Record Nout Added");
			}
		  }else {
			  json.put("status", "fail");
			  json.put("msg", "Users already exists");
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public String getUsersDetailsList(HashMap<String, Object> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
     
		if (jsondata.get("employeeId") == null || jsondata.get("employeeId").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain employeeId as a  key or value or it is null\"}";
		} 
		else
		{
			    Map<String,Object> map=userManagementDao.getUserDetailsList(jsondata);
			  
			    int count = (int) map.get("count");
				List<Users> getUsersList = (List<Users>) map.get("list");
				
				if (getUsersList.size() == 0) {
					return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
				}

				else {

					List<HashMap<String, Object>> c = new ArrayList<HashMap<String, Object>>();
					
					try{
					for (Users v : getUsersList) {
						HashMap<String, Object> pt = new HashMap<String, Object>();
						if(v.getMobileNo()!=null)
						{
							pt.put("mobileNo", v.getMobileNo());
						}
						if(v.getUserName()!=null)
						{
							pt.put("userName", v.getUserName());
						}
						if(v.getUserTypeId()!=null)
						{
							pt.put("userTypeId", v.getUserTypeId());
						}
						if(v.getUserFlag()!=null)
						{
							pt.put("userFlag", v.getUserFlag());
						}
						if(v.getLevelOfUser()!=null)
						{
							pt.put("levelOfUser", v.getLevelOfUser());
						}
						if(v.getUserId()!=null)
						{
							pt.put("userId", v.getUserId());
						}
						c.add(pt);
					
					}
					if(c != null && c.size()>0){
						json.put("data", c);
						json.put("count",count);
						json.put("msg", "Visit List  get  sucessfull... ");
						json.put("status", "1");
					}else{
						return "{\"status\":\"0\",\"msg\":\"Pending Status Not Found\"}";
					}
          
					}
					 catch(Exception e)
					{
						 e.printStackTrace();
						 return "{\"status\":\"0\",\"msg\":\"Somting went wrong}";
					}
				}
				}

			/*} else
			{
				return "{\"status\":\"0\",\"msg\":\"json is not contain EMPLOYEE ID Not Found\"}";

			}*/

			return json.toString();
		}
	
	@Override
	public String getAllUserType(JSONObject jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List list = new ArrayList();
		if (jsondata != null) {
			Map<String, List<MasUserType>> mapUserType = userManagementDao.getAllUserType(jsondata);
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
	public String activeInactiveUsers(Map<String, Object> jsonData, HttpServletRequest httpServletRequest) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		Users userLoginEntity=null;
		if(!jsonData.get("isActiveStatus").equals("inactive"))
		{	
			 userLoginEntity = userManagementDao.checkActiveUsers(jsonData.get("mobileNo").toString(), true);
		}
		if (userLoginEntity == null) {
			boolean result =	userManagementDao.isActiveInactiveUsers(jsonData.get("userId").toString(),jsonData.get("isActiveStatus").toString());
			
			if(result==true)
			{	
			jsonObject.put("status", "1");
			jsonObject.put("msg", "Record Update Successfully");
			}
		}
		else
		{
			jsonObject.put("msg", "User with same mobile number is already active");	
		}
		return jsonObject.toString();
}

	@Override
	public String editUserDetails(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Long unitIdVal=null;
		String unitCodeVal="";
		Long selectedUnitId=null;
		
		
		try {
			List<Object> responseList = new ArrayList<Object>();
			Users users=userManagementDao.getUsersByUserId(Long.parseLong(jsondata.get("userIdVal").toString()));
				HashMap<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("userId", users.getUserId());
				jsonMap.put("loginName", users.getLoginName());
				jsonMap.put("userName", users.getUserName());
				jsonMap.put("signatureFileName", users.getSignatureFileName());
				if(users.getLevelOfUser().equals("M"))
				{	
					jsonMap.put("levelOfUSers","MMU");
				}
				if(users.getLevelOfUser().equals("D"))
				{	
					jsonMap.put("levelOfUSers","Dist");
				}
				if(users.getLevelOfUser().equals("C"))
				{	
					jsonMap.put("levelOfUSers","City");
				}
				if(users.getLevelOfUser().equals("S"))
				{	
					jsonMap.put("levelOfUSers","State");
				}
				if(users.getLevelOfUser().equals("V"))
				{	
					jsonMap.put("levelOfUSers","Vendor");
				}
				jsonMap.put("emailId", users.getEmailAddress());
				jsonMap.put("status", users.getUserFlag());
				
				if(users.getUserTypeId()!=null)
				{
            		MasUserType typeOfuser= userManagementDao.getMasTypeOfUsers(Long.parseLong(users.getUserTypeId().toString()));
					jsonMap.put("userTypeId", typeOfuser.getUserTypeId());
					jsonMap.put("userTypeName", typeOfuser.getUserTypeName());
				}
				else
				{
					jsonMap.put("userTypeId", "");
					jsonMap.put("userTypeName", "");
				}
				
				
				String distName="";
				if(StringUtils.isNotEmpty(users.getDistrictId()))
					distName= userManagementDao.getMasDistrictId(users.getDistrictId());	
				if(StringUtils.isNotEmpty(distName)) {
				String [] desiValueee=distName.split("##");
				if(desiValueee!=null && desiValueee.length>0) {
					jsonMap.put("distName", desiValueee[0]);
					jsonMap.put("distId", desiValueee[1]);
				}
				else {
					jsonMap.put("distName", "");
					jsonMap.put("distId", "");
				}
				}
				else {
					jsonMap.put("distName", "");
					jsonMap.put("distId", "");
					
				}
				
				String mmuName="";
				if(StringUtils.isNotEmpty(users.getMmuId()))
					mmuName= userManagementDao.getMasMmu(users.getMmuId());	
				if(StringUtils.isNotEmpty(mmuName)) {
				String [] mmuValueee=mmuName.split("##");
				if(mmuValueee!=null && mmuValueee.length>0) {
					jsonMap.put("mmuName", mmuValueee[0]);
					jsonMap.put("mmuId", mmuValueee[1]);
				}
				else {
					jsonMap.put("mmuName", "");
					jsonMap.put("mmuId", "");
				}
				}
				else {
					jsonMap.put("mmuName", "");
					jsonMap.put("mmuId", "");
					
				}
				
				String cityName="";
				if(StringUtils.isNotEmpty(users.getCityId()))
					cityName= userManagementDao.getMasCity(users.getCityId());	
				if(StringUtils.isNotEmpty(cityName)) {
				String [] cityValueee=cityName.split("##");
				if(cityValueee!=null && cityValueee.length>0) {
					jsonMap.put("cityName", cityValueee[0]);
					jsonMap.put("cityId", cityValueee[1]);
				}
				else {
					jsonMap.put("cityName", "");
					jsonMap.put("cityId", "");
				}
				}
				else {
					jsonMap.put("cityName", "");
					jsonMap.put("cityId", "");
					
				}
				
				String vendorName="";
				if(StringUtils.isNotEmpty(users.getVendorId()))
					vendorName= userManagementDao.getMasVendor(users.getVendorId());	
				if(StringUtils.isNotEmpty(vendorName)) {
				String [] vendorValueee=vendorName.split("##");
				if(vendorValueee!=null && vendorValueee.length>0) {
					jsonMap.put("vendorName", vendorValueee[0]);
					jsonMap.put("vendorId", vendorValueee[1]);
				}
				else {
					jsonMap.put("vendorName", "");
					jsonMap.put("vendorId", "");
				}
				}
				else {
					jsonMap.put("vendorName", "");
					jsonMap.put("vendorId", "");
					
				}
				
				if(StringUtils.isNotEmpty(users.getStateId()))
				{
					jsonMap.put("stateName","Chhattisgarh");
					jsonMap.put("stateId", "1");
			
				}
				else {
					jsonMap.put("stateName", "");
					jsonMap.put("stateId", "");
					
				}
				
				
				String roleNames="";
				if(StringUtils.isNotEmpty(users.getRoleId()))
				roleNames=systemAdminDao.getMasRoleByRoleId(users.getRoleId());
				if(StringUtils.isNotEmpty(roleNames)) {
				String [] desiRoleValueee=roleNames.split("##");
				if(desiRoleValueee!=null && desiRoleValueee.length>0) {
				jsonMap.put("roleNames", desiRoleValueee[0]);
				jsonMap.put("rolesId", desiRoleValueee[1]);
				}
				else {
					jsonMap.put("roleNames", "");
					jsonMap.put("rolesId", "");
				}
				}
				else {
					jsonMap.put("roleNames", "");
					jsonMap.put("rolesId", "");
					
				}
				
				if(users.getEmployeeIdValue()!=null)
				{
					jsonMap.put("empId",users.getEmployeeIdValue());	
				}
				else
				{
					jsonMap.put("empId","");	
				}
				
				responseList.add(jsonMap);
				
			if (responseList != null && responseList.size() > 0) {
				json.put("listUserData", responseList);
				json.put("unitIdForUnitA", unitIdVal);
				json.put("unitCodeVal", unitCodeVal);
				json.put("selectUnitId", selectedUnitId);
				
				json.put("status", 1);
			} else {
				json.put("data", responseList);
				json.put("msg", "Data not found");
				json.put("status", 0);
				json.put("unitIdForUnitA", unitIdVal);
				json.put("unitCodeVal", unitCodeVal);
				json.put("selectUnitId", selectedUnitId);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	
	@Override
	public String sendSMS(String mobile, String name,String password) {

		try {
			
			final String uri ="https://2factor.in/API/R1/?module=TRANS_SMS&apikey=5cdc6365-22b5-11ec-a13b-0200cd936042&to="+mobile+
					"&from=CGMMSY&templatename=Username-New&var1="+name+"&var2="+mobile+"&var3="+password;

			MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);

			System.out.println(responseObject.toString());
			System.out.println("SMS send succefully");
			return responseObject;
		} catch (Exception e) {

			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}
	
	/*@Override
	public String sendSMS(String mobile, String messageVal) {

		try {
            String getMobile=mobile;
            String message=messageVal;
			final String uri = "https://sms.weblinto.com/smsapi/index?key=2614C35C9E9BDF&campaign=689&routeid=6&type=text&contacts="+getMobile+"&senderid=VESIPL&msg=Dear "+message+" -VESIPL";
			
			MultiValueMap<String,String> requestHeaders = new LinkedMultiValueMap<String, String>();
			RestTemplate restTemplate = new RestTemplate();
			String responseObject = restTemplate.postForObject(uri, requestHeaders, String.class);
			
			System.out.println(responseObject.toString());
			System.out.println("SMS send succefully");
			return responseObject;
		} catch (Exception e) {
			
			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}*/
	
	private static String generatePassword(int length) { // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
 
        for (int i = 0; i < length; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        System.out.println("Password : "+sb.toString());
 
        return sb.toString();
    }

	@Override
	public String updateUsersRegistartionType(HashMap<String, Object> jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String userList = null;
		try {
			if (!jsonObject.isEmpty()) {
				
				userList = userManagementDao.updateUsersDetails(jsonObject);
		// TODO Auto-generated method stub
				if (userList != null && userList.equalsIgnoreCase("statusUpdated")) {
					json.put("msg", "usersUpdated");
					json.put("status", "statusUpdated");
				} else if (userList != null && userList.equalsIgnoreCase("403")) {
					json.put("msg", " you are not authorized for this activity ");
					json.put("status", "0");
				} else {
					json.put("msg", userList);
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
	
	@Override
	public JSONObject checkUserName(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		Users userLoginEntity = userManagementDao.checkLoginCredential(jsondata.get("userName").toString(), true);
		if (userLoginEntity == null) {
			return ProjectUtils.getReturnMsg("0", "Sorry, we couldn't find an account with that Username");
		}
		
		JSONObject object = new JSONObject();
		object.put("status", "1");
		object.put("success", userLoginEntity.toString());
				
		return object;
	}

	@Override
	public String sendOtp(HashMap<String, Object> jsondata, HttpServletRequest request)
	{
		Long mobileNo=Long.parseLong(jsondata.get("mobileNo").toString());
		try {
		final String uri = "https://2factor.in/API/V1/5cdc6365-22b5-11ec-a13b-0200cd936042/SMS/"
				+ mobileNo + "/AUTOGEN/Upayog-New";
		HttpResponse<String> response = Unirest.post(uri).asString();
		System.out.println(response.getBody());
		System.out.println("OTP Generation is completed");
		return response.getBody();
	} catch (Exception e) {
		return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
	}
  }

	@Override
	public String verifyOtp(HashMap<String, Object> jsonData, HttpServletRequest request) {

		try {

			System.err.println();
			System.err.println();
			final String uri = "https://2factor.in/API/V1/5cdc6365-22b5-11ec-a13b-0200cd936042/SMS/VERIFY/"
					+ jsonData.get("session_id").toString() + "/" + Long.parseLong(jsonData.get("otp").toString());
			HttpResponse<String> response = Unirest.post(uri)
					.header("content-type", "application/x-www-form-urlencoded").asString();
			System.out.println(response.getBody());
			System.out.println("OTP Verification is completed OTP Verification");
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return ProjectUtils.getReturnMsg("0", "We are unable to process your request").toString();
		}
	}

	@Override
	public JSONObject checkUserNameEmployee(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		EmployeeRegistration userLoginEntity = userManagementDao.checkUserNameEmployee(jsondata.get("userName").toString(), true);
		JSONObject object = new JSONObject();
		if (userLoginEntity == null) {
			return ProjectUtils.getReturnMsg("0", "Sorry, we couldn't find an account with that Username");
		}
		else
		{
			String userName=userLoginEntity.getEmployeeName();
			String mobilNo=userLoginEntity.getMobileNo();
			Long typeOfUser=userLoginEntity.getUserTypeId().getUserTypeId();
			Long empId=userLoginEntity.getEmployeeId();

			object.put("userName", userName);
        	object.put("typeOfUser", typeOfUser);
        	object.put("mobilNo", mobilNo);
        	object.put("empId", empId);
		}
		
		
		
		object.put("status", "1");
		object.put("success", userLoginEntity.toString());
				
		return object;
	}

	@Override
	public JSONObject closeIdleTranaction(HashMap<String, Object> jsondata, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		String closeIdleTrans=userManagementDao.closeIdleTrans();
		
		return null;
	}

	@Override
	public String getFilteredUsers(JSONObject jsondata){
		JSONObject json = new JSONObject();
		try {
			List<Object[]> usersList = new ArrayList<>();
			List list = new ArrayList();
			if (jsondata != null) {
				Map<String, List<Object[]>> map = userManagementDao.filterUsers(jsondata);
				List totalMatches = new ArrayList();
				if (map.get("usersList") != null) {
					usersList = map.get("usersList");
					totalMatches = map.get("totalMatches");
					usersList.forEach(arr -> {
						HashMap<String, Object> mapObj = new HashMap<String, Object>();
						mapObj.put("userId", arr[0].toString());
						mapObj.put("userName", arr[1].toString());
						list.add(mapObj);
					});

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
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return json.toString();
	}
	
	@Override
	public Map<String, Object> getVendorList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<Map<String, Object>> mmuList = new ArrayList<Map<String, Object>>();
		try {

			List<MasMmuVendor> list = userManagementDao.getVendorList(requestData);
			if (!list.isEmpty()) {
				for (MasMmuVendor mmu : list) {
					Map<String, Object> map2 = new HashMap<>();
					map2.put("id", mmu.getMmuVendorId());
					map2.put("code", mmu.getMmuVendorCode());
					map2.put("name", mmu.getMmuVendorName());
					mmuList.add(map2);
				}
				response.put("status", true);
				response.put("list", mmuList);
			} else {
				response.put("status", true);
				response.put("list", mmuList);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.put("status", false);
		response.put("list", mmuList);
		return response;
	}

	 
	 
	@Override
	public String getAuthenticateUser(JSONObject jsondata) {
		JSONObject json = new JSONObject();
	 
		String status="";
		try {

			  status = userManagementDao.getPatientValid(jsondata);
			 
			if (status!=null) {
				json.put("status", status);
				 
			} else {
				json.put("status", status);
				 
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			status="Something is wrong"+"##"+"0";
			json.put("status", status);
		}
		
		 
		return json.toString();
	}

	@Override
	public String getUsersList(HashMap<String, String> jsondata, HttpServletRequest request) {
		JSONObject json = new JSONObject();
        try
        {
		if (jsondata.get("userName") == null || jsondata.get("userName").toString().trim().equals(""))
		{
			return "{\"status\":\"0\",\"msg\":\"json is not contain userName as a  key or value or it is null\"}";
		} 
		else
		{
				
				List<Users> mst_users = userManagementDao.getUsersList(jsondata);
			    if (mst_users.size() == 0)
			    {
			    	 return "{\"status\":\"0\",\"msg\":\"Data not found\"}";
			    } 
			    else 
			    {
			    
			    	json.put("usersList", mst_users);
			    	json.put("msg", "mst_users List  get  sucessfull... ");
			    	json.put("status", "1");

			   }

		

		return json.toString();
	}
        }finally
        {
        	System.out.println("Hi");
        }
	}

	
}
