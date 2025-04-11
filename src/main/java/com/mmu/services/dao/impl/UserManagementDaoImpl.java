package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.mmu.services.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.RoleTemplateDao;
import com.mmu.services.dao.UserManagementDao;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.UserManagementService;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.JavaUtils;


@Repository
@Transactional
public class UserManagementDaoImpl implements UserManagementDao {

	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String mas_application=databaseScema+"."+"mas_application";
	
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	UserManagementService userManagementService;
	@Autowired
	RoleTemplateDao roleTemplateDao;
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<UserApplication>> getAllUserApplication(JSONObject jsonObject) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageNo").trim());

		Map<String, List<UserApplication>> mapObj = new HashMap<String, List<UserApplication>>();
		List<UserApplication> userAppList = new ArrayList<UserApplication>();
		@SuppressWarnings("rawtypes")
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(UserApplication.class);

			if (jsonObject.get("PN") != null)
				pageNo = Integer.parseInt(jsonObject.get("PN").toString());

			String appName = "";
			if (jsonObject.has("appName")) {
				appName = jsonObject.get("appName") + "%";
				if (jsonObject.get("appName").toString().length() > 0
						&& !jsonObject.get("appName").toString().trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("appName", appName));
				}
			}
			criteria.addOrder(Order.asc("appName"));
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			userAppList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("userAppList", userAppList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserApplication> validateUserApplication(String applicationName, String url) {
		List<UserApplication> userAppList = new ArrayList<UserApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().getCurrentSession();
			Criteria criteria = session.createCriteria(UserApplication.class);
					if(!url.equalsIgnoreCase("#")) {
						criteria.add(Restrictions.or(Restrictions.eq("appName", applicationName), Restrictions.eq("url", url)));
					}else {
						criteria.add(Restrictions.and(Restrictions.eq("appName", applicationName),Restrictions.eq("url", url)));
					}
			userAppList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return userAppList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateUserApplication(JSONObject jsonObject) {
		String result = "";
		Timestamp timestamp = new Timestamp(new Date().getTime());
		try {
			if (jsonObject != null) {
				
				List<UserApplication> userApplicationList = new ArrayList<UserApplication>();
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(UserApplication.class);
				String uStatus = "";
				Long appId;
				if (jsonObject.has("id")) {
					appId = Long.parseLong(jsonObject.get("id").toString());
					criteria.add(Restrictions.eq("id", appId));
					userApplicationList = criteria.list();

					for (int i = 0; i < userApplicationList.size(); i++) {
						Long applicationId = userApplicationList.get(i).getId();
						
						Object object = session.load(UserApplication.class, applicationId);
						UserApplication userApplication = (UserApplication) object;

						Transaction transaction = session.beginTransaction();
						if (jsonObject.has("status")) {							
							uStatus = jsonObject.get("status").toString();
							
							if (uStatus.equalsIgnoreCase("active") || uStatus.equalsIgnoreCase("inactive")) {
								if (uStatus.equalsIgnoreCase("active"))
									userApplication.setStatus("Y");
								else
									userApplication.setStatus("N");
								session.update(userApplication);
								transaction.commit();
								result = "200";

							} else {
								userApplication.setAppName(jsonObject.get("appName").toString());//.toUpperCase());
								userApplication.setUrl(jsonObject.get("url").toString());
								
								/*Users user = new Users();
								user.setUserId(new Long(1));
								userApplication.setUsers(user);*/

								userApplication.setLastChgDate(timestamp);
								session.update(userApplication);
								transaction.commit();
								result = "200";
							}

						}
					}

				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<MasTemplate>> getAllTemplate(JSONObject jsonObject) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo = 0;
		long hospitalId = 1;
		Map<String, List<MasTemplate>> mapObj = new HashMap<String, List<MasTemplate>>();
		List<MasTemplate> templateList = new ArrayList<MasTemplate>();
		@SuppressWarnings("rawtypes")
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTemplate.class);
			if (jsonObject.get("PN") != null) {
				pageNo = Integer.parseInt(jsonObject.get("PN").toString());

				String tempName = "";
				if (jsonObject.has("templateName")) {
					tempName = jsonObject.get("templateName") + "%";
					if (jsonObject.get("templateName").toString().length() > 0
							&& !jsonObject.get("templateName").toString().trim().equalsIgnoreCase("")) {
						criteria.add(Restrictions.ilike("templateName", tempName));
					}
				}

				criteria.addOrder(Order.asc("templateName"));
				totalMatches = criteria.list();

				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
				templateList = criteria.list();

			}

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("templateList", templateList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> getApplicationAutoComplete(JSONObject jsonObject) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		List<UserApplication> userApplicationsList = new ArrayList<UserApplication>();
		List<MasApplication> maxAppIdlist = new ArrayList<MasApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class);
			//criteria.setProjection(Projections.max("applicationId").as("applicationId"));
			//criteria.addOrder(Order.asc("applicationId"));
			criteria.addOrder(Order.desc("orderNo"));
			maxAppIdlist = criteria.list();
			
			Criteria criteria1 = session.createCriteria(UserApplication.class);
			if (jsonObject.has("icdName")) {
				String appName = "%" + jsonObject.get("icdName") + "%";
				if (jsonObject.get("icdName").toString().length() > 0
						&& !jsonObject.get("icdName").toString().trim().equalsIgnoreCase("")) {
					criteria1.add(Restrictions.like("appName", appName).ignoreCase());
				}
			}

			userApplicationsList = criteria1.list();

			mapObj.put("maxAppIdlist", maxAppIdlist);
			mapObj.put("userApplicationsList", userApplicationsList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String saveUserApplication(JSONObject jsonObject) {
		String result = "";
		try {
			UserApplication userApplication = new UserApplication();
			String applicationName = jsonObject.get("appName").toString();
			String url = jsonObject.get("url").toString();
			String status = "Y";
			Long userId = Long.parseLong(jsonObject.get("userId").toString());
			Timestamp lastChgDate = new Timestamp(new Date().getTime());
			
			userApplication.setAppName(applicationName);
			userApplication.setUrl(url);
			userApplication.setStatus(status);
			/*
			Users users = new Users();
			users.setUserId(userId);
			userApplication.setUsers(users);	
			*/	
			userApplication.setLastChgBy(userId);
			userApplication.setLastChgDate(lastChgDate);
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Serializable sezObject = session.save(userApplication);

			if (sezObject != null) {
				result = "200";
			} else {
				result = "300";
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateTemplate(JSONObject jsonObject) {
		String result = "";
		Timestamp timestamp = new Timestamp(new Date().getTime());
		try {
			if (jsonObject != null) {
				
				List<MasTemplate> templateList = new ArrayList<MasTemplate>();
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasTemplate.class);
				String hStatus = "";
				Long appId;
				if (jsonObject.has("templateId")) {
					appId = Long.parseLong(jsonObject.get("templateId").toString());
					criteria.add(Restrictions.eq("id", appId));
					templateList = criteria.list();

					for (int i = 0; i < templateList.size(); i++) {
						Long tempId = templateList.get(i).getTemplateId();

						Object object = session.load(MasTemplate.class, tempId);
						MasTemplate masTemplate = (MasTemplate) object;

						Transaction transaction = session.beginTransaction();

						if (jsonObject.has("status")) {
							hStatus = jsonObject.get("status").toString();

							if (hStatus.equalsIgnoreCase("active") || hStatus.equalsIgnoreCase("inactive")) {
								if (hStatus.equalsIgnoreCase("active"))
									masTemplate.setStatus("Y");
								else
									masTemplate.setStatus("N");
								session.update(masTemplate);
								transaction.commit();
								result = "201";

							} else {
								masTemplate.setTemplateCode(jsonObject.get("templateCode").toString());//.toUpperCase());
								masTemplate.setTemplateName(jsonObject.get("templateName").toString());//.toUpperCase());

								Users user = new Users();
								user.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
								masTemplate.setUser(user);

								masTemplate.setLastChgDate(timestamp);
								session.update(masTemplate);
								transaction.commit();
								result = "200";
							}

						}
					}

				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasTemplate> validateTemplate(String templateCode, String templateName) {
		List<MasTemplate> masTemplatesList = new ArrayList<MasTemplate>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTemplate.class).add(Restrictions
					.or(Restrictions.eq("templateCode", templateCode), Restrictions.eq("templateName", templateName)));
			masTemplatesList = criteria.list();
		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masTemplatesList;
	}

	@Override
	//public String saveTemplate(MasTemplate masTemplate) {
	public String saveTemplate(JSONObject jsonObject) {
		String templateCode = jsonObject.get("templateCode").toString();
		String templateName = jsonObject.get("templateName").toString();
		String flag = "Y";
		Long hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());
		/*
		MasHospital masHospital = new MasHospital();
		masHospital.setHospitalId(Long.parseLong(hospitalId));
		*/
		String userId = jsonObject.get("userId").toString();
		/*
		Users user = new Users();
		user.setUserId(Long.parseLong(userId));
		*/
		Timestamp lastChgDate = new Timestamp(new Date().getTime());
		
		
		
		MasTemplate masTemplate = new MasTemplate();
		
		masTemplate.setTemplateCode(templateCode);
		masTemplate.setTemplateName(templateName);
		masTemplate.setStatus(flag);
		masTemplate.setLastChgDate(lastChgDate);
		masTemplate.setHospitalId(hospitalId);
		//masTemplate.setUser(user);
		
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Serializable sezObject = session.save(masTemplate);

			if (sezObject != null) {
				result = "200";
			} else {
				result = "300";
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasTemplate> getTemplateList() {
		List<MasTemplate> tempList = new ArrayList<MasTemplate>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTemplate.class).add(Restrictions.eq("status", "Y"));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("templateId").as("templateId"));
			projectionList.add(Projections.property("templateName").as("templateName"));
			criteria.setProjection(projectionList).addOrder(Order.asc("templateName"));
			tempList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasTemplate.class)).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return tempList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<TemplateApplication>> getModuleNameTemplateWise(JSONObject jsonObject) {
		List<TemplateApplication> templateAppList = new ArrayList<TemplateApplication>();
		Map<String, List<TemplateApplication>> mapObject = new HashMap<String, List<TemplateApplication>>();
		long tempId = 0;
		if (jsonObject.has("templateId")) {

			if (jsonObject.get("templateId").toString().length() != 0) {
				tempId = Long.parseLong(jsonObject.get("templateId").toString());
			}
		}
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(TemplateApplication.class, "templateapp")
					.createAlias("templateapp.masTemplate", "mastemplate")
					.createAlias("templateapp.masApplication", "masapplication")
					.add(Restrictions.eq("mastemplate.templateId", tempId))
					.add(Restrictions.eq("masapplication.parentId", "0"))
					.add(Restrictions.eq("status", "y").ignoreCase());

			
			List<TemplateApplication> listObj = criteria.list();
			
			Iterator itr = listObj.iterator();
			while(itr.hasNext()) {
				System.out.println("listObj :::: >>>"+itr.next());
			}
			
			templateAppList = (List<TemplateApplication>) listObj.stream().distinct().collect(Collectors.toList());
			// templateAppList = criteria.setResultTransformer(new
			// AliasToBeanResultTransformer(TemplateApplication.class)).list();
			mapObject.put("templateAppList", templateAppList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasApplication> getModuleListForTemplate() {
		List<MasApplication> moduleList = new ArrayList<MasApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class).add(Restrictions.or(
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.eq("parentId", "0")), 
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.eq("parentId", "0"))))
					.addOrder(Order.asc("applicationName"));
			moduleList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return moduleList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasApplication> addFormsReportParentChildHierarchy(JSONObject jsonObject) {
		List<MasApplication> moduleList = new ArrayList<MasApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class).add(Restrictions.or(
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.eq("parentId", "0")), 
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.ne("parentId", "0"))))
					.addOrder(Order.asc("applicationName"));
					
					if (jsonObject.has("icdName")) {
						String appName = "%" + jsonObject.get("icdName") + "%";
						if (jsonObject.get("icdName").toString().length() > 0
								&& !jsonObject.get("icdName").toString().trim().equalsIgnoreCase("")) {
							criteria.add(Restrictions.like("applicationName", appName).ignoreCase());
						}
					}
			
			
			moduleList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return moduleList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> populateApplications(JSONObject jsonObject) {
		Map<String, Object> mapObject = new HashMap<String, Object>();
		List<MasApplication> masAppList = new ArrayList<MasApplication>();
		String parentId = "";
		if (jsonObject != null) {
			parentId = jsonObject.get("parentId").toString();

			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasApplication.class, "masapplication")
						.add(Restrictions.eq("parentId", parentId));
				masAppList = criteria.list();
				mapObject.put("masAppList", masAppList);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		}
		return mapObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean validateAddFormAndReports(MasApplication masApplication) {
		List<MasApplication> listMasApp = new ArrayList<MasApplication>();
		boolean flag = false;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class);
				Criterion cr1=Restrictions.eq("parentId", masApplication.getParentId());
				Criterion cr2=Restrictions.eq("url", masApplication.getUrl());
			LogicalExpression expeee=Restrictions.and(cr1, cr2);
					criteria.add(expeee) ;
										
			listMasApp = criteria.list();
			if(!listMasApp.isEmpty() && listMasApp.size()>0) {
				if(masApplication.getApplicationName().equalsIgnoreCase(listMasApp.get(0).getApplicationName())) {
			
				flag = true;
			}else {
				flag = false;
			}
					
		}else {
			flag = false;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	@Override
	public String addFormAndReports(MasApplication masApplication, Long appID ) {
		String result="";
		
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();
		try {
			
			Serializable saveObj = session.save(masApplication);
			if(saveObj!=null) {
				result = "200";
			}else {
				result = "201";
			}
			
				UserApplication userApplication = (UserApplication)session.load(UserApplication.class,appID);
			
			if(userApplication.getStatus().equalsIgnoreCase("Y")) {
				userApplication.setStatus("N");				
			}else {
				userApplication.setStatus("Y");
			}
			session.update(userApplication);
			
			tx.commit();
		}catch(Exception e) {
			
			tx.rollback();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateUserApplicationStatus(long appID) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
			UserApplication userApplication = (UserApplication)session.load(UserApplication.class,appID);
			
			if(userApplication.getStatus().equalsIgnoreCase("Y")) {
				userApplication.setStatus("N");				
			}else {
				userApplication.setStatus("Y");
			}
			session.update(userApplication);
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllApplicationAndTemplates(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> mapObject = new HashMap<String, Object>();
		List<String[]> listApp = new ArrayList<String[]>();
		List<MasApplication> masApp = new ArrayList<MasApplication>();
		List<String[]> existintemplateAppList = new ArrayList<String[]>();
		String templateId="";
		String parentId="";
		if(jsonObject!=null) {			
			parentId = jsonObject.get("parentId").toString();
			templateId = jsonObject.get("templateId").toString();
		}
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//Criteria criteria = session.createCriteria(MasApplication.class)
			/*String entityQuery = "select ma1.name, ma1.parent_id, ma1.app_id, ma1.url,ma2.name as name2, t.status as status, t.TEMP_APP_ID as TEMP_APP_ID " + 
								" from MAS_APPLICATION ma1 " + 
								" left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
								" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id " + 
								" where ma1.parent_id='"+parentId+"' " + 
								" union all " + 
								" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as name2, t.status as status, t.TEMP_APP_ID as TEMP_APP_ID " + 
								" from MAS_APPLICATION ma1 " + 
								" left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
								" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id " + 
								" where ma1.parent_id in ( select app_id from mas_application where parent_id='"+parentId+"') " + 
								" union all " + 
								" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as name2,t.status as status, t.TEMP_APP_ID as TEMP_APP_ID " + 
								" from MAS_APPLICATION ma1 " + 
								" left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
								" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id " + 
								" where ma1.app_id ='"+parentId+"'";*/
			String entityQuery = "select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa , " + 
							" t.status as status, " + 
							" t.TEMP_APP_ID as TEMP_APP_ID " + 
							" from mas_application ma1 left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id  and t.TEMPLATE_ID='"+templateId+"' " + 
							" where ma1.parent_id='"+parentId+"' " + 
							" union all  " + 
							" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa, " + 
							" t.status as status," + 
							" t.TEMP_APP_ID as TEMP_APP_ID  " + 
							" from mas_application ma1 left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id and t.TEMPLATE_ID='"+templateId+"' " + 
							" where ma1.parent_id in ( select app_id from mas_application where parent_id='"+parentId+"') " + 
							" union all  " + 
							" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa, " + 
							" t.status as status, t.TEMP_APP_ID as TEMP_APP_ID  " + 
							" from mas_application ma1 " + 
							" left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" left outer join TEMPLATE_APPLICATION t on ma1.app_id=t.app_id and t.TEMPLATE_ID='"+templateId+"'" + 
							" where ma1.app_id ='"+parentId+"'" ;
									
			SQLQuery sqlQuery = session.createSQLQuery(entityQuery);
			listApp = sqlQuery.list();
			
			masApp  = session.createCriteria(MasApplication.class)
                    .add(Restrictions.eq("parentId", parentId))
                    .add(Restrictions.eq("url", "#")).list();
			
			String parentid = "";
			
			if(parentId != null && !parentId.isEmpty())
				parentid = parentid +"'"+ parentId+"'";
			for(MasApplication masAp : masApp){
				if(parentid.length() == 0)
					parentid = parentid + "'"+masAp.getApplicationId()+"'";
				else
					parentid = parentid + ",'" + masAp.getApplicationId()+"'";				
			}
			String checkIntotempApplicationQry = "SELECT masApplication.parent_id,tempApplication.app_id,tempApplication.template_id,tempApplication.STATUS " + 
					" FROM TEMPLATE_APPLICATION tempApplication, mas_application masApplication " + 
					" where tempApplication.app_id = masApplication.app_id " + 
					" and tempApplication.template_id = '"+templateId+"' and masApplication.parent_id in ("+parentid+")";
			existintemplateAppList = (List) session.createSQLQuery(checkIntotempApplicationQry).list();
	
			mapObject.put("listApp", listApp);
			mapObject.put("existintemplateAppList", existintemplateAppList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObject;
	}

	@Override
	public String addTemplateApplication(TemplateApplication templateApplication) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
			   session.saveOrUpdate(templateApplication);
			   Long savedObj=templateApplication.getTempAppId();
			if(savedObj!=null) {
				result ="200";
			}else {
				result = "201";
			}
			tx.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public List<MasRole> getRoleRightsList() {
		List<MasRole> roleList = new ArrayList<MasRole>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasRole.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("roleId").as("roleId"));
			projectionList.add(Projections.property("roleName").as("roleName"));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("status", "Y").ignoreCase());
			criteria.addOrder(Order.asc("roleName"));
			roleList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasRole.class)).list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	public List<MasTemplate> getTemplateNameList() {
		List<MasTemplate> tempNameList = new ArrayList<MasTemplate>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasTemplate.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("templateId").as("templateId"));
			projectionList.add(Projections.property("templateName").as("templateName"));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("status", "Y"));
			criteria.addOrder(Order.asc("templateName"));
			tempNameList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasTemplate.class)).list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tempNameList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Users> getUserAndHospitalFromServiceNo(Long userId) {
		List<Users> userList = new ArrayList<Users>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			  userList = session.createCriteria(Users.class)
			  .add(Restrictions.eq("userId", userId)).list();
			 
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return userList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TemplateApplication> getApplicationNameBasesOnRole(Object roleName) {
		List<MasRole> roleList = new ArrayList<MasRole>();
		List<RoleTemplate> templateIdList = new ArrayList<RoleTemplate>();
		List<TemplateApplication> applicationNameList = new ArrayList<TemplateApplication>();
		long roleId=0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			roleList = session.createCriteria(MasRole.class)
			.add(Restrictions.eq("roleName", roleName))
			.add(Restrictions.eq("status", "y").ignoreCase())
			.list();
			
			if(roleList!=null && roleList.size()>0) {
				roleId = roleList.get(0).getRoleId();
				
				templateIdList=session.createCriteria(RoleTemplate.class)
				.add(Restrictions.eq("masRole.roleId", roleId))
				.add(Restrictions.eq("status", "y").ignoreCase())
				.setProjection(Projections.property("masTemplate.templateId"))
				.list();
				
				if(templateIdList!=null && templateIdList.size()>0) {
					applicationNameList=session.createCriteria(TemplateApplication.class)
							.createAlias("masApplication", "application")
					.add(Restrictions.in("masTemplate.templateId", templateIdList))
					.add(Restrictions.eq("status", "y").ignoreCase())
					.list();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return applicationNameList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RoleTemplate> getAssingedTemplateNameList(JSONObject json) {
		
		List<RoleTemplate> rolelist = new ArrayList<RoleTemplate>();
		MasRole mas=new MasRole();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			mas.setRoleId(json.getLong("roleId"));	
			 rolelist = session.createCriteria(RoleTemplate.class).add(Restrictions.eq("msRole", mas)).list();		

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return rolelist;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public String saveRolesRight(JSONObject json) {
		
		String result = "";
		boolean success = false;
		MasTemplate maTemplate = null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction tx = session.beginTransaction();		
		String roleid = (String) json.get("roleId");
		//Long hospitalId=Long.parseLong(json.get("hospitalId").toString());
		JSONArray jsonArray = json.getJSONArray("templateid");
		String[] masTemp = null;
		if (jsonArray != null) {
			int length = jsonArray.length();
			masTemp = new String[length];
			for (int i = 0; i < length; i++) {
				masTemp[i] = jsonArray.optString(i);
			}
		}

		try {
			if (masTemp != null && masTemp.length > 0) {

				for (String masTemplatee : masTemp) {
					 JSONObject jsonobj=new JSONObject(masTemplatee);					 
					RoleTemplate roleTemplate = null;
					maTemplate = new MasTemplate();
					maTemplate.setTemplateId(jsonobj.getLong("tid"));
					MasRole masRole = new MasRole();
					masRole.setRoleId(Long.parseLong(roleid));

					List<RoleTemplate> roleTemplateList = session.createCriteria(RoleTemplate.class)
							.add(Restrictions.eq("msRole", masRole)).add(Restrictions.eq("msTemplate", maTemplate))
							.list();

					if (CollectionUtils.isNotEmpty(roleTemplateList)) {
						roleTemplate = roleTemplateList.get(0);
						
					}

					if (roleTemplate == null) {
						roleTemplate = new RoleTemplate();
					}

					long d = System.currentTimeMillis();
					Date date = new Date(d);
					roleTemplate.setLastChgDate(date);
					
					Users users = new Users();
					users.setUserId(Long.parseLong(json.get("userId").toString()));						
					roleTemplate.setStatus(jsonobj.get("status").toString());
					roleTemplate.setMsRole(masRole);
					maTemplate.setTemplateId(jsonobj.getLong("tid"));
					roleTemplate.setMsTemplate(maTemplate);
					roleTemplate.setUser(users);
					session.saveOrUpdate(roleTemplate);
					success = true;
				}
			}
			tx.commit();

			if (success) {
				result = "200";
			} else {
				result = "500";
			}

		} catch (Exception e) {
			result = "500";
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasApplication> getApplicationNameFormsAndReport(JSONObject jsonObject) {
		
		List<MasApplication> applicationsList = new ArrayList<MasApplication>();
		try {
			String appName="";
			String applicationName="";
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class);
			
			//new code added 
			/*Criteria criteria = session.createCriteria(MasApplication.class).add(Restrictions.or(
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.eq("parentId", "0")), 
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.ne("parentId", "0"))))
					.addOrder(Order.asc("applicationName"));*/
			
			//if (jsonObject.has("applicationName")) {
			if (jsonObject.has("icdName")) {
				String applicationName1 = jsonObject.get("icdName").toString();
					int aIndex1 = applicationName1.lastIndexOf("[");
					int aIndex2 = applicationName1.lastIndexOf("]");
					aIndex1++;
					String applicationNames = JavaUtils.getReplaceString(applicationName1).trim();
				    String [] applicationNamee = applicationNames.split(",");
				    applicationName = applicationNamee[0];
				
				appName = "%" + applicationName + "%";
				if (applicationName.length() > 0 && !applicationName.trim().equalsIgnoreCase("")) {
					criteria.add(Restrictions.ilike("applicationName", appName));
					
				}
			}
			
			applicationsList = criteria.list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return applicationsList;
	}

	@Override
	public String updateAddFormsAndReport(MasApplication masApplication) {
		String result="";
		try {
			if(masApplication!=null) {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Object object  = session.load(MasApplication.class, masApplication.getApplicationId());
				MasApplication applicationObject = (MasApplication) object;
				 
					Transaction tx = session.beginTransaction();
					
					//applicationObject.setApplicationId(masApplication.getApplicationId());
					applicationObject.setApplicationName(masApplication.getApplicationName());//.toUpperCase());
					applicationObject.setParentId(masApplication.getParentId());
					applicationObject.setUrl(masApplication.getUrl());
					applicationObject.setStatus(masApplication.getStatus());
					//applicationObject.setOrderNo(23);
					session.saveOrUpdate(applicationObject);					
					tx.commit();
					result = "200";
				}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDesignationMapping> getDesignationList(JSONObject jsonObject) {
		List<MasDesignationMapping> designationList = new ArrayList<MasDesignationMapping>();
		List<MasDesignationMapping> listMasDesignationMapping = null;
		List<MasHospital> hospitalList = new ArrayList<MasHospital>();
		Long hospitalId = null;
		
		
		String designationIds="";
		String[] designationIdValues = null;
		
		hospitalId=  Long.parseLong(jsonObject.get("hospitalId").toString().trim());
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			
			Criteria mdesigMappingCriteria = session.createCriteria(MasDesignationMapping.class)
					.add(Restrictions.eq("unitId", hospitalId));
			listMasDesignationMapping = mdesigMappingCriteria.list();
			
			if(listMasDesignationMapping!=null && !listMasDesignationMapping.isEmpty()) {
			
			for(MasDesignationMapping masDesignationMapping:listMasDesignationMapping) {
				designationIds = masDesignationMapping.getDesignationId()!=null?masDesignationMapping.getDesignationId():"";
				designationIdValues = designationIds.split(",");				
				
			}
			
			List<Long> desigIdList = new ArrayList<Long>();
			for(int i=0 ; i<designationIdValues.length; i++) {
				desigIdList.add(Long.parseLong(designationIdValues[i]));
			}
			
			Criteria criteria = session.createCriteria(MasDesignation.class).add(Restrictions.in("designationId", desigIdList));
			
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("designationId").as("designationId"));
			projectionList.add(Projections.property("designationName").as("designationName"));
			criteria.setProjection(projectionList);
			
			criteria.addOrder(Order.asc("designationName"));
			designationList = criteria.setResultTransformer(new AliasToBeanResultTransformer(MasDesignation.class)).list();
			
			}else {
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return designationList;
	}

	@Override
	public Map<String, List<RoleDesignation>> getRoleAndDesignationMappingList(JSONObject jsonObject) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageNo").trim());

		Map<String, List<RoleDesignation>> mapObj = new HashMap<String, List<RoleDesignation>>();
		List<RoleDesignation> roleDesgList = new ArrayList<RoleDesignation>();
		@SuppressWarnings("rawtypes")
		List totalMatches = new ArrayList();
		long locationId = Long.parseLong(jsonObject.get("locationId").toString());
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(RoleDesignation.class,"roledesign")
					.createAlias("masDesignation", "masDesignation").add(Restrictions.eq("locationId", locationId));
							

			if (jsonObject.get("PN") != null)
				pageNo = Integer.parseInt(jsonObject.get("PN").toString());
			
			Long designId;
			if (jsonObject.get("designationId") != null && !(jsonObject.get("designationId")).equals("")) {
				designId = Long.parseLong(jsonObject.get("designationId").toString());
				if (jsonObject.get("designationId").toString().length() > 0
						&& !jsonObject.get("designationId").toString().equalsIgnoreCase("0")) {
						
						criteria.add(Restrictions.eq("masDesignation.designationId", designId));
				}
			}
			
			totalMatches = criteria.list();

			criteria.setFirstResult((pageSize) * (pageNo - 1));
			criteria.setMaxResults(pageSize);
			roleDesgList = criteria.list();

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("roleDesgList", roleDesgList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@Override
	public String roleAndDesignationMapping(RoleDesignation roleDesignation) {
		String result="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();			
			Transaction transaction = session.beginTransaction();
			Long id = (Long)session.save(roleDesignation);
			
		
			if (id != 0) {
				result = "200";
			} else {
				result = "300";
			}
			transaction.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public String updateRoleAndDesignationMapping(JSONObject jsonObject) {
		String result = "";
		Timestamp timestamp = new Timestamp(new Date().getTime());
		try {
			if (jsonObject != null) {
				
				List<RoleDesignation> roleDesigList = new ArrayList<RoleDesignation>();
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(RoleDesignation.class);
				String uStatus = "";
				Long roleDesigId;
				if (jsonObject.has("roleDesignationId")) {
					roleDesigId = Long.parseLong(jsonObject.get("roleDesignationId").toString());
					criteria.add(Restrictions.eq("roleDesignationId", roleDesigId));
					roleDesigList = criteria.list();

					for (int i = 0; i < roleDesigList.size(); i++) {
						Long rdId = roleDesigList.get(i).getRoleDesignationId();
						
						Object object = session.load(RoleDesignation.class, rdId);
						RoleDesignation roleDesignation = (RoleDesignation) object;

						Transaction transaction = session.beginTransaction();
						if (jsonObject.has("status")) {							
							uStatus = jsonObject.get("status").toString();
							
							if (uStatus.equalsIgnoreCase("active") || uStatus.equalsIgnoreCase("inactive")) {
								if (uStatus.equalsIgnoreCase("active"))
									roleDesignation.setStatus("Y");
								else
									roleDesignation.setStatus("N");
								session.update(roleDesignation);
								transaction.commit();
								result = "statusupdated";

							} else {
								
								String rolesArr = jsonObject.get("roleId").toString();
								String rolessId="";
								
									int pidIndex1 = rolesArr.lastIndexOf("[");
									int pidIndex2 = rolesArr.lastIndexOf("]");
									pidIndex1++;
									rolessId = JavaUtils.getReplaceString1(rolesArr);
									String roleIds = rolessId.replaceAll("\"", "");
									
								
						    	roleDesignation.setRoleId(roleIds);	
								/*roleDesignation.setRoleId(jsonObject.get("roleId").toString());*/
								MasDesignation masDesignation = new MasDesignation();
								masDesignation.setDesignationId(Long.parseLong(jsonObject.get("designationId").toString()));
								roleDesignation.setMasDesignation(masDesignation);
								
								Users user = new Users();
								user.setUserId(Long.parseLong(jsonObject.get("userId").toString()));
								roleDesignation.setUser(user);
								
								roleDesignation.setLocationId(Long.parseLong(jsonObject.get("hospitalId").toString()));
								
								roleDesignation.setLastChgDate(timestamp);
								session.update(roleDesignation);
								transaction.commit();
								result = "200";
							}

						}
					}

				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}
	
	//designation master
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<MasDesignation>> getAllDesignations(JSONObject jsonObject) {
		int pageSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		int pageNo = 0;
		Map<String, List<MasDesignation>> mapObj = new HashMap<String, List<MasDesignation>>();
		List<MasDesignation> designationList = new ArrayList<MasDesignation>();
		@SuppressWarnings("rawtypes")
		List totalMatches = new ArrayList();

		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDesignation.class);

			if (jsonObject.get("PN") != null) {
				pageNo = Integer.parseInt(jsonObject.get("PN").toString());

				String desigName = "";
				if (jsonObject.has("designationName")) {
					desigName = "%" +jsonObject.get("designationName") + "%";
					if (jsonObject.get("designationName").toString().length() > 0
							&& !jsonObject.get("designationName").toString().trim().equalsIgnoreCase("")) {
						criteria.add(Restrictions.like("designationName", desigName));
					}
				}

				criteria.addOrder(Order.asc("designationName"));
				totalMatches = criteria.list();

				criteria.setFirstResult((pageSize) * (pageNo - 1));
				criteria.setMaxResults(pageSize);
				designationList = criteria.list();

			}

			mapObj.put("totalMatches", totalMatches);
			mapObj.put("designationList", designationList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String updateDesignationDetails(JSONObject jsonObject) {
		String result = "";
		Timestamp lastChgDate = new Timestamp(new Date().getTime());
		try {
			if (jsonObject != null) {
				
				List<MasDesignation> designationsList = new ArrayList<MasDesignation>();
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(MasDesignation.class);
				String hStatus = "";
				Long desigId;
				if (jsonObject.has("designationId")) {
					desigId = Long.parseLong(jsonObject.get("designationId").toString());
					criteria.add(Restrictions.eq("designationId", desigId));
					designationsList = criteria.list();

					for (int i = 0; i < designationsList.size(); i++) {
						Long dId = designationsList.get(i).getDesignationId();

						Object object = session.load(MasDesignation.class, dId);
						MasDesignation masDesignation = (MasDesignation) object;

						Transaction transaction = session.beginTransaction();

						if (jsonObject.has("status")) {
							hStatus = jsonObject.get("status").toString();

							if (hStatus.equalsIgnoreCase("active") || hStatus.equalsIgnoreCase("inactive")) {
								if (hStatus.equalsIgnoreCase("active"))
									masDesignation.setStatus("Y");
								else
									masDesignation.setStatus("N");
									session.update(masDesignation);
									transaction.commit();
									result = "201";

							} else {
								masDesignation.setDesignationCode(jsonObject.get("designationCode").toString().toUpperCase());
								masDesignation.setDesignationName(jsonObject.get("designationName").toString().toUpperCase());

								Users user = new Users();
								user.setUserId(new Long(1));
								masDesignation.setUser(user);

								masDesignation.setLastChgDate(lastChgDate);
								session.update(masDesignation);
								transaction.commit();
								result = "200";
							}

						}
					}

				}
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasDesignation> validateMasDesignation(String designationCode, String designationName) {
		List<MasDesignation> masDesignationsList = new ArrayList<MasDesignation>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDesignation.class).add(Restrictions.or(
					Restrictions.eq("designationCode", designationCode), Restrictions.eq("designationName", designationName)));
			masDesignationsList = criteria.list();
		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return masDesignationsList;
	}

	@Override
	public String addDesignation(MasDesignation masDesignation) {
		String result = "";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction transaction = session.beginTransaction();
			Serializable sezObject = session.save(masDesignation);

			if (sezObject != null) {
				result = "200";
			} else {
				result = "300";
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return result;
	}

	@Override
	public Long saveRoleAndDesignation(RoleDesignation roleDesignation) {
		Session session=null;
		Long roleDesignationId=null;
		Transaction tx=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(roleDesignation);
			roleDesignationId=roleDesignation.getRoleDesignationId();
			session.flush();
	        session.clear();
			tx.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			 
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
		return roleDesignationId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleDesignation> getMultipleRoleAndDesignation(JSONObject jsonObject) {
		List<RoleDesignation> listOfRoleDesignation=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			listOfRoleDesignation = session.createCriteria(RoleDesignation.class).list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listOfRoleDesignation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getMasDesignationByDesignationId(Long masDesigtionId) {
		List<MasDesignation> listMasDesignation = null;
		String designationName="";
		String designationId="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			listMasDesignation = session.createCriteria(MasDesignation.class).add(Restrictions.eq("designationId",masDesigtionId)).list();
			if(CollectionUtils.isNotEmpty(listMasDesignation)) {
				int count=0;
				for(MasDesignation masDesignation:listMasDesignation) {
					if(count==0) {
						designationName=masDesignation.getDesignationName();
						designationId=""+masDesignation.getDesignationId();
					}
					else {
						designationName+=","+masDesignation.getDesignationName();
						designationId+=","+masDesignation.getDesignationId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return designationName+"##"+designationId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getMasRoleByRoleId(String masRoles) {
		List<MasRole> listMasRole = null;
		String roleName="";
		String rolessId="";
		try {
			if(StringUtils.isNotEmpty(masRoles)) {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massRolesArray=masRoles.split(",");
			List<Long>listRoles=new ArrayList<>();
			for(String ss:massRolesArray) {
				listRoles.add(Long.parseLong(ss.trim()));
			}
			listMasRole = session.createCriteria(MasRole.class) .add(Restrictions.in("roleId",listRoles)).list();
			if(CollectionUtils.isNotEmpty(listMasRole)) {
				int count=0;
				for(MasRole masRole:listMasRole) {
					if(count==0) {
						roleName=masRole.getRoleName();
						rolessId=""+masRole.getRoleId();
					}
					else {
						roleName+=","+masRole.getRoleName();
						rolessId+=","+masRole.getRoleId();
					}
					count++;
				}
			}else {
				return null;
			}
			}
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return roleName+"##"+rolessId;
	}
	
	 
	
	 
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> getApplicationNameBasesOnRoleNew(Object roleName,String hospitalId) {
		List<MasRole> roleList = new ArrayList<MasRole>();
		List<Long> templateIdList = new ArrayList<Long>();
	
		Map<String,Object>hashMap=new HashMap<>();
		List<TemplateApplication> applicationNameList = new ArrayList<TemplateApplication>();
		List<TemplateApplication> applicationNameList1=new ArrayList<TemplateApplication>();
		
		List<MasApplication>listMasApplication1=new ArrayList<>();
		List<MasApplication>listMasApplication=new ArrayList<>();
		long roleId=0;
		  List<Object> result=null;
		List<Object> result1=null;
		try {
			List<Long>listOfRoles=(List<Long>) roleName;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			if(CollectionUtils.isNotEmpty(listOfRoles)) {
			Criteria cr = session.createCriteria(MasRole.class);
					
			//roleList=cr.add(Restrictions.eq("roleName", roleName))
			roleList=cr.add(Restrictions.in("roleId", listOfRoles))	
			.add(Restrictions.eq("status", "y").ignoreCase())
			.list();
			List<Long>listMasRoleId=new ArrayList<>();
			for(MasRole mm:roleList) {
				listMasRoleId.add(mm.getRoleId());
			}
			
			System.out.println("listMasRoleId="+listMasRoleId);
			if(roleList!=null && roleList.size()>0) {
				roleId = roleList.get(0).getRoleId();
				
				templateIdList=session.createCriteria(RoleTemplate.class)
				.add(Restrictions.in("msRole.roleId", listMasRoleId))
				.add(Restrictions.eq("status", "y").ignoreCase())
							/*
							 * .add(Restrictions.eq("masHospital.hospitalId", Long.parseLong(hospitalId)))
							 */
				.setProjection(Projections.property("msTemplate.templateId"))
				.list();
				
				System.out.println("templateIdList="+templateIdList.size());
				
				//Check For All Unit Admin Template
				/*String roleIdss=UtilityServices.getValueOfPropByKey("roleIdUnitAdmin");
				MasRole masRole=new MasRole();
				masRole.setRoleId(Long.parseLong(roleIdss));
				MasHospital masHospital=new MasHospital();
				masHospital.setHospitalId(Long.parseLong(hospitalId));
				Criterion cr11=Restrictions.eq("masHospital", masHospital);
				Criterion cr12=Restrictions.eq("msRole", masRole);
				List<RoleTemplate>lisRoleTemplate=roleTemplateDao.findByCriteria(cr11,cr12);
				 if(CollectionUtils.isEmpty(lisRoleTemplate)) {
					List<Long> templateIdList1=session.createCriteria(RoleTemplate.class)
							.add(Restrictions.in("msRole.roleId", listMasRoleId))
							.add(Restrictions.eq("status", "y").ignoreCase())
							//.add(Restrictions.eq("masHospital.hospitalId", Long.parseLong(hospitalId)))
							.setProjection(Projections.property("msTemplate.templateId"))
							.list();
					if(CollectionUtils.isNotEmpty(templateIdList1)) {
						templateIdList.addAll(templateIdList1);
					}
				}*/
				//End of Checking
					/*
					 * if(CollectionUtils.isEmpty(templateIdList)) {
					 * templateIdList=session.createCriteria(RoleTemplate.class)
					 * .add(Restrictions.in("msRole.roleId", listMasRoleId))
					 * .add(Restrictions.eq("status", "y").ignoreCase())
					 * //.add(Restrictions.eq("masHospital.hospitalId", Long.parseLong(hospitalId)))
					 * .setProjection(Projections.property("msTemplate.templateId")) .list(); }
					 */
				
				
				if(templateIdList!=null && templateIdList.size()>0) {
					Criteria cr1=session.createCriteria(TemplateApplication.class) ;
					cr1.createAlias("masApplication", "application")
					.add(Restrictions.in("masTemplate.templateId", templateIdList))
					.add(Restrictions.eq("status", "y").ignoreCase())
					.add(Restrictions.ne("application.parentId", "0").ignoreCase());
					cr1.setProjection(
						        Projections.projectionList()
						        .add(Projections.distinct(Projections.property("application.applicationId")))
						        .add(Projections.property("application.url")) 
						          .add(Projections.property("application.applicationName"))
						          .add(Projections.property("application.parentId"))
						          .add(Projections.property("application.orderNo"))
						          .add(Projections.property("application.appSequenceNo"))
						        );
					 
					result=cr1.list();
					if(CollectionUtils.isNotEmpty(result))
					for(Object aa:result) {
							Object[]rows=(Object[]) aa;
							MasApplication masApplication=new MasApplication();
							if(rows[0]!=null)
								masApplication.setApplicationId(rows[0].toString());
								if(rows[1]!=null)
								masApplication.setUrl(rows[1].toString());
								if(rows[2]!=null)
								masApplication.setApplicationName( rows[2].toString());
								if(rows[3]!=null)
								masApplication.setParentId(rows[3].toString());
								if(rows[4]!=null)
								masApplication.setOrderNo(Long.parseLong(rows[4].toString()));
								if(rows[5]!=null)
									masApplication.setAppSequenceNo(Long.parseLong(rows[5].toString()));
							listMasApplication.add(masApplication);
						}
					Collections.sort(listMasApplication, MasApplication.orderNoComparator);
				}
				if(templateIdList!=null && templateIdList.size()>0) {
					
					Criteria cr2= session.createCriteria(TemplateApplication.class) ;
						cr2	.createAlias("masApplication", "application")
					.add(Restrictions.in("masTemplate.templateId", templateIdList))
					.add(Restrictions.eq("status", "y").ignoreCase())
					.add(Restrictions.eq("application.parentId", "0").ignoreCase());
						
					cr2.setProjection(
					        Projections.projectionList()
					        .add(Projections.distinct(Projections.property("application.applicationId")))
					        .add(Projections.property("application.url"))
					          .add(Projections.property("application.applicationName"))
					          .add(Projections.property("application.parentId"))
					          .add(Projections.property("application.orderNo"))
					          .add(Projections.property("application.appSequenceNo"))
							);
					result1=cr2.list();
					if(CollectionUtils.isNotEmpty(result1))
					for(Object aa:result1) {
						Object[]rows=(Object[]) aa;
						MasApplication masApplication=new MasApplication();
						if(rows[0]!=null)
						masApplication.setApplicationId(rows[0].toString());
						if(rows[1]!=null)
						masApplication.setUrl(rows[1].toString());
						if(rows[2]!=null)
						masApplication.setApplicationName( rows[2].toString());
						if(rows[3]!=null)
						masApplication.setParentId(rows[3].toString());
						if(rows[4]!=null)
						masApplication.setOrderNo(Long.parseLong(rows[4].toString()));
						if(rows[5]!=null)
							masApplication.setAppSequenceNo(Long.parseLong(rows[5].toString()));
						listMasApplication1.add(masApplication);
					}
					Collections.sort(listMasApplication1, MasApplication.orderNoComparator);
					
				}
			}
			String stempId="";
			 if(CollectionUtils.isNotEmpty(templateIdList)) {
			 for(Long l:templateIdList) {
				 if(StringUtils.isEmpty(stempId)) {
					 stempId=""+l;
				 }
				 else {
					 stempId+=","+l;
				 }
			 }
			 }
			 List<Object[]> listObject=null;
			StringBuffer sQuery=new StringBuffer();
			//sQuery.append(" SELECT ma.APP_ID,ma.url,ma.NAME,ma.PARENT_ID ");
				//	sQuery.append(" FROM mas_application ma,(select app_id,NAME,PARENT_ID,url from mas_application  where PARENT_ID<>'0' and url='#') aks where ma.PARENT_ID=AKS.APP_ID");
					sQuery.append(" select ma.APP_ID,ma.url,ma.NAME,ma.PARENT_ID,ma.ORDER_NO,ma.app_sequence_no from "+mas_application+" ma where PARENT_ID<>'0' and url='#'");
					sQuery.append(" union ");
					sQuery.append("	select  ma.APP_ID,ma.url,ma.NAME,ma.PARENT_ID,ma.ORDER_NO,ma.app_sequence_no  ");
					sQuery.append("	from "+mas_application+" ma,template_application ta ");
					sQuery.append("	where  ma.PARENT_ID in (select  app_id from mas_application  where PARENT_ID<>'0' and url='#')  and ");
					sQuery.append("	 ma.app_id=ta.app_id and ta.status='y' and template_id in("+stempId+")"  );
					
					if(StringUtils.isNotEmpty(stempId)) {
						Query query = session.createSQLQuery(sQuery.toString());
				     listObject = query.list();
				     
				     
					}
		 
			hashMap.put("applicationNameList", result);
			hashMap.put("applicationNameList1", result1);
			hashMap.put("applicationListSubchild", listObject);
			hashMap.put("listMasApplication", listMasApplication);
			hashMap.put("listMasApplication1", listMasApplication1);
			}
			else {
				hashMap.put("applicationNameList", result);
				hashMap.put("applicationNameList1", result1);
				hashMap.put("applicationListSubchild", null);
				hashMap.put("listMasApplication", listMasApplication);
				hashMap.put("listMasApplication1", listMasApplication1);
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			hashMap.put("applicationNameList", result);
			hashMap.put("applicationNameList1", result1);
			hashMap.put("applicationListSubchild", null);
			hashMap.put("listMasApplication", listMasApplication);
			hashMap.put("listMasApplication1", listMasApplication1);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return hashMap;
	}
	
	@Override
	public List<Long> getRolesList(Users users,String hospitalIdForMas){
		
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<Long>listRoles=new ArrayList<>();
		String [] massRolesArray=null;
		String masRoles=users.getRoleId();
		if(StringUtils.isNotEmpty(masRoles))
		 massRolesArray=masRoles.split(",");
		if(massRolesArray!=null)
		for(String ss:massRolesArray) {
			listRoles.add(Long.parseLong(ss.trim()));
		}
		//String [] massDesignationArray=null;
		//String masDesigtionId=users.getDesignationId();
		//if(StringUtils.isNotEmpty(masDesigtionId))
		// massDesignationArray=masDesigtionId.split(",");
		
		
		/*
		 * List<Long>listMasssDesi=new ArrayList<>();
		 * List<MasDesignation>listMasDesignation =new ArrayList<>();
		 * if(massDesignationArray!=null) for(String ss:massDesignationArray) {
		 * listMasssDesi.add(Long.parseLong(ss.trim())); MasDesignation
		 * masDesignation=new MasDesignation();
		 * masDesignation.setDesignationId(Long.parseLong(ss.trim()));
		 * listMasDesignation.add(masDesignation); }
		 */
		/*
		 * List<RoleDesignation>listOfRoleDesignation=null;
		 * if(CollectionUtils.isNotEmpty(listMasDesignation)) listOfRoleDesignation=
		 * (List<RoleDesignation>) session.createCriteria(RoleDesignation.class)
		 * .add(Restrictions.in("masDesignation", listMasDesignation))
		 * .add(Restrictions.eq("locationId", Long.parseLong(hospitalIdForMas)))
		 * .list();
		 */
		
		
		/*
		 * for(RoleDesignation roleDesignation:listOfRoleDesignation) { String []
		 * massRolesArrayRole=null; String masRolesMas=roleDesignation.getRoleId();
		 * if(StringUtils.isNotEmpty(masRolesMas))
		 * massRolesArrayRole=masRolesMas.split(","); if(massRolesArrayRole!=null)
		 * for(String ss:massRolesArrayRole) { listRoles.add(Long.parseLong(ss.trim()));
		 * }
		 */
			
			  //listRoles.add(roleDesignation.getMasRole().getRoleId());
	
				
			return listRoles;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getDesignationNameFromUser(String designationId) {
		List<MasDesignation> list = new ArrayList<MasDesignation>();
		String designationName="";
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasDesignation.class).add(Restrictions.eq("designationId", Long.parseLong(designationId)));
			list = criteria.list();
			
			if(list!=null && list.size()>0) {
				MasDesignation designation = list.get(0);
				designationName = designation.getDesignationName();
			}else {
				designationName="";
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return designationName;
	}

	@Override
	public String existDesignationInMasRoleDesignationMapping(long designationId, Long locationId) {
		String flag="";
		List<RoleDesignation> listRoleDesignation=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(RoleDesignation.class, "roledesignation")
			.createAlias("masDesignation", "masDesignation")
			.add(Restrictions.and(Restrictions.eq("masDesignation.designationId", designationId), Restrictions.eq("locationId", locationId)));
			listRoleDesignation = criteria.list();
			
			if(CollectionUtils.isNotEmpty(listRoleDesignation)) {
				listRoleDesignation.get(0);
				flag = "success";
				return flag;
			}else {
				flag = "fail";
				return flag;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasApplication> getAllApp_Id() {
		List<MasApplication> list_app_id=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			/*Criteria criteria = session.createCriteria(MasApplication.class)
					.add(Restrictions.eq("parentId", "0"));*/
			Criteria criteria = session.createCriteria(MasApplication.class).add(Restrictions.or(
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.eq("parentId", "0")), 
					Restrictions.and(Restrictions.eq("url", "#"), Restrictions.ne("parentId", "0"))));
					
					
			
			/*ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("applicationId").as("applicationId"));
			
			list_app_id = criteria.setProjection(projectionList).list();*/
			list_app_id = criteria.list();
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list_app_id;
	}
	
	@Override
	public Map<String, Object> getLoginDetails(HashMap<String, Object> jsondataLdap) {
		//List<MasEmployee> list =null;
		List<Users> listUser =null;
		//long visitId = Long.parseLong((String) jsonData.get("visitId"));
		String userName =(String) jsondataLdap.get("userName");
		String password =(String) jsondataLdap.get("password");
		Map<String,Object> map = new HashMap<String, Object>();
		try{
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		 Criteria cr = session.createCriteria(Users.class)
				      .add(Restrictions.eq("loginName",userName))
				      .add(Restrictions.eq("password",password));
		// Criterion c1 = null;
		// c1= Restrictions.add(Restrictions.eq("loginName", userName).ignoreCase(), Restrictions.eq("password",password).ignoreCase());
		 //cr.add(c2);
		 listUser= cr.list();
	
	     map.put("listUser", listUser);
		 getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MasApplication> getParentApplicationName(String ParentId) {
		List<MasApplication> parentApplicationList=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			parentApplicationList = session.createCriteria(MasApplication.class).add(Restrictions.eq("applicationId", ParentId)).list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			 getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return parentApplicationList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllApplicationOfSelectedParent(JSONObject jsonObject, HttpServletRequest request,
			HttpServletResponse response) {
		String parentId="";
		Map<String, Object> mapObject = new HashMap<String, Object>();
		List<String[]> listApp = new ArrayList<String[]>();
		List<MasApplication> masApp = new ArrayList<MasApplication>();
		
		if(jsonObject!=null) {			
			parentId = jsonObject.get("parentId").toString();
		}
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			String entityQuery = "select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa, " + 
					        " ma1.app_sequence_no as sequenceNo " + 
							" from mas_application ma1 left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" where ma1.parent_id='"+parentId+"' " + 
							" union all  " + 
							" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa, " + 
							" ma1.app_sequence_no as sequenceNo " + 
							" from mas_application ma1 left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" where ma1.parent_id in ( select app_id from mas_application where parent_id='"+parentId+"') " + 
							" union all  " + 
							" select ma1.name,ma1.parent_id,ma1.app_id,ma1.url,ma2.name as aaaa, " + 
							" ma1.app_sequence_no as sequenceNo " + 
							" from mas_application ma1 " + 
							" left join mas_application ma2 on ma1.parent_id=ma2.app_id " + 
							" where ma1.status='y' and ma1.app_id ='"+parentId+"'" ;
			
			
			
			SQLQuery sqlQuery = session.createSQLQuery(entityQuery);
			listApp = sqlQuery.list();
			
			masApp  = session.createCriteria(MasApplication.class)
                    .add(Restrictions.eq("parentId", parentId))
                    .add(Restrictions.eq("url", "#")).list();
			
			String parentid = "";
			
			if(parentId != null && !parentId.isEmpty())
				parentid = parentid +"'"+ parentId+"'";
			for(MasApplication masAp : masApp){
				if(parentid.length() == 0)
					parentid = parentid + "'"+masAp.getApplicationId()+"'";
				else
					parentid = parentid + ",'" + masAp.getApplicationId()+"'";				
			}
	
			mapObject.put("listApp", listApp);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObject;
	}

	@Override
	public boolean setSequenceToApplication(JSONObject jsonObject) {
		boolean flag=false;
		Transaction tx =null;
		if(jsonObject!=null) {
			try {
				String applicationId = jsonObject.get("applicationIdAarray").toString();
				applicationId= JavaUtils.getReplaceString1(applicationId);
				applicationId=applicationId.replaceAll("\"", "");
				String[] applicationValueArray = applicationId.split(",");
				
				String sequenceNo = jsonObject.get("applicationSequenceAarray").toString();
				sequenceNo= JavaUtils.getReplaceString1(sequenceNo);
				sequenceNo=sequenceNo.replaceAll("\"", "");
				String[] sequenceIdArray = sequenceNo.split(","); 
				
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				tx=session.beginTransaction();
				
				for (int i = 0; i < applicationValueArray.length; i++) {
						MasApplication masApplication = new MasApplication();
						masApplication = (MasApplication) session.get(MasApplication.class,
								applicationValueArray[i].toString());
						if(masApplication!=null) {
							masApplication.setAppSequenceNo(Long.parseLong(sequenceIdArray[i].toString()));
							session.update(masApplication);
						}
				}
				tx.commit();
				flag=true;
				
			}catch(Exception e) {
				flag=false;
				e.printStackTrace();
				tx.rollback();
			}
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMaxCountOfChild(String parentIdd) {
		Criteria criteria = null;
		Long rowCount=null;
		String applicationIddd="";
		List<MasApplication> appList=new ArrayList<MasApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			if(!parentIdd.equalsIgnoreCase("0")) {
			int pidIndex1 = parentIdd.lastIndexOf("[");
			int pidIndex2 = parentIdd.lastIndexOf("]");
			pidIndex1++;
			
			String parId = JavaUtils.getReplaceString(parentIdd);
		    String [] pid = parId.split(",");
		    if(!pid[3].equalsIgnoreCase("0")) {
				Criteria cri = session.createCriteria(MasApplication.class)
								.add(Restrictions.ilike("applicationName", pid[3]))
								.add(Restrictions.eq("parentId", "0"));
								appList = cri.list();
								applicationIddd = appList.get(0).getApplicationId();
				
			}
		    
		    if(!applicationIddd.equalsIgnoreCase("") && StringUtils.isNotEmpty(applicationIddd)) {
				criteria = session.createCriteria(MasApplication.class)
						.add(Restrictions.eq("parentId", applicationIddd));
			}else {
		    
			    if(pid[1]!="" && StringUtils.isNotBlank(pid[1])) {
			    	criteria= session.createCriteria(MasApplication.class);
					criteria.add(Restrictions.eq("parentId", pid[3]));
			    }	
			  }
		    criteria.setProjection(Projections.max("appSequenceNo"));
			 List list = criteria.list();
			 if(list!=null && list.size()>0) {
				 rowCount = (Long) list.get(0);
	              
			 }
		    
			} 
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			 getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getApplicationIdParent(String parentName) {
		String parentAppId="";
		List<MasApplication> appList=new ArrayList<MasApplication>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cri = session.createCriteria(MasApplication.class)
				.add(Restrictions.ilike("applicationName", parentName))
				.add(Restrictions.eq("parentId", "0"));
				appList = cri.list();
				parentAppId = appList.get(0).getApplicationId();
				System.out.println("parentAppId :: "+parentAppId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return parentAppId;
	}

	@Override
	public List<MasDistrict> getDistrictList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasDistrict> list = new ArrayList<MasDistrict>();
		try {
			//Long stateId = Long.parseLong(String.valueOf(requestData.get("stateId")));
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr  = session.createCriteria(MasDistrict.class).add(Restrictions.eq("status", "Y").ignoreCase())
					.addOrder(Order.asc("districtName")); 
			list=cr.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@Override
	public List<MasCity> getCityList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasCity> list = new ArrayList<>();
		try {
			Long districtId = null;
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasCity.class).add(Restrictions.eq("status", "Y").ignoreCase())
								.addOrder(Order.asc("cityName")); 
			
			if(requestData.get("districtId") != null) {
				districtId = Long.parseLong(String.valueOf(requestData.get("districtId")));
				//criteria.createAlias("masDistrict", "district").add(Restrictions.eq("district.districtId", districtId));
			}
			list = criteria.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@Override
	public List<MasMMU> getMMUList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasMMU> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasMMU.class).add(Restrictions.eq("status", "Y").ignoreCase())
					.addOrder(Order.asc("mmuName"));
			list=cr.list();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}

	@Override
	public Long saveRoleAndTypeOfUsers(Users users) {
		Session session=null;
		Long roleDesignationId=null;
		Transaction tx=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(users);
			roleDesignationId=users.getUserId();
			session.flush();
	        session.clear();
			tx.commit();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			 
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			
			}
		return roleDesignationId;
	}

	@Override
	public Map<String,Object> getUserDetailsList(HashMap<String,Object> jsonData) {
		
		List<Visit> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			
			
			Criteria cr = null;
			
			int pageNo = Integer.parseInt(jsonData.get("pageNo") + "");
		
			String userName = (String) jsonData.get("userName");
			Long userFlag = Long.parseLong((String) jsonData.get("statusVal"));
			String apmUsers=(String) jsonData.get("userTypeName");
			
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count = 0;
						
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			cr = session.createCriteria(Users.class,"users")
					     .addOrder(Order.asc("userId"));
				
		   if(apmUsers.equals("APM"))
		   {
			   ArrayList<String> listOfMmu = new ArrayList<String>();
			   String inputString = (String) jsonData.get("mmuId");
			   String[] convertedMMUArray = inputString.split(",");
			   List<Integer> convertedMMUList = new ArrayList<Integer>();
			   for (String number : convertedMMUArray) {
				   convertedMMUList.add(Integer.parseInt(number.trim()));
				   listOfMmu.add(number+",");
				   
			   }
			   
			   Criterion c1 = Restrictions.in("mmuId", listOfMmu);
			   cr.add(c1);
		   }

			if (userName != null && !userName.equals("") && !userName.equals("null")) {
				String pName = "%" + userName + "%";
				  cr.add(Restrictions.like("users.userName", pName).ignoreCase());
				  //cr.add(Restrictions.or(Restrictions.like("patient.patientName", pName,MatchMode.ANYWHERE),Restrictions.eq("patient.patientName", patientName).ignoreCase() ));
			}
			if (userFlag != null && !userFlag.equals("") && !userFlag.equals("null")) {
				if(userFlag!=3l)
				cr.add(Restrictions.eq("userFlag", userFlag));
			}
			
			list = cr.list();
			count = list.size();
			map.put("count", count);
			cr = cr.setFirstResult(pagingSize * (pageNo - 1));
	    	cr = cr.setMaxResults(pagingSize);
			list = cr.list();

			//System.out.println("");
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
	
	
	@Override
	public List<Users> getUserDetails() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Users.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("mobileNo").as("mobileNo"));
		projectionList.add(Projections.property("userName").as("userName"));
		projectionList.add(Projections.property("userTypeId").as("userTypeId"));
		projectionList.add(Projections.property("userFlag").as("userFlag"));
		projectionList.add(Projections.property("levelOfUser").as("levelOfUser"));

		cr.setProjection(projectionList);
		
		List<Users> list = cr.setResultTransformer(new AliasToBeanResultTransformer(Users.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List getCampDetais(String mmuid) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<MasCamp> list = null;
		List<Object[]> searchList = null;
		try {	
			//String Query ="SELECT VT.VISIT_DATE,ICD_DIAGNOSIS,WORKING_DIAGNOSIS,CHIEF_COMPLAIN,FAMILY_HISTORY,PAST_MEDICAL_HISTORY,PRESENT_ILLNESS_HISTORY,VT.VISIT_ID FROM  VISIT VT LEFT OUTER JOIN  OPD_PATIENT_DETAILS OPD ON OPD.VISIT_ID=VT.VISIT_ID LEFT OUTER JOIN  OPD_PATIENT_HISTORY OPH ON OPH.VISIT_ID=VT.VISIT_ID  WHERE   VT.PATIENT_ID='"+patientId+"' AND Upper(VT.VISIT_STATUS)='C' ORDER BY VISIT_DATE DESC";
			//String Query ="select camp_id,location,city_id,department_id from mas_camp where mmu_id='"+mmuid+"' and camp_date=CURRENT_DATE" ;
			String Query="select mc.camp_id,mc.location,mc.city_id,mc.department_id,mm.MMU_NAME,de.department_name,ce.city_name, mc.weekly_off from mas_camp mc LEFT OUTER JOIN  MAS_MMU mm ON mc.mmu_id=mm.MMU_ID LEFT OUTER JOIN  MAS_DEPARTMENT de ON mc.department_id=de.department_id  LEFT OUTER JOIN  MAS_CITY ce ON mc.city_id=ce.city_id  where mc.mmu_id='"+mmuid+"' and mc.camp_date=CURRENT_DATE";
			/*String Query = " select mc.camp_id,mc.location,mc.city_id,mc.department_id,mm.MMU_NAME,de.department_name,ce.city_name " 
							+" from mas_camp mc " 
							+" LEFT OUTER JOIN  MAS_MMU mm ON mc.mmu_id=mm.MMU_ID "
							+" LEFT OUTER JOIN  MAS_DEPARTMENT de ON mc.department_id=de.department_id "
							+" LEFT OUTER JOIN  MAS_CITY ce ON mc.city_id=ce.city_id "
							+" where mc.mmu_id='"+mmuid+"' and mc.camp_date=CURRENT_DATE "
							+" and to_char(current_timestamp, 'HH24:MI:SS') >= to_char(start_time, 'HH24:MI:SS') "
							+" and to_char(current_timestamp, 'HH24:MI:SS')<=to_char(end_time, 'HH24:MI:SS') ";*/
			System.out.println(Query);
		if (Query != null) 
		{
			searchList = session.createSQLQuery(Query).list();
		} 
		else
		{
			System.out.println("No Record Found");
		}
		return searchList;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return searchList;
	}
	
	@Override
	public Map<String, List<MasUserType>> getAllUserType(JSONObject jsondata) {
		Map<String, List<MasUserType>> map = new HashMap<String, List<MasUserType>>();
		List<MasUserType> userTypeList = new ArrayList<MasUserType>();
		List totalMatches  =new ArrayList<>();
		//String mmuStaff = jsondata.getString("mmuStaff");
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasUserType.class);	
			/*if(!mmuStaff.equals("") && mmuStaff.equalsIgnoreCase("y")) {
				criteria.add(Restrictions.eq("mmuStaff", mmuStaff));
			}*/
						
				 criteria.addOrder(Order.asc("userTypeName"));
				 totalMatches = criteria.list();				 
				 userTypeList = criteria.list();
			
		map.put("userTypeList", userTypeList);
		map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@Override
	public boolean isActiveInactiveUsers(String userId, String usersStatus) {
		boolean flag = false;
		Long updatedResult = null;
		Transaction tx = null;
		String qryString = null;
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		tx = session.beginTransaction();
		try {
			if(usersStatus.equals("inactive"))
			{	
			  qryString  ="update Users l  set l.userFlag='2' where l.userId='"+userId+"' " ;
			}
			else
			{
			   qryString  ="update Users l  set l.userFlag='1' where l.userId='"+userId+"' " ;
			}
			Query query = session.createQuery(qryString);
	        int count = query.executeUpdate();
			//session.update(le);
			System.out.println("query ::"+qryString );
			System.out.println(count + " Record(s) Updated.");
		    System.out.println("Updating with Query Parameters ");
			tx.commit();
			flag = true;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		return flag;
	}
	
	@Override
	public Users checkLoginCredential(String username, boolean status) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long userFlag=2l;
			Criteria cr = session.createCriteria(Users.class);
			cr.add(Restrictions.eq("loginName", username));
			cr.add(Restrictions.ne("userFlag", userFlag));
			Users entity = (Users) cr.uniqueResult();
			session.close();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			session.close();
			return null;
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

	}

	@Override
	public String getMasDistrictId(String districtId) {
		List<MasDistrict> listMasDistrict = null;
		String districtName="";
		String districtIdVal="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massDistArray=districtId.split(",");
			List<Long>listMasssDesi=new ArrayList<>();
			for(String ss:massDistArray) {
				listMasssDesi.add(Long.parseLong(ss.trim()));
			}
			listMasDistrict = session.createCriteria(MasDistrict.class) .add(Restrictions.in("districtId",listMasssDesi)).
					 list();
			if(CollectionUtils.isNotEmpty(listMasDistrict)) {
				int count=0;
				for(MasDistrict masDist:listMasDistrict) {
					if(count==0) {
						districtName=masDist.getDistrictName();
						districtIdVal=""+masDist.getDistrictId();
					}
					else {
						districtName+=","+masDist.getDistrictName();
						districtIdVal+=","+masDist.getDistrictId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return districtName+"##"+districtIdVal;
}
	
	@Override
	public Users getUsersByUserId(Long userIdVal) {
		 Users user = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			user = (Users) session.createCriteria(Users.class)
					 .add(Restrictions.eq("userId",userIdVal)).uniqueResult();
					// list();
			
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return user;
	}

	@Override
	public String getMasMmu(String mmuId) {
		List<MasMMU> listMasMmu = null;
		String mmuName="";
		String mmuIdVal="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massMmuArray=mmuId.split(",");
			List<Long>listMasssMmu=new ArrayList<>();
			for(String ss:massMmuArray) {
				listMasssMmu.add(Long.parseLong(ss.trim()));
			}
			listMasMmu = session.createCriteria(MasMMU.class) .add(Restrictions.in("mmuId",listMasssMmu)).
					 list();
			if(CollectionUtils.isNotEmpty(listMasMmu)) {
				int count=0;
				for(MasMMU masMmu:listMasMmu) {
					if(count==0) {
						mmuName=masMmu.getMmuName();
						mmuIdVal=""+masMmu.getMmuId();
					}
					else {
						mmuName+=","+masMmu.getMmuName();
						mmuIdVal+=","+masMmu.getMmuId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mmuName+"##"+mmuIdVal;
}
	
	@Override
	public MasUserType getMasTypeOfUsers(Long typeOfUserId) {
		MasUserType usertype = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			usertype = (MasUserType) session.createCriteria(MasUserType.class)
					 .add(Restrictions.eq("userTypeId",typeOfUserId)).uniqueResult();
					// list();
			
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return usertype;
	}

	@Override
	public String getMasCity(String cityId) {
		List<MasCity> listMasCity = null;
		String cityName="";
		String cityIdVal="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massCityArray=cityId.split(",");
			List<Long>listMasssCity=new ArrayList<>();
			for(String ss:massCityArray) {
				listMasssCity.add(Long.parseLong(ss.trim()));
			}
			listMasCity = session.createCriteria(MasCity.class) .add(Restrictions.in("cityId",listMasssCity)).
					 list();
			if(CollectionUtils.isNotEmpty(listMasCity)) {
				int count=0;
				for(MasCity MasCity:listMasCity) {
					if(count==0) {
						cityName=MasCity.getCityName();
						cityIdVal=""+MasCity.getCityId();
					}
					else {
						cityName+=","+MasCity.getCityName();
						cityIdVal+=","+MasCity.getCityId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return cityName+"##"+cityIdVal;
}

	@Override
	public String updateUsersDetails(HashMap<String, Object> jsonObject) {
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		String userId=jsonObject.get("userId").toString();
		String levelUsers=jsonObject.get("levelUsers").toString();
	    String nameOfUser=jsonObject.get("nameOfUser").toString();
	    String emailId=jsonObject.get("emailId").toString();
		String masRoleIdValues=jsonObject.get("masRoleIdValues").toString();
		String userTypeValues=jsonObject.get("userTypeValues").toString();
		String userTypeVal=jsonObject.get("userTypeVal").toString();
		String signatureFileName = "";
		if(jsonObject.containsKey("signatureFileName")){
			signatureFileName = jsonObject.get("signatureFileName").toString();
		}
		String empIdNumber=null;
		String employeeId=null;
		if(jsonObject.get("empIdNumber")!=null && jsonObject.get("empIdNumber")!="")
		{	
		 empIdNumber=jsonObject.get("empIdNumber").toString();
		}
		if(jsonObject.get("employeeId")!=null && jsonObject.get("employeeId")!="")
		{	
			employeeId=jsonObject.get("employeeId").toString();
		}
		
		if(userId!=null && levelUsers.equals("M"))
		{	
		String qryStringHd  ="update Users u set u.levelOfUser='M',u.mmuId='"+userTypeValues+"', u.districtId='"+""+"',u.cityId='"+""+"',u.stateId='"+""+"',u.userName='"+nameOfUser+"',u.emailAddress='"+emailId+"',u.userTypeId='"+userTypeVal+"',u.roleId='"+masRoleIdValues+"',u.signatureFileName='"+signatureFileName+"' where u.userId='"+userId+"' " ;
		Query queryHd = session.createQuery(qryStringHd);
        int countHd = queryHd.executeUpdate();
		System.out.println(countHd + "Users Records MMU Updated.");
		t.commit();
		
		if(employeeId!=null && employeeId!="")
		{
			String str = userTypeValues;
			str = str.substring(0, str.lastIndexOf(","));
			
			Transaction t1 = session.beginTransaction();
			String qryStringHdEmp  ="update EmployeeRegistration u set u.mmuId='"+str+"' where u.employeeId='"+employeeId+"' " ;
			Query queryHdEmp = session.createQuery(qryStringHdEmp);
	        int countHdEmp = queryHdEmp.executeUpdate();
			System.out.println(countHdEmp + "Mas employee mas_employee MMUId Updated.");
			t1.commit();
		}
		
		}
		else if(userId!=null && levelUsers.equals("D"))
		{
			
			String qryStringHd  ="update Users u set u.levelOfUser='D',u.districtId='"+userTypeValues+"', u.mmuId='"+""+"',u.cityId='"+""+"',u.stateId='"+""+"',u.userName='"+nameOfUser+"',u.emailAddress='"+emailId+"',u.userTypeId='"+userTypeVal+"',u.roleId='"+masRoleIdValues+"',u.signatureFileName='"+signatureFileName+"' where u.userId='"+userId+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "Users Records Dist Updated.");
			t.commit();
		}
		else if(userId!=null && levelUsers.equals("C"))
		{
			String qryStringHd  ="update Users u set u.levelOfUser='C',u.cityId='"+userTypeValues+"', u.mmuId='"+""+"',u.districtId='"+""+"',u.stateId='"+""+"',u.userName='"+nameOfUser+"',u.emailAddress='"+emailId+"',u.userTypeId='"+userTypeVal+"',u.roleId='"+masRoleIdValues+"',u.signatureFileName='"+signatureFileName+"' where u.userId='"+userId+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "Users Records City Updated.");
			t.commit();
		}
		else if(userId!=null && levelUsers.equals("S"))
		{
			String qryStringHd  ="update Users u set u.levelOfUser='S',u.stateId='"+userTypeValues+"',u.mmuId='"+""+"',u.districtId='"+""+"',u.cityId='"+""+"',u.userName='"+nameOfUser+"',u.emailAddress='"+emailId+"',u.userTypeId='"+userTypeVal+"',u.roleId='"+masRoleIdValues+"',u.signatureFileName='"+signatureFileName+"' where u.userId='"+userId+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "Users Records City Updated.");
			t.commit();
		}
		else if(userId!=null && levelUsers.equals("V"))
		{
			String qryStringHd  ="update Users u set u.levelOfUser='V',u.vendorId='"+userTypeValues+"',u.stateId='"+""+"',u.mmuId='"+""+"',u.districtId='"+""+"',u.cityId='"+""+"',u.userName='"+nameOfUser+"',u.emailAddress='"+emailId+"',u.userTypeId='"+userTypeVal+"',u.roleId='"+masRoleIdValues+"',u.signatureFileName='"+signatureFileName+"' where u.userId='"+userId+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "Users Records vendor Updated.");
			t.commit();
		}
		else if(userTypeVal.equals("changePassword"))
		{
			String qryStringHd  ="update Users u set u.password='"+emailId+"',u.userFlag=1 where u.loginName='"+nameOfUser+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "password updated successfully");
			t.commit();
		}
		if(empIdNumber!=null && empIdNumber!="" )
		{
			Transaction t1 = session.beginTransaction();
			String mimeTypeId = "";
			byte[] decodedProfileImage= null;
			byte[] decodedIdImage = null;
			String empIdType=jsonObject.get("empIdType").toString();
			String iDNumber=jsonObject.get("empIdNumber").toString();
			if(jsonObject.get("idMimeType") !=null) {
				mimeTypeId = String.valueOf(jsonObject.get("idMimeType"));
			}
			if(jsonObject.get("base64ForProfileStr") != null && !jsonObject.get("base64ForProfileStr").equals("")) {
				String base64ForProfileTmp= String.valueOf(jsonObject.get("base64ForProfileStr"));			
				String base64ForProfileStr = base64ForProfileTmp.substring(base64ForProfileTmp.indexOf(",")+1, base64ForProfileTmp.length());
				decodedProfileImage = Base64.decodeBase64(base64ForProfileStr);
				//employee.setProfileImage(decodedProfileImage);
			}
			if(jsonObject.get("base64ForIdStr") != null && !jsonObject.get("base64ForIdStr").equals("")) {
				String base64ForIdStr = String.valueOf(jsonObject.get("base64ForIdStr"));
				decodedIdImage = Base64.decodeBase64(base64ForIdStr);
				//employee.setIdentificationTypeImage(decodedIdImage);
			}
			String qryStringHd  ="update EmployeeRegistration u set u.profileImage='"+decodedProfileImage+"',u.identificationTypeId='"+empIdType+"',u.identificationTypeNo='"+iDNumber+"',u.identificationTypeImage='"+decodedIdImage+""+"',u.idMimeType='"+mimeTypeId+"' where u.mobileNo='"+nameOfUser+"' " ;
			Query queryHd = session.createQuery(qryStringHd);
	        int countHd = queryHd.executeUpdate();
			System.out.println(countHd + "Mas employee mas_employee Updated.");
			t1.commit();
			
		}
		}catch (Exception e) {
			e.printStackTrace();
			return "500";
			// TODO: handle exception
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "statusUpdated";
	}
	
	@Override
	public MasMMU getCityIdAndName(String mmuId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			long mmuIdd = Long.parseLong(String.valueOf(mmuId));
			MasMMU masMMU = (MasMMU) session.get(MasMMU.class, mmuIdd);
					
			return masMMU;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return null;
	}

	@Override
	public EmployeeRegistration checkUserNameEmployee(String username, boolean b) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria cr = session.createCriteria(EmployeeRegistration.class);
			cr.add(Restrictions.eq("mobileNo", username));
			EmployeeRegistration entity = (EmployeeRegistration) cr.uniqueResult();
			session.close();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			session.close();
			return null;
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

	}
	
	@Override
	public String updateEmpDetails(JSONObject jsonObject) {
		String mmuId=jsonObject.get("userTypeValues").toString();
		String employeeId=jsonObject.get("employeeId").toString();
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		try {
			String str = mmuId;
			str = str.substring(0, str.lastIndexOf(","));
			String qryStringHdEmp  ="update EmployeeRegistration u set u.mmuId='"+str+"' where u.employeeId='"+employeeId+"' " ;
			Query queryHdEmp = session.createQuery(qryStringHdEmp);
	        int countHdEmp = queryHdEmp.executeUpdate();
			System.out.println(countHdEmp + "Mas employee mas_employee MMUId Updated.");
			t.commit();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "statusUpdated";
	}
	@Override
	public Users checkActiveUsers(String username, boolean status) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Long userFlagActive=1l;
			Long userNewFlag=0l;
			Criteria cr = session.createCriteria(Users.class);
			cr.add(Restrictions.eq("loginName", username));
			cr.add(Restrictions.or(Restrictions.eq("userFlag", userFlagActive), Restrictions.eq("userFlag", userNewFlag)));
			Users entity = (Users) cr.uniqueResult();
			session.close();
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			session.close();
			return null;
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

	}

	@Override
	public String closeIdleTrans() {
		// TODO Auto-generated method stub
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Transaction t = session.beginTransaction();
		String qryStringHdEmp  ="SELECT" + 
				"    pg_terminate_backend(pid)" + 
				" FROM" + 
				" pg_stat_activity" + 
				" WHERE" + 
				" pid <> pg_backend_pid()" + 
				" AND datname = 'MMU_PROD'" ;
		Query queryHdEmp = session.createQuery(qryStringHdEmp);
        int countHdEmp = queryHdEmp.executeUpdate();
		System.out.println(countHdEmp + "close all idle transaction.");
		t.commit();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return "success";
	}

	@Override
	public Map<String, List<Object[]>> filterUsers(JSONObject jsondata) {
		Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
		List totalMatches  =new ArrayList<>();
		int pageNo=0;
		int pageSize = 5;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

			if(jsondata.get("PN").toString() !=null)
				pageNo = Integer.parseInt(jsondata.get("PN").toString());

			String hql = "Select u.userId, u.userName from Users u, MasUserType t where u.userFlag ='1' and u.userTypeId=t.userTypeId";

			if (jsondata.has("userTypeName") && !jsondata.getString("userTypeName").trim().isEmpty()) {
				hql = hql+" and lower(t.userTypeName)=:userTypeName";
			}

			//Add more conditions if required any other dynamic fields to filter

			Query query = session.createQuery(hql);
			if (jsondata.has("userTypeName") && !jsondata.getString("userTypeName").trim().isEmpty()) {
				query.setParameter("userTypeName", jsondata.getString("userTypeName").toLowerCase());
			}

			totalMatches = query.list();
			if (pageNo > 0) {
				query.setFirstResult((pageSize) * (pageNo - 1));
				query.setMaxResults(pageSize);
			}
			List rec = query.list();

			map.put("usersList", rec);
			map.put("totalMatches", totalMatches);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}

	@Override
	public List<MasMmuVendor> getVendorList(Map<String, Object> requestData) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MasMmuVendor> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasMmuVendor.class).add(Restrictions.eq("status", "Y").ignoreCase())
					.addOrder(Order.asc("mmuVendorName"));
			list=cr.list();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	}
	
	public Object read(Class entityClass, Serializable id) {
        Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
        Object entity = session.load(entityClass, id);
        session.flush();
        getHibernateUtils.getHibernateUtlis().CloseConnection();
        return entity;
    }
	
	@Override
	public String getMasVendor(String vendorId) {
		List<MasMmuVendor> listMasCity = null;
		String vendorName="";
		String vendorIdVal="";
		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String [] massVendorArray=vendorId.split(",");
			List<Long>listMasssVendor=new ArrayList<>();
			for(String ss:massVendorArray) {
				listMasssVendor.add(Long.parseLong(ss.trim()));
			}
			listMasCity = session.createCriteria(MasMmuVendor.class) .add(Restrictions.in("mmuVendorId",listMasssVendor)).
					 list();
			if(CollectionUtils.isNotEmpty(listMasCity)) {
				int count=0;
				for(MasMmuVendor MasMmuVendor:listMasCity) {
					if(count==0) {
						vendorName=MasMmuVendor.getMmuVendorName();
						vendorIdVal=""+MasMmuVendor.getMmuVendorId();
					}
					else {
						vendorName+=","+MasMmuVendor.getMmuVendorName();
						vendorIdVal+=","+MasMmuVendor.getMmuVendorId();
					}
					count++;
				}
			}
			 
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return vendorName+"##"+vendorIdVal;
}
	
	public Long getAllMAsApplicationMax(){
		

		 MasApplication maxVal=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasApplication.class) 
					.addOrder(Order.desc("orderNo"));
			List<MasApplication>lisMas =   criteria.list();
			maxVal=lisMas.stream().collect(Collectors.maxBy(Comparator.comparingLong(MasApplication::getOrderNo))).get();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return maxVal.getOrderNo();	
		 
	}
	 
	@Override
	public String getPatientValid(JSONObject jsondata) {
		 
		 String message="";
		 Integer counter=0;
		 Integer counterOrderHdId=0;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
 
			//String hql = "Select u.patientId, u.mobileNumber from Patient u  where u.mobileNumber=:mobileNumber ";
			String MobileNo=jsondata.get("MobileNo").toString();
			MobileNo=HMSUtil.getReplaceString(MobileNo);
			
			String orderHdId=jsondata.get("orderHdId").toString();
			orderHdId=HMSUtil.getReplaceString(orderHdId);
			//Add more conditions if required any other dynamic fields to filter
			//Query query = session.createQuery(hql);
			
	 		//query.setParameter("mobileNumber", MobileNo);
	 		 //List  rec = query.list();

	 		 
	 		DgOrderhd dgOrderhd=(DgOrderhd) session.createCriteria(DgOrderhd.class ).add(Restrictions.eq("orderhdId", Long.parseLong(orderHdId))).uniqueResult();
 	 		/*if(rec.size()>0) {
	 			counter++;
	 		 }*/
	 		if(dgOrderhd!=null && dgOrderhd.getPatient()!=null && dgOrderhd.getPatient().getMobileNumber().toString().equalsIgnoreCase(MobileNo.trim())) {
	 			counterOrderHdId++;
	 		 }
	 		 
	 		/* if(counter==0) {
	 			message="You are not authorized to view other's patient report"+"##"+"0";
	 		 }*/
	 		 if(counterOrderHdId==0) {
	 			message="You have not entered correct mobile number to see the report."+"##"+"0"; 
	 		 }
	 		 
	 		/* if(counter==0 && counterOrderHdId!=0) {
		 			message="You are not authorized to view other's patient report"+"##"+"0";
		 		 }
	 		if(counter!=0 && counterOrderHdId==0) {
	 			message="Order Number is not correct"+"##"+"0";
	 		 }*/
	 		 if(counterOrderHdId!=0) {
		 			message="Validate User"+"##"+"1";
		 		 }
	 		 
	 		 
		}catch(Exception e) {
			message="Something wrong!"+"##"+"0";
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return message;
	}
	
	@Override
	public List getApprovingDetais(Long userTypeId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		//List<MasCamp> list = null;
		List<Object[]> searchList = null;
		try {	
			String Query="select ma.authority_id,ma.authority_code,ma.authority_name,ma.level_no,ma.final_approval,ma.order_no" + 
					" from mas_authority ma LEFT OUTER JOIN  authority_mapping am ON ma.authority_id=am.authority_id " + 
					" LEFT OUTER JOIN  mas_user_type mut ON am.user_type_id=mut.user_type_id\r\n" + 
					" where mut.user_type_id="+userTypeId+" and ma.status='y'";
			
			if (Query != null) 
			{
				searchList = session.createSQLQuery(Query).list();
			} 
			else
			{
				System.out.println("No Record Found");
			}
			return searchList;
			}catch(Exception ex) {
				ex.printStackTrace();
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
			return searchList;
		}
	 
	
}
