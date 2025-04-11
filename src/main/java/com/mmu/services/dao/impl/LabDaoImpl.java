package com.mmu.services.dao.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.DgResultEntryDtDao;
import com.mmu.services.dao.DgSampleCollectionDtDao;
import com.mmu.services.dao.LabDao;
import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.entity.DgFixedValue;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.DgNormalValue;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.DgSampleCollectionDt;
import com.mmu.services.entity.DgSampleCollectionHd;
import com.mmu.services.entity.DgSubMasInvestigation;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasSubChargecode;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.JavaUtils;

@Repository
@Transactional
public class LabDaoImpl implements LabDao{

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	DgResultEntryDtDao dgResultEntryDtDao;
	@Autowired
	OpdMasterDao opdMasterDao;
	
	@Autowired
	DgSampleCollectionDtDao dgSampleCollectionDtDao;
	/******************************************************* SAMPLE COLLECTION **************************************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingSampleCollectionWaitingListGrid(JSONObject jsonObject) {
		Long hospitalId = null;
		/*String serviceNo = "";
		String pName = "";
		String mobNumber = "";*/
		String serviceNo = "", patientName="", mobileNo = "";
		if(jsonObject.has("hospitalId")) {
			hospitalId = Long.parseLong(String.valueOf(jsonObject.get("hospitalId")));
		}
		
		 
		Map<String, Object> mapobj = new LinkedHashMap<String, Object>();		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			int pageNo = Integer.parseInt(String.valueOf(jsonObject.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
			Date fromDate = new Date();
			
			// convert date to calendar current date
	        Calendar c = Calendar.getInstance();
	        c.setTime(fromDate);
	        c.add(Calendar.DATE, 1);
	        Date toDate = c.getTime();
	        
			String fromDate1 = HMSUtil.convertDateToStringFormat(fromDate, "dd-MMM-yy");
			String toDate1 = HMSUtil.convertDateToStringFormat(toDate, "dd-MMM-yy");
			
//			
			if (jsonObject.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonObject.get("patientName")))  && jsonObject.has("patientName")) {
				patientName = "%" + jsonObject.get("patientName").toString() + "%";
			}

			if (jsonObject.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonObject.get("mobileNumber"))) && jsonObject.has("mobileNumber")) {
				mobileNo = "%" + jsonObject.get("mobileNumber").toString().trim() + "%";
			}
			
			String mainChargeCode=HMSUtil.getProperties("js_messages_en.properties", "mainchargeCodeLab");
			MasMainChargecode mmcc=null;
			mmcc =opdMasterDao. getMainChargeCode(mainChargeCode);
			Long	MAIN_CHARGECODE_ID=mmcc.getMainChargecodeId();
				
		String LAB_MARK = "I";
		StringBuilder sampleWaitingListQury = new StringBuilder();
			
			sampleWaitingListQury .append( "select patient.PATIENT_ID, patient.PATIENT_NAME, " ); 
			sampleWaitingListQury .append(" dgorderhd.ORDERHD_ID,dgorderhd.ORDER_date,  masdepartment.DEPARTMENT_NAME," ); 
			sampleWaitingListQury .append(" patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH,administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO,visit.VISIT_DATE " );
			sampleWaitingListQury .append(" from DG_ORDER_HD dgorderhd " );
			sampleWaitingListQury .append(" LEFT OUTER join DG_ORDER_DT dgorderdt on dgorderhd.ORDERHD_ID = dgorderdt.ORDERHD_ID "); 
			sampleWaitingListQury .append(" LEFT OUTER join PATIENT patient on patient.PATIENT_ID=dgorderhd.PATIENT_ID " ); 
			
			sampleWaitingListQury .append(" LEFT OUTER join MAS_ADMINISTRATIVE_SEX administrativeSex on administrativeSex.ADMINISTRATIVE_SEX_ID=patient.ADMINISTRATIVE_SEX_ID ");
			sampleWaitingListQury .append(" LEFT OUTER join MAS_DEPARTMENT masdepartment on dgorderhd.department_id = masdepartment.DEPARTMENT_ID ");
			sampleWaitingListQury .append(" left outer join DG_MAS_INVESTIGATION masInvestigation on dgorderdt.INVESTIGATION_ID = masInvestigation.INVESTIGATION_ID ");
			sampleWaitingListQury .append(" left outer join Visit visit on visit.visit_id = dgorderhd.visit_id ");
			//sampleWaitingListQury .append(" where dgorderhd.ORDER_STATUS =:ORDER_STATUS" );
			sampleWaitingListQury .append(" where dgorderdt.ORDER_STATUS =:ORDER_STATUS" );
			sampleWaitingListQury .append(" and dgorderhd.MMU_ID= :hospitalId" );
			sampleWaitingListQury.append(" and masInvestigation.MAIN_CHARGECODE_ID=:MAIN_CHARGECODE_ID ");
			
			//sampleWaitingListQury .append(" and dgorderdt.LAB_MARK=:LAB_MARK" ); 
			
			
			
				
			if (jsonObject.has("patientName")) {
				patientName = "%"+jsonObject.get("patientName").toString()+"%";
				if (jsonObject.get("patientName").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("patientName").toString().trim())) {
					
					sampleWaitingListQury .append(" and upper(patient.patient_name) like upper(:patient_name) ");													
					
				}
			}
			
			
			if (jsonObject.has("mobileNumber")) {
				mobileNo = jsonObject.get("mobileNumber").toString();
				if (jsonObject.get("mobileNumber").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("mobileNumber").toString().trim())) {
															
					sampleWaitingListQury .append(" and patient.MOBILE_NUMBER =:MOBILE_NUMBER ");													
					
				}
			}
			
			sampleWaitingListQury .append(" group by patient.PATIENT_ID, patient.PATIENT_NAME,  dgorderdt.LAB_MARK, "); 
			sampleWaitingListQury .append(" dgorderhd.ORDERHD_ID ,dgorderhd.ORDER_date," ); 
			sampleWaitingListQury .append(" masdepartment.DEPARTMENT_NAME,patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH, administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO,visit.VISIT_DATE ");
			
			sampleWaitingListQury .append(" order by dgorderhd.ORDER_date desc"); 
			
			//sampleWaitingListQury .append("visit.VISIT_DATE ");
		
			Query query = session.createSQLQuery(sampleWaitingListQury.toString());
			query.setParameter("ORDER_STATUS", "P");
			query.setParameter("hospitalId", hospitalId);
			query.setParameter("MAIN_CHARGECODE_ID", MAIN_CHARGECODE_ID);
			
			if (jsonObject.has("patientName")) {
				if (jsonObject.get("patientName").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("patientName").toString().trim())) {
					query.setParameter("patient_name", "%"+patientName+"%");
				}
				
			}
			if (jsonObject.has("mobileNumber")) {
				if (jsonObject.get("mobileNumber").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("mobileNumber").toString().trim())) {
					query.setParameter("MOBILE_NUMBER", mobileNo);
				}
				
			}
			
			List<Object[]> dgOrderhdsList = query.list();
			count = dgOrderhdsList.size();
			
			query = query.setFirstResult(pagingSize * (pageNo - 1));
			query = query.setMaxResults(pagingSize);
			dgOrderhdsList = query.list();
			mapobj.put("dgOrderhdsList", dgOrderhdsList);
			mapobj.put("count", count);
		
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapobj;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<?>> getPendingSampleCollection(JSONObject jsonObject) {
		
		Map<String, List<?>> mapobj = new LinkedHashMap<String, List<?>>();
		Long hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());		
		String serviceNo = "";
		String pName = "";
		String mobNumber = "";
		String LAB_MARK = "I";
		StringBuilder sampleWaitingListQury = new StringBuilder();
			
		try {
			
			sampleWaitingListQury .append( "select patient.PATIENT_ID, patient.SERVICE_NO, patient.PATIENT_NAME, patient.RELATION_ID, dgorderdt.LAB_MARK, " ); 
			sampleWaitingListQury .append(" dgorderhd.ORDERHD_ID,dgorderhd.ORDER_date, patient.EMPLOYEE_NAME, masdepartment.DEPARTMENT_NAME, " ); 
			sampleWaitingListQury .append(" patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH,administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO,masrelation.relation_Name,visit.VISIT_DATE ");
			sampleWaitingListQury .append(" from DG_ORDER_HD dgorderhd " );
			sampleWaitingListQury .append(" LEFT OUTER join DG_ORDER_DT dgorderdt on dgorderhd.ORDERHD_ID = dgorderdt.ORDERHD_ID "); 
			sampleWaitingListQury .append(" LEFT OUTER join PATIENT patient on patient.PATIENT_ID=dgorderhd.PATIENT_ID "); 
			sampleWaitingListQury .append(" LEFT OUTER join VU_MAS_RELATION masrelation on masrelation.RELATION_ID=patient.RELATION_ID "); 
			sampleWaitingListQury .append(" LEFT OUTER join MAS_ADMINISTRATIVE_SEX administrativeSex on administrativeSex.ADMINISTRATIVE_SEX_ID=patient.ADMINISTRATIVE_SEX_ID ");
			sampleWaitingListQury .append(" LEFT OUTER join MAS_DEPARTMENT masdepartment on dgorderhd.department_id = masdepartment.DEPARTMENT_ID ");
			sampleWaitingListQury .append(" left outer join Visit visit on visit.visit_id = dgorderhd.visit_id ");
			sampleWaitingListQury .append(" where dgorderhd.ORDER_STATUS =:ORDER_STATUS" ); 
			sampleWaitingListQury .append(" and dgorderhd.HOSPITAL_ID= :hospitalId" );
			sampleWaitingListQury .append(" and dgorderdt.LAB_MARK=:LAB_MARK" ); 
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
			String serNo ="";
			if (jsonObject.has("serviceNo")) {
				serviceNo = jsonObject.get("serviceNo").toString();
				//serNo = "%"+serviceNo+"%";
				if (jsonObject.get("serviceNo").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("serviceNo").toString().trim())) {
					
					sampleWaitingListQury .append(" and patient.service_no like :serviceNo ");													
					
				}
			}
				
			if (jsonObject.has("patientName")) {
				pName = jsonObject.get("patientName").toString();
				if (jsonObject.get("patientName").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("patientName").toString().trim())) {
					
					sampleWaitingListQury .append(" and patient.patient_name like :patient_name ");													
					
				}
			}
			
			
			if (jsonObject.has("mobileNumber")) {
				mobNumber = jsonObject.get("mobileNumber").toString();
				if (jsonObject.get("mobileNumber").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("mobileNumber").toString().trim())) {
															
					sampleWaitingListQury .append(" and patient.MOBILE_NUMBER like :MOBILE_NUMBER ");													
					
				}
			}
			sampleWaitingListQury .append(" group by patient.PATIENT_ID, patient.SERVICE_NO, patient.PATIENT_NAME, patient.RELATION_ID, dgorderdt.LAB_MARK, "); 
			sampleWaitingListQury .append(" dgorderhd.ORDERHD_ID ,dgorderhd.ORDER_date, patient.EMPLOYEE_NAME," ); 
			sampleWaitingListQury .append(" masdepartment.DEPARTMENT_NAME,patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH, administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO,masrelation.relation_Name,visit.VISIT_DATE ");
			
		
			SQLQuery query = session.createSQLQuery(sampleWaitingListQury.toString());
				
			query.setParameter("ORDER_STATUS", "P");
			query.setParameter("hospitalId", hospitalId);
			query.setParameter("LAB_MARK", LAB_MARK);
			
			if (jsonObject.has("serviceNo")) {
				query.setParameter("serviceNo", "%"+serviceNo+"%");
			}
			if (jsonObject.has("patientName")) {
				query.setParameter("patient_name", "%"+pName+"%");
			}
			if (jsonObject.has("mobileNumber")) {
				query.setParameter("MOBILE_NUMBER", "%"+mobNumber+"%");
			}
			
				List<Object[]> dgOrderhdsList = query.list();
				
			
			mapobj.put("dgOrderhdsList", dgOrderhdsList);
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapobj;
	}


	/**
	 * @Description : getPendingSampleCollectionDetails().
	 * @param JSONObject jsonObject
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<?>> getPendingSampleCollectionDetails(JSONObject jsonObject) {
		Map<String, List<?>> mapobj = new LinkedHashMap<String, List<?>>();
		Long orderhdId = null;
		Long hospId = null;
		Long lastChgBy = null;
		if(jsonObject.has("orderhdId")) {
			 orderhdId = Long.parseLong(jsonObject.get("orderhdId").toString());
		}
		if(jsonObject.has("hospitalId")) {
			 hospId = Long.parseLong(jsonObject.get("hospitalId").toString());
		}
		if(jsonObject.has("userId")) {
			lastChgBy = Long.parseLong(jsonObject.get("userId").toString());
		}
		
		List<Object[]> pendingSampleCollectionDtlsLists = null;
		
		StringBuilder pendingSampleCollectionDtlsListQury = new StringBuilder();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		pendingSampleCollectionDtlsListQury.append("select dgorderhd.ORDERHD_ID, dgorderhd.ORDER_date, dgorderhd.last_chg_date , ");
		pendingSampleCollectionDtlsListQury.append( "masdepartment.DEPARTMENT_NAME,  patient.PATIENT_ID, patient.PATIENT_NAME, "); 
		pendingSampleCollectionDtlsListQury.append(" patient.DATE_OF_BIRTH,administrativeSex.ADMINISTRATIVE_SEX_NAME, "); 
		pendingSampleCollectionDtlsListQury.append(" patient.MOBILE_NUMBER,  masdepartment.DEPARTMENT_ID,dgorderhd.ORDER_STATUS,dgorderhd.ORDER_NO, users.user_name,visit.VISIT_DATE "); 
		pendingSampleCollectionDtlsListQury.append("from DG_ORDER_HD dgorderhd ");  
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join DG_ORDER_DT dgorderdt on dgorderhd.ORDERHD_ID = dgorderdt.ORDERHD_ID "); 
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join PATIENT patient on patient.PATIENT_ID=dgorderhd.PATIENT_ID "); 
	
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join MAS_DEPARTMENT masdepartment on masdepartment.DEPARTMENT_ID=dgorderhd.DEPARTMENT_ID "); 
			
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join MAS_ADMINISTRATIVE_SEX administrativeSex on administrativeSex.ADMINISTRATIVE_SEX_ID=patient.ADMINISTRATIVE_SEX_ID ");
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join USERS users on users.user_id=dgorderhd.LAST_CHG_BY ");
		pendingSampleCollectionDtlsListQury.append("LEFT OUTER join DG_MAS_INVESTIGATION masinvestigation on masinvestigation.investigation_id=dgorderdt.investigation_id ");
		pendingSampleCollectionDtlsListQury .append(" left outer join Visit visit on visit.visit_id = dgorderhd.visit_id ");
		pendingSampleCollectionDtlsListQury.append("where dgorderhd.ORDERHD_ID=:ORDERHD_ID "); 
		pendingSampleCollectionDtlsListQury.append("and masinvestigation.main_chargecode_id=:MAIN_CHARGECODE_ID ");
		/*pendingSampleCollectionDtlsListQury.append("and dgorderhd.HOSPITAL_ID=:HOSPITAL_ID "); 
		pendingSampleCollectionDtlsListQury.append("and dgorderhd.LAST_CHG_BY=:LAST_CHG_BY ");	*/
			/*
			 * pendingSampleCollectionDtlsListQury.
			 * append("group by patient.PATIENT_ID, patient.SERVICE_NO, patient.PATIENT_NAME, patient.RELATION_ID, dgorderdt.LAB_MARK, "
			 * ); pendingSampleCollectionDtlsListQury.
			 * append(" dgorderhd.ORDERHD_ID ,dgorderhd.ORDER_date, patient.EMPLOYEE_NAME, "
			 * ); pendingSampleCollectionDtlsListQury.
			 * append(" masdepartment.DEPARTMENT_NAME,patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH, "
			 * ); pendingSampleCollectionDtlsListQury.
			 * append(" administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.last_chg_date, masdepartment.DEPARTMENT_ID,dgorderhd.ORDER_STATUS,dgorderhd.ORDER_NO,masrelation.relation_Name,rank.RANK_NAME,users.FIRST_NAME ,visit.VISIT_DATE "
			 * );
			 */
		
		SQLQuery query = session.createSQLQuery(pendingSampleCollectionDtlsListQury.toString());
		query.setParameter("ORDERHD_ID", orderhdId);
		query.setParameter("MAIN_CHARGECODE_ID", new Long(2));
		
		pendingSampleCollectionDtlsLists = query.list();
			mapobj.put("pendingSampleCollectionDtlsLists", pendingSampleCollectionDtlsLists);
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapobj;
	}


	/**
	 * @Desription : getInvestigationListdgOrderdtwise().
	 * @param JSONObject jsonObject
	 */
	@SuppressWarnings("unchecked")	
	@Override
	public List<Object[]> getInvestigationListdgOrderdtwise(JSONObject jsonObject) {
		List<Object[]> listofInvestigation = null;
		if(jsonObject!=null) {
			Long orderhdId = Long.parseLong(jsonObject.get("orderhdId").toString());
			String mainChargeCode=HMSUtil.getProperties("js_messages_en.properties", "mainchargeCodeLab");
			MasMainChargecode mmcc=null;
			mmcc =opdMasterDao. getMainChargeCode(mainChargeCode);
			Long	MAIN_CHARGECODE_ID=mmcc.getMainChargecodeId();
			String lab_mark = "I";
			StringBuilder investigationDetailsQuery = new StringBuilder();
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				investigationDetailsQuery.append("select dgorderdt.orderdt_id ,dgorderdt.ORDERHD_ID, masInvestigation.INVESTIGATION_ID, masInvestigation.INVESTIGATION_NAME," ); 
				investigationDetailsQuery.append(" massample.SAMPLE_ID, massample.sample_code, massample.sample_description,subchargecode.sub_chargecode_name, "); 
				investigationDetailsQuery.append(" dgorderdt.lab_mark, subchargecode.SUB_CHARGECODE_ID, collection.COLLECTION_ID ,collection.COLLECTION_NAME ");  
				investigationDetailsQuery.append(" from DG_ORDER_DT dgorderdt ");  
				investigationDetailsQuery.append(" left outer join DG_MAS_INVESTIGATION masInvestigation on dgorderdt.INVESTIGATION_ID = masInvestigation.INVESTIGATION_ID "); 
				investigationDetailsQuery.append(" left outer join MAS_MAIN_CHARGECODE mainchargecode on masInvestigation.MAIN_CHARGECODE_ID = mainchargecode.MAIN_CHARGECODE_ID "); 
				investigationDetailsQuery.append(" left outer join MAS_SAMPLE massample on masInvestigation.SAMPLE_ID=massample.SAMPLE_ID "); 
				investigationDetailsQuery.append(" left outer join MAS_SUB_CHARGECODE subchargecode on subchargecode.sub_chargecode_id=masInvestigation.sub_chargecode_id ");  
				investigationDetailsQuery.append(" left outer join dg_order_hd dgorderhd on dgorderhd.ORDERHD_ID=dgorderdt.ORDERHD_ID ");
				investigationDetailsQuery.append(" left outer join DG_MAS_COLLECTION collection on collection.collection_id=masInvestigation.collection_id");
				investigationDetailsQuery.append(" where dgorderdt.ORDERHD_ID=:ORDERHD_ID " ); 
				
				investigationDetailsQuery.append(" and dgorderdt.ORDER_STATUS =:ORDER_STATUS");
				investigationDetailsQuery.append(" and masInvestigation.MAIN_CHARGECODE_ID=:MAIN_CHARGECODE_ID ");
				
						
				SQLQuery query = session.createSQLQuery(investigationDetailsQuery.toString());
				
				query.setParameter("ORDERHD_ID", orderhdId);
				
				query.setParameter("ORDER_STATUS", "P");
				query.setParameter("MAIN_CHARGECODE_ID", MAIN_CHARGECODE_ID);
				
				listofInvestigation = query.list();
				
			}catch(Exception e) {
				e.printStackTrace(System.out);
			}finally {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}
		
	}
		return listofInvestigation;

 }



	@Override
	public Long submitSampleCollectionHeader(DgSampleCollectionHd sampleCollectionHd) {
		Long sampCollHdId = null;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			sampCollHdId = (Long) session.save(sampleCollectionHd);

			session.flush();
			session.clear();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampCollHdId;
	}


	/**
	 * @Description : submitSampleCollectionDetails().
	 * @param DgSampleCollectionDt sampleCollectionDt
	 */
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Long submitSampleCollectionDetails(DgSampleCollectionHd sampleCollectionHd, DgSampleCollectionDt sampleCollectionDt) {
		Long sampleCollectionDtId = null;
		Transaction tx = null;
		boolean execute=true;;
		Session session = null;
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			
			session.saveOrUpdate(sampleCollectionHd);
			
			Long sampleCollHeaderId = sampleCollectionHd.getSampleCollectionHdId();
			
			System.out.println("sampleCollHeaderId in dao"+sampleCollHeaderId);
			
			sampleCollectionDt.setSampleCollectionHdId(sampleCollHeaderId);
			session.save(sampleCollectionDt);
			
			Criteria cr= session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "hd").
					add(Restrictions.eq("hd.sampleCollectionHdId", sampleCollHeaderId));
			
			List<DgSampleCollectionDt>dtidList = cr.list();
			
			List<Long> listOfIds = new ArrayList<Long>();
			
			String s = "";
			String comma = "";
			for (DgSampleCollectionDt dt : dtidList) {
				if (s.equalsIgnoreCase("")) {
					s += dt.getOrderdtId() + "";

					comma += ",";
				} else {
					s += "," + dt.getOrderdtId();
					comma += ",";
				}
				listOfIds.add(dt.getOrderdtId());

			}
			
			System.out.println("listOfIds="+listOfIds);
			String sqlQuery = "update DG_ORDER_DT set ORDER_STATUS = 'C' where ORDERDT_ID in(:listOfIds)";

			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			SQLQuery query = session.createSQLQuery(sqlQuery);
			query.setParameterList("listOfIds", listOfIds);
			int result = query.executeUpdate();
			
			

			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			execute= false;
			tx.rollback();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		if(execute)
		{ 
			return new Long(1);
		}
		else
		{
			return new Long(0);
		}
	}
	
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Map<String,Object> submitSampleCollectionDetailsAll(JSONObject jsonObject,DgSampleCollectionHd sampleCollectionHd, Long visitId) {
		
		Map<String,Object>datamap= new HashMap<String,Object>();
		Long sampleCollectionDtId = null;
		Transaction tx = null;
		boolean execute=true;
		Session session = null;
		
		System.out.println("jsonObject="+jsonObject);
		
		try {
			
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
						
			
			Long orderhdId = null;
			Long hospitalId = null;
			Long lastChgBy = null;
			String userId = "";
			Long patientId = null;

			JSONObject json = new JSONObject();
			

			if (jsonObject != null) {

				String orderhdIds = JavaUtils.getReplaceString1(jsonObject.get("orderhdId").toString());
				orderhdIds = orderhdIds.replaceAll("\"", "");
				orderhdId = Long.parseLong(orderhdIds);								
				
				System.out.println("sampleCollectionHd="+sampleCollectionHd);
				
				Timestamp lastChgDate = new Timestamp(System.currentTimeMillis());

				if (sampleCollectionHd == null) 
				
				{
					sampleCollectionHd = new DgSampleCollectionHd();
					
					
					sampleCollectionHd.setOrderhdId(orderhdId);

					// code wuill be replace by MMU id

					String hospitalIds = JavaUtils.getReplaceString1(jsonObject.get("hospitalId").toString());
					hospitalIds = hospitalIds.replaceAll("\"", "");
					hospitalId = Long.parseLong(hospitalIds);
					sampleCollectionHd.setMmuId(hospitalId);

					userId = JavaUtils.getReplaceString1(jsonObject.get("userId").toString());
					userId = userId.replaceAll("\"", "");
					lastChgBy = Long.parseLong(userId);
					sampleCollectionHd.setLastChgBy(lastChgBy);

					Timestamp collectionDate = new Timestamp(System.currentTimeMillis());
					sampleCollectionHd.setCollectionDate(collectionDate);

					String departmentIdd = JavaUtils.getReplaceString1(jsonObject.get("departmentId").toString());
					departmentIdd = departmentIdd.replaceAll("\"", "");
					Long departmentId = Long.parseLong(departmentIdd);
					sampleCollectionHd.setDepartmentId(departmentId);

					lastChgBy = Long.parseLong(userId);
					sampleCollectionHd.setValidatedBy(lastChgBy);

					Timestamp diagnosisDate = new Timestamp(System.currentTimeMillis());
					sampleCollectionHd.setDiagnosisDate(diagnosisDate);

					
					sampleCollectionHd.setLastChgDate(lastChgDate);

					String orderNumberd = JavaUtils.getReplaceString1(jsonObject.get("orderNumber").toString().trim());
					orderNumberd = orderNumberd.replaceAll("\"", "");
					String orderNumberds = StringUtils.trim(orderNumberd);
					Long orderId = Long.parseLong(orderNumberds);
					sampleCollectionHd.setOrderId(orderId);

					String orderStatus = JavaUtils.getReplaceString1(jsonObject.get("orderStatus").toString());
					orderStatus = orderStatus.replaceAll("\"", "");
					sampleCollectionHd.setOrderStatus(orderStatus);

					String patientIdd = JavaUtils.getReplaceString1(jsonObject.get("patientId").toString());
					patientIdd = patientIdd.replaceAll("\"", "");
					patientId = Long.parseLong(patientIdd);
					sampleCollectionHd.setPatientId(patientId);
					
					//visitId = getVisitIdFromDgOrderHd(orderhdId);

					if (visitId != null) {
						sampleCollectionHd.setVisitId(visitId);
					}
					
					
				}
				
				session.saveOrUpdate(sampleCollectionHd);		
				
				
				Long sampleCollHeaderId = sampleCollectionHd.getSampleCollectionHdId();

				
				

				System.out.println("sampleCollHeaderId=" + sampleCollHeaderId);

				////////////////// save in dgsamplecollectionDt

				String collectedchecked = jsonObject.get("collectedCheckBox").toString();
				collectedchecked = JavaUtils.getReplaceString1(collectedchecked);
				collectedchecked = collectedchecked.replaceAll("\"", "");
				String[] collectedcheckedArray = collectedchecked.split(",");

				String investigations = jsonObject.get("investigationsArray").toString();
				investigations = JavaUtils.getReplaceString1(investigations);
				investigations = investigations.replaceAll("\"", "");
				String[] investigationsArray = investigations.split(",");

				String orderdtIdd = jsonObject.get("orderdtId").toString();
				orderdtIdd = JavaUtils.getReplaceString1(orderdtIdd);
				orderdtIdd = orderdtIdd.replaceAll("\"", "");
				String[] orderdtIdArray = orderdtIdd.split(",");

				String sampleCollections = jsonObject.get("sampleCollectionsArray").toString();
				sampleCollections = JavaUtils.getReplaceString1(sampleCollections);
				sampleCollections = sampleCollections.replaceAll("\"", "");
				String[] samplesId = sampleCollections.split(",");

				String remarks = jsonObject.get("remarksArray").toString();
				remarks = JavaUtils.getReplaceString1(remarks);
				remarks = remarks.replaceAll("\"", "");
				String[] remarkArray = remarks.split(",");

				String remarkId = jsonObject.get("remarkId").toString();
				remarkId = JavaUtils.getReplaceString1(remarkId);
				remarkId = remarkId.replaceAll("\"", "");
				String[] remarkIdArr = remarkId.split(",");

				String collectedBy = jsonObject.get("collectedBy").toString();
				collectedBy = JavaUtils.getReplaceString1(collectedBy);
				collectedBy = collectedBy.replaceAll("\"", "");
				String[] collectedByArray = collectedBy.split(",");

				String subChargeCodeId = jsonObject.get("subChargeCodeId").toString();
				subChargeCodeId = JavaUtils.getReplaceString1(subChargeCodeId);
				subChargeCodeId = subChargeCodeId.replaceAll("\"", "");
				String[] subChargeCodeIdArr = subChargeCodeId.split(",");

				String mainChargeCodeId = HMSUtil.getProperties("js_messages_en.properties", "mainchargeIdLab");
				System.out.println("mainChargeCodeId="+mainChargeCodeId);
				
				mainChargeCodeId="2";
					
				System.out.println("arraylength="+collectedcheckedArray.length);
				DgSampleCollectionDt sampleCollectionDt=null;

					for (int i = 0; i < collectedcheckedArray.length; i++) {
						
						
						if (collectedcheckedArray.length != 0) {
							if (collectedcheckedArray[i].equalsIgnoreCase("y")) {
								
								sampleCollectionDt = new DgSampleCollectionDt();
								//sampleCollectionDt.setSampleCollectionHdId(sampleCollHeaderId);

								sampleCollectionDt.setCollected(collectedcheckedArray[i]);

								sampleCollectionDt.setInvestigationId(Long.parseLong(investigationsArray[i]));
								sampleCollectionDt.setLastChgBy(lastChgBy);
								sampleCollectionDt.setValidated("V");
								sampleCollectionDt.setLastChgDate(lastChgDate);
								if(mainChargeCodeId !=null)
								{
									sampleCollectionDt.setMaincharge(Long.parseLong(mainChargeCodeId));
								}
								
								sampleCollectionDt.setOrderStatus("C");
								sampleCollectionDt.setQuantity("");
								sampleCollectionDt.setCollectedBy(Long.parseLong(collectedByArray[i]));
								if (remarkIdArr.length > 0 && i < remarkIdArr.length) {
									if (remarkIdArr[i] != null && !remarkIdArr[i].equalsIgnoreCase("")) {
										sampleCollectionDt.setReason(remarkIdArr[i]);
									} else {
										sampleCollectionDt.setReason("");
									}
								} else {
									sampleCollectionDt.setReason("");
								}

								sampleCollectionDt.setRejected("");

								Timestamp sampleCollDatetime = new Timestamp(System.currentTimeMillis());
								sampleCollectionDt.setSampleCollDatetime(sampleCollDatetime);
								if (samplesId[i] != null && !samplesId[i].equalsIgnoreCase("")) {
									sampleCollectionDt.setSampleId(Long.parseLong(samplesId[i]));
								} else {
									sampleCollectionDt.setSampleId(null);
								}
								if (subChargeCodeIdArr[i] != null && !subChargeCodeIdArr[i].equalsIgnoreCase("")) {
									sampleCollectionDt.setSubcharge(Long.parseLong(subChargeCodeIdArr[i]));
								} else {
									sampleCollectionDt.setSubcharge(null);
								}

								if (orderdtIdArray[i] != null && !orderdtIdArray[i].equalsIgnoreCase("")) {
									sampleCollectionDt.setOrderdtId(Long.parseLong(orderdtIdArray[i]));
								} else {
									sampleCollectionDt.setOrderdtId(null);
								}
								
								

							}
						}
						
						
						
						System.out.println("sampleCollHeaderId in dao"+sampleCollHeaderId);
						
						sampleCollectionDt.setSampleCollectionHdId(sampleCollHeaderId);
						session.save(sampleCollectionDt);
					}
					
				
					
					Criteria cr= session.createCriteria(DgSampleCollectionDt.class)
							.createAlias("dgSampleCollectionHd", "hd").
							add(Restrictions.eq("hd.sampleCollectionHdId", sampleCollHeaderId));
					
					List<DgSampleCollectionDt>dtidList = cr.list();
					
					List<Long> listOfIds = new ArrayList<Long>();
					
					String s = "";
					String comma = "";
					for (DgSampleCollectionDt dt : dtidList) {
						if (s.equalsIgnoreCase("")) {
							s += dt.getOrderdtId() + "";

							comma += ",";
						} else {
							s += "," + dt.getOrderdtId();
							comma += ",";
						}
						listOfIds.add(dt.getOrderdtId());

					}
					
					System.out.println("listOfIds="+listOfIds);
					String sqlQuery = "update DG_ORDER_DT set ORDER_STATUS = 'C' where ORDERDT_ID in(:listOfIds)";

					

					SQLQuery query = session.createSQLQuery(sqlQuery);
					query.setParameterList("listOfIds", listOfIds);
					int result = query.executeUpdate();
					
					
					tx.commit();
					session.flush();
					session.clear();
				

			}		
			
			
			
			

			
		} catch (Exception e) {
			execute= false;
			
			System.out.println(e);
			tx.rollback();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return datamap;
		
	}




	@SuppressWarnings("unchecked")
	@Override
	public Long getVisitIdFromDgOrderHd(Long orderhdId) {
		Long visitId = null;
		Session session = null;
		List<DgOrderhd> dgorderList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dgorderList = session.createCriteria(DgOrderhd.class).add(Restrictions.eq("orderhdId", orderhdId)).list();
			visitId = dgorderList.get(0).getVisitId();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return visitId;
	}



	@Override
	public boolean updateOrderStatusDgOrderHd(Long orderhdId) {
		boolean flag = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			DgOrderhd dgOrderhd = (DgOrderhd) session.load(DgOrderhd.class, orderhdId);
			dgOrderhd.getOrderStatus();
			if (dgOrderhd.getOrderStatus().equalsIgnoreCase("P")) {
				dgOrderhd.setOrderStatus("C");
			}
			session.update(dgOrderhd);
			tx.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	/******************************************************* SAMPLE VALIDATION **************************************************/
	
	/**
	 * @Description Method: getPendingSampleValidateList(). get List of sample validation modality wise.
	 * @param JSONObject jsonObject
	 * @param serviceNo
	 * @return
	 */
	//onload
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingSampleValidateListGrid(JSONObject jsonData) {
		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		Long hospitalId = null;
		Map<String,Object> map = new HashMap<>();
		
		if(jsonData.has("hospitalId")) {
			hospitalId = Long.parseLong(String.valueOf(jsonData.get("hospitalId")));
		}
		
		String patientName = "";
		String serviceNo = "";
		String mobileNo = "";

		if (jsonData.has("hospitalId")) {
			hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
		}

		if (jsonData.get("serviceNo") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("serviceNo"))) && jsonData.has("serviceNo")) {
			serviceNo = "%" + jsonData.get("serviceNo").toString() + "%";
		}
		if (jsonData.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("patientName")))  && jsonData.has("patientName")) {
			patientName = "%" + jsonData.get("patientName").toString() + "%";
		}

		if (jsonData.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("mobileNumber"))) && jsonData.has("mobileNumber")) {
			mobileNo = "%" + jsonData.get("mobileNumber").toString() + "%";
		}
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
		
			Date fromDate = new Date();
			String d1 = HMSUtil.convertDateToStringFormat(fromDate, "dd/MM/yyyy");
			Date fromDate1 = HMSUtil.convertStringTypeDateToDateType(d1);
			
			Calendar c = Calendar.getInstance();
	        c.setTime(fromDate);
	        c.add(Calendar.DATE, 1);
	        Date toDate = c.getTime();
	        String d2 = HMSUtil.convertDateToStringFormat(toDate, "dd/MM/yyyy");
			Date toDate1 = HMSUtil.convertStringTypeDateToDateType(d2);
	        
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.dgOrderHd", "dgOrderHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.createAlias("masSubChargecode", "masSubChargecode")
					.add(Restrictions.eq("dgSampleCollectionHd.hospitalId", hospitalId))
					.add(Restrictions.eq("orderStatus", "P"))
							.setProjection(Projections.projectionList()
									.add(Projections.property("sampleCollectionHdId").as("sampleCollectionHdId"))
									.add(Projections.property("subcharge").as("subcharge"))
									.add(Projections.groupProperty("sampleCollectionHdId"))									
									.add(Projections.groupProperty("subcharge")))
									.setResultTransformer(Transformers.aliasToBean(DgSampleCollectionDt.class));
			
			
			if( (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) ) {
				Calendar c2 = Calendar.getInstance();
		        c2.setTime(fromDate);
		        c2.add(Calendar.DATE, -15);
		        Date toDate2 = c2.getTime();
		        String date = HMSUtil.convertDateToStringFormat(toDate2, "dd/MM/yyyy");
				Date fromDate3 = HMSUtil.convertStringTypeDateToDateType(date);
				criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate3))
						.add(Restrictions.lt("lastChgDate", toDate1));
			}else {
				criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate1))
				.add(Restrictions.lt("lastChgDate", toDate1));
			}
			
			if (serviceNo != null && StringUtils.isNotBlank(serviceNo)) {
				criteria.add(Restrictions.ilike("patient.serviceNo", serviceNo));
			}
			if (patientName != null && StringUtils.isNotEmpty(patientName)) {
				criteria.add(Restrictions.ilike("patient.patientName", patientName));
			}

			if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
				criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
			}
			
			
			List<DgSampleCollectionDt> dgSampleCollectionDt2 = new ArrayList<>();
			List<Long> list = new ArrayList<>();
			dgSampleCollectionDtsList = criteria.list();
			
			count = dgSampleCollectionDtsList.size();
			
			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			dgSampleCollectionDtsList = criteria.list();
			
			map.put("count", count);
			map.put("list", dgSampleCollectionDtsList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getPendingSampleValidateList(JSONObject jsonObject) {
		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		Long hospitalId = null;
		String patientName = "";
		String serviceNo = "";
		String mobileNo = "";

		if (jsonObject.has("hospitalId")) {
			hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());
		}

		if (jsonObject.has("serviceNo")) {
			serviceNo = jsonObject.get("serviceNo").toString();
		}
		if (jsonObject.has("patientName")) {
			patientName = "%" + jsonObject.get("patientName").toString() + "%";
		}

		if (jsonObject.has("mobileNo")) {
			mobileNo = "%" + jsonObject.get("mobileNo").toString() + "%";
		}

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Date dt = new Date();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.setTime(dt);
			cal.add(Calendar.DATE, 1);
			Date from = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.add(Calendar.DATE, -15);
			Date to = cal.getTime();

			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class, "DgSampleCollectionDt")
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.dgOrderHd", "dgOrderHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("dgSampleCollectionHd.hospitalId", hospitalId))
					.add(Restrictions.eq("orderStatus", "P"))
					.add(Restrictions.between("DgSampleCollectionDt.lastChgDate", to, from));

			if (serviceNo != null && StringUtils.isNotBlank(serviceNo)) {
				criteria.add(Restrictions.eq("patient.serviceNo", serviceNo));

			}
			if (patientName != null && StringUtils.isNotEmpty(patientName)) {
				criteria.add(Restrictions.ilike("patient.patientName", patientName));
			}

			if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
				criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
			}
			dgSampleCollectionDtsList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsList;
	}


	/**
	 * @Description Method: getPendingSampleValidateDetails(). get pending Sample validation Details modality wise.
	 * @param sampleCollectionHdId
	 * @param sampleCollectionDtId
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPendingSampleValidateDetails(JSONObject jsonObject) {
		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Long subchargeCodeId = Long.parseLong(jsonObject.get("subchargeCodeId").toString());
		Long sampleCollectionHeaderId = Long.parseLong(jsonObject.get("sampleCollectionHeaderId").toString());

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.dgOrderHd", "dgOrderHd")
					.createAlias("dgSampleCollectionHd.patient", "patient").add(Restrictions.eq("orderStatus", "P"))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHeaderId))
					.add(Restrictions.eq("subcharge", subchargeCodeId));

			dgSampleCollectionDtsList = criteria.list();

			map.put("dgSampleCollectionDtsList", dgSampleCollectionDtsList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<DgSampleCollectionDt> getPendingSampleValidateDetailsForSampleValidate(JSONObject jsonObject) {
		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsValidateSampleList = null;
		Long subchargeCodeId = Long.parseLong(jsonObject.get("subchargeCodeId").toString());
		Long sampleCollectionHeaderId = Long.parseLong(jsonObject.get("sampleCollectionHeaderId").toString());
		/*String investigationType = jsonObject.get("investigationType").toString();*/
		/*System.out.println("investigationType :: "+investigationType);*/
		
		String mainChargeCode=HMSUtil.getProperties("js_messages_en.properties", "mainchargeCodeLab");
		MasMainChargecode mmcc=null;
		mmcc =opdMasterDao. getMainChargeCode(mainChargeCode);
		Long	MAIN_CHARGECODE_ID=mmcc.getMainChargecodeId();
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					/*.createAlias("dgOrderdt", "dgOrderdt")
					.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations",JoinType.LEFT_OUTER_JOIN)*/
					.createAlias("dgMasInvestigation", "dgMasInvestigation",JoinType.LEFT_OUTER_JOIN)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.add(Restrictions.eq("orderStatus", "P"))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHeaderId))
					.add(Restrictions.eq("subcharge", subchargeCodeId))
					.add(Restrictions.eq("maincharge", MAIN_CHARGECODE_ID));

			dgSampleCollectionDtsValidateSampleList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsValidateSampleList;
	}


	@Override
	@Transactional
	public boolean submitSampleCollectionHeaderAndDetails(DgSampleCollectionHd sampleCollectionHd,
			JSONObject jsonObject) {
		Long sampCollHdId = null;
		boolean flag = false;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			transaction = session.beginTransaction();
			sampCollHdId = (Long) session.save(sampleCollectionHd);

			// save in details
			DgSampleCollectionDt sampleCollectionDt = new DgSampleCollectionDt();
			String collectedchecked = jsonObject.get("collectedCheckBox").toString();
			collectedchecked = JavaUtils.getReplaceString1(collectedchecked);
			collectedchecked = collectedchecked.replaceAll("\"", "");
			String[] collectedcheckedArray = collectedchecked.split(",");

			String investigations = jsonObject.get("investigationsArray").toString();
			investigations = JavaUtils.getReplaceString1(investigations);
			investigations = investigations.replaceAll("\"", "");
			String[] investigationsArray = investigations.split(",");

			String sampleCollections = jsonObject.get("sampleCollectionsArray").toString();
			sampleCollections = JavaUtils.getReplaceString1(sampleCollections);
			sampleCollections = sampleCollections.replaceAll("\"", "");
			String[] samplesId = sampleCollections.split(",");

			String remarks = jsonObject.get("remarksArray").toString();
			remarks = JavaUtils.getReplaceString1(remarks);
			remarks = remarks.replaceAll("\"", "");
			String[] remarkArray = remarks.split(",");

			String diagNo = jsonObject.get("diagnosticNo").toString();
			diagNo = JavaUtils.getReplaceString1(diagNo);
			diagNo = diagNo.replaceAll("\"", "");
			String[] diagNoArr = diagNo.split(",");

			for (int i = 0; i < collectedcheckedArray.length; i++) {
				if (collectedcheckedArray.length != 0) {
					if (collectedcheckedArray[i].equalsIgnoreCase("y")) {
						sampleCollectionDt.setSampleCollectionHdId(sampCollHdId);

						sampleCollectionDt.setCollected(collectedcheckedArray[i]);

						String userId = JavaUtils.getReplaceString1(jsonObject.get("userId").toString());
						userId = userId.replaceAll("\"", "");
						Long lastChgBy = Long.parseLong(userId);
						sampleCollectionHd.setLastChgBy(lastChgBy);

						sampleCollectionDt.setInvestigationId(Long.parseLong(investigationsArray[i]));
						sampleCollectionDt.setLastChgBy(lastChgBy);

						Timestamp lastChgDate = new Timestamp(System.currentTimeMillis());
						sampleCollectionHd.setLastChgDate(lastChgDate);

						sampleCollectionDt.setLastChgDate(lastChgDate);
						Long maincharge = new Long(2);
						sampleCollectionDt.setMaincharge(maincharge);
						sampleCollectionDt.setOrderStatus("C");
						sampleCollectionDt.setQuantity("");
						sampleCollectionDt.setReason("");
						sampleCollectionDt.setRejected("");

						Timestamp sampleCollDatetime = new Timestamp(System.currentTimeMillis());
						sampleCollectionDt.setSampleCollDatetime(sampleCollDatetime);
						sampleCollectionDt.setSampleId(Long.parseLong(samplesId[i]));
						Long subcharge = new Long(2);
						sampleCollectionDt.setSubcharge(subcharge);

						submitSampleCollectionDetails1(sampleCollectionDt);
					}
				}
			}

			session.flush();
			session.clear();
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}



	private Boolean submitSampleCollectionDetails1(DgSampleCollectionDt sampleCollectionDt) {
		boolean flag = false;
		Long sampleCollectionDtId = null;
		Transaction tx = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			sampleCollectionDtId = (Long) session.save(sampleCollectionDt);

			session.flush();
			session.clear();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DgOrderdt> getDgOrderdtId(Long orderhdId) {
		List<DgOrderdt> dgOrderdtList = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgOrderdt.class).add(Restrictions.eq("orderhdId", orderhdId))
					.add(Restrictions.eq("orderStatus", "P"));
			dgOrderdtList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgOrderdtList;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> updateOrderStatusDgOrderDt(List<DgSampleCollectionDt> sampleCollectionDtId) {
		Transaction tx = null;
		Session session = null;
		List<DgSampleCollectionDt> sampleCollectionDtsList = null;
		List dgorderdtidList = new ArrayList();
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			for (DgSampleCollectionDt dt : sampleCollectionDtId) {
				dt.getDgOrderdt().getOrderdtId();
				dgorderdtidList.add(dt.getDgOrderdt().getOrderdtId());

			}
			tx = session.beginTransaction();
			DgOrderdt dgOrderdt = (DgOrderdt) session.load(DgOrderdt.class, (Serializable) dgorderdtidList);
			dgOrderdt.setOrderStatus("C");
			session.update(dgOrderdt);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampleCollectionDtsList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgOrderDtsIdsFromSampleCollDt(Long sampleCollHeaderId, String[] investigations) {
		List<DgSampleCollectionDt> list = new ArrayList<DgSampleCollectionDt>();
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			Long[] investigationIds = new Long[investigations.length];
			for (int i = 0; i < investigations.length; i++) {
				investigationIds[i] = Long.valueOf(investigations[i]);
			}
			
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollHeaderId))
					.add(Restrictions.in("investigationId", investigationIds))
					.add(Restrictions.eq("orderStatus", "P"));
			list = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;
	}



	@Override
	@org.springframework.transaction.annotation.Transactional
	public boolean updateDgOrderDt(List<DgSampleCollectionDt> dtidList) {
		Session session = null;
		boolean flag = false;
		Transaction tx = null;

		try {
			String s = "";
			String comma = "";
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			List<Long> listOfIds = new ArrayList<Long>();
			for (DgSampleCollectionDt dt : dtidList) {
				if (s.equalsIgnoreCase("")) {
					s += dt.getOrderdtId() + "";

					comma += ",";
				} else {
					s += "," + dt.getOrderdtId();
					comma += ",";
				}
				listOfIds.add(dt.getOrderdtId());

			}
			String sqlQuery = "update DG_ORDER_DT set ORDER_STATUS = 'C' where ORDERDT_ID in(:listOfIds)";

			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			SQLQuery query = session.createSQLQuery(sqlQuery);
			query.setParameterList("listOfIds", listOfIds);
			int result = query.executeUpdate();
			session.clear();
			session.flush();
			tx.commit();

			if (result != 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DgOrderdt> getDgOrderdtIdForUpdates(Long orderhdId) {
		List<DgOrderdt> dgOrderdtList = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgOrderdt.class).add(Restrictions.eq("orderhdId", orderhdId));
			dgOrderdtList = criteria.list();
			session.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgOrderdtList;
	}



	@SuppressWarnings("unchecked")
	@Override
	public DgSampleCollectionHd getSampleCollectionHdId(Long orderhdId) {

		Session session = null;
		List<DgSampleCollectionHd> sampleCollectionHdsList = null;
		DgSampleCollectionHd dgSampleCollectionHdObj = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionHd.class)
					.add(Restrictions.eq("orderhdId", orderhdId));
			sampleCollectionHdsList = criteria.list();

			if (sampleCollectionHdsList != null && !sampleCollectionHdsList.isEmpty()) {
				dgSampleCollectionHdObj = sampleCollectionHdsList.get(0);
			} else {
				dgSampleCollectionHdObj = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionHdObj;
	}

	/**
	 * Save In to DG Result Entry Header
	 */

	@SuppressWarnings("unchecked")
	@Override
	public DgResultEntryHd getDgResultEntryHd(Long sampleCollectionHdId) {
		Session session = null;
		List<DgResultEntryHd> dgResultEntryHdsList = null;
		DgResultEntryHd dgResultEntryHd = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgResultEntryHd.class)
					.add(Restrictions.eq("sampleCollectionHeaderId", sampleCollectionHdId));
			dgResultEntryHdsList = criteria.list();
			if (dgResultEntryHdsList != null && !dgResultEntryHdsList.isEmpty()) {
				dgResultEntryHd = dgResultEntryHdsList.get(0);
			} else {
				dgResultEntryHd = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryHd;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Long getVisitIdFromDgSampleCollectionHeader(Long sampleCollectionHdId, Session session) {
		//Session session = null;
		List<DgSampleCollectionHd> dgSampleCollectionHdsList = null;
		Long visitId = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionHd.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId));
			dgSampleCollectionHdsList = criteria.list();
			visitId = dgSampleCollectionHdsList.get(0).getVisitId();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return visitId;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgSampleCollectionDtIds(Long sampleCollectionHdId) {
		Session session = null;
		List<DgSampleCollectionDt> sampleCollectionDtsList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.eq("orderStatus", "P"));
			sampleCollectionDtsList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampleCollectionDtsList;
	}

	/************************************** Result Entry  ***********************/
	

	/**
	 * @Description: Method:getDgResultEntryDtIdsFromSampleCollDt(). get the List of Sample_Colllection_Dt_Id from DgResultEntryDetails
	 * @param: resultEntryHeader:resultEntryhdId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getSampleCollectionDtsIdsubChrgCode(Long sampleCollectionHdId, Long subchargeCodeId, Long investID) {
		Session session = null;
		List<DgSampleCollectionDt> list = new ArrayList<DgSampleCollectionDt>();
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.eq("subcharge", subchargeCodeId))
					.add(Restrictions.eq("orderStatus", "P"))
					.add(Restrictions.eq("investigationId", investID));
			list = criteria.list();
		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;
	}



	@Override
	@org.springframework.transaction.annotation.Transactional
	public boolean updateDgSampleCollectionDt(List<DgSampleCollectionDt> sampleCollectionDtIds, String[] reason, String[] additionalRemarks, String acceptflag) {
		Session session = null;
		boolean flag = false;
		Transaction tx = null;

		try {
			String s = "";
			String comma = "";
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			List<Long> listOfIds = new ArrayList<Long>();
			int count=0;
			Long result = null;
			if(acceptflag.equalsIgnoreCase("A")) {
			for (DgSampleCollectionDt dt : sampleCollectionDtIds) {
				
				if(reason.length>0 && count<reason.length) {
					if(reason[count]!=null && !reason[count].toString().equalsIgnoreCase("")) {
						dt.setReason(reason[count]);
					}else {
						dt.setReason("");
					}
				}else {
					dt.setReason("");
				}
				
				if(additionalRemarks.length>0 && count<additionalRemarks.length) {
					if(additionalRemarks[count]!=null && !additionalRemarks[count].toString().equalsIgnoreCase("")) {
						dt.setAdditionalRemarks(additionalRemarks[count]);
					}else {
						dt.setAdditionalRemarks("");
					}
				}else {
					dt.setAdditionalRemarks("");
				}
				
				
				dt.setOrderStatus("C");
				dt.setValidated("V");
				Timestamp lastChgDate = new Timestamp(System.currentTimeMillis());
				dt.setLastChgDate(lastChgDate);
				
				dgSampleCollectionDtDao.saveOrUpdate(dt);
			    result = dt.getSampleCollectionDtId();
			count++;
			}
		}
			if(acceptflag.equalsIgnoreCase("R")) {
				for (DgSampleCollectionDt dt : sampleCollectionDtIds) {
					
					if(reason.length>0 && count<reason.length) {
						if(reason[count]!=null && !reason[count].toString().equalsIgnoreCase("")) {
							dt.setReason(reason[count]);
						}else {
							dt.setReason("");
						}
					}else {
						dt.setReason("");
					}
					
					if(additionalRemarks.length>0 && count<additionalRemarks.length) {
						if(additionalRemarks[count]!=null && !additionalRemarks[count].toString().equalsIgnoreCase("")) {
							dt.setAdditionalRemarks(additionalRemarks[count]);
						}else {
							dt.setAdditionalRemarks("");
						}
					}else {
						dt.setAdditionalRemarks("");
					}
					
					dt.setOrderStatus("R");
					Timestamp lastChgDate = new Timestamp(System.currentTimeMillis());
					dt.setLastChgDate(lastChgDate);
					dgSampleCollectionDtDao.saveOrUpdate(dt);
				    result = dt.getSampleCollectionDtId();
				count++;
				}
			}
			
			tx.commit();
			session.flush();
			session.clear();
			if (result != 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgSampleCollectiondtIdForUpdates(List<Long> sampleCollectionHdId, Session session) {
		//Session session = null;
		List<DgSampleCollectionDt> list = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			/*Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.in("sampleCollectionHdId", sampleCollectionHdId));
			list = criteria.list();*/
			Criterion cr1=Restrictions.in("sampleCollectionHdId", sampleCollectionHdId);
			list =	dgSampleCollectionDtDao.findByCriteria(cr1);
		//	session.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;
	}


	@Override
	public boolean updateStatusDgSampleCollectionHd(Long sampleCollectionHdId) {
		boolean flag = false;
		Session session = null;
		Transaction tx = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			DgSampleCollectionHd dgSampleCollectionHd = (DgSampleCollectionHd) session.load(DgSampleCollectionHd.class,
					sampleCollectionHdId);
			dgSampleCollectionHd.getOrderStatus();
			if (dgSampleCollectionHd.getOrderStatus().equalsIgnoreCase("P")) {
				dgSampleCollectionHd.setOrderStatus("C");
			}
			session.update(dgSampleCollectionHd);
			tx.commit();
			session.flush();
			session.clear();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgSampleCollectionDtUOMId(Long sampleCollectionHdId) {
		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class).createAlias("dgOrderdt", "dgOrderdt")
					.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId));
			dgSampleCollectionDtsList = criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsList;
	}
	
	/************************************************************ Result Entry *********************************************************/

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultEntryWaitingListGrid(JSONObject jsonData) {
		Session session=null;
		List<DgSampleCollectionDt> dgSampleCollectionDts = null;
		Map<String,Object> map = new HashMap<>();
		 Long hospitalId = null;
		 String patientName = "";
		 String serviceNo = "";
		 String mobileNo = "";
		 
		 if (jsonData.has("hospitalId")) {
				hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
			}

			
			if (jsonData.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("patientName")))  && jsonData.has("patientName")) {
				patientName = "%" + jsonData.get("patientName").toString() + "%";
			}

			if (jsonData.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("mobileNumber"))) && jsonData.has("mobileNumber")) {
				mobileNo = "%" + jsonData.get("mobileNumber").toString() + "%";
				//mobileNo =   jsonData.get("mobileNumber").toString()  ;
			}
			
		 
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
		
			Date fromDate = new Date();
			String d1 = HMSUtil.convertDateToStringFormat(fromDate, "dd/MM/yyyy");
/*			String d1 = HMSUtil.changeDateToddMMyyyy(fromDate);*/
			Date fromDate1 = HMSUtil.convertStringTypeDateToDateType(d1);
			
			Calendar c = Calendar.getInstance();
	        c.setTime(fromDate);
	        c.add(Calendar.DATE, 1);
	        Date toDate = c.getTime();
	        String d2 = HMSUtil.convertDateToStringFormat(toDate, "dd/MM/yyyy");
			Date toDate1 = HMSUtil.convertStringTypeDateToDateType(d2);
			
			/*
			 * Criteria criteria = session.createCriteria(DgSampleCollectionDt.class,
			 * "DgSampleCollectionDt") .createAlias("dgSampleCollectionHd",
			 * "dgSampleCollectionHd") .createAlias("dgSampleCollectionHd.patient",
			 * "patient") .add(Restrictions.eq("dgSampleCollectionHd.mmuId", hospitalId))
			 * .add(Restrictions.eq("DgSampleCollectionDt.orderStatus", "C"))
			 * .setProjection(Projections.projectionList()
			 * .add(Projections.property("sampleCollectionHdId").as("sampleCollectionHdId"))
			 * .add(Projections.property("subcharge").as("subcharge"))
			 * .add(Projections.groupProperty("sampleCollectionHdId"))
			 * .add(Projections.groupProperty("subcharge")) )
			 * .setResultTransformer(Transformers.aliasToBean(DgSampleCollectionDt.class));
			 */
			
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class, "DgSampleCollectionDt")
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("dgSampleCollectionHd.mmuId", hospitalId))
					.add(Restrictions.eq("DgSampleCollectionDt.orderStatus", "C"))
					.setProjection(Projections.projectionList()
							.add(Projections.property("sampleCollectionHdId").as("sampleCollectionHdId"))							
							.add(Projections.groupProperty("sampleCollectionHdId")))								
							.setResultTransformer(Transformers.aliasToBean(DgSampleCollectionDt.class));
			
				if( (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) ) {
					Calendar c2 = Calendar.getInstance();
			        c2.setTime(fromDate);
			        c2.add(Calendar.DATE, -15);
			        Date toDate2 = c2.getTime();
			        String date = HMSUtil.convertDateToStringFormat(toDate2, "dd/MM/yyyy");
					Date fromDate3 = HMSUtil.convertStringTypeDateToDateType(date);
					criteria = criteria.add(Restrictions.ge("DgSampleCollectionDt.lastChgDate", fromDate3))
							.add(Restrictions.lt("DgSampleCollectionDt.lastChgDate", toDate1));
				}else {
					criteria = criteria.add(Restrictions.ge("DgSampleCollectionDt.lastChgDate", fromDate1))
					.add(Restrictions.lt("DgSampleCollectionDt.lastChgDate", toDate1));
				}
			
				
				if (patientName != null && StringUtils.isNotEmpty(patientName)) {
					criteria.add(Restrictions.ilike("patient.patientName", patientName));
				}

				if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
					criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo.trim()));
				}
				
			dgSampleCollectionDts = criteria.list();
			count = dgSampleCollectionDts.size();
			
			System.out.println("size="+dgSampleCollectionDts.size());
			
			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			dgSampleCollectionDts = criteria.list();
		
			map.put("count", count);
			map.put("list", dgSampleCollectionDts);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getResultEntryWaitingList(JSONObject jsonObject) {
		Session session = null;
		String serviceNo = "";
		String patientName = "";
		String mobileNo = "";
		Long hospitalId = null;

		if (jsonObject.has("hospitalId")) {
			hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());
		}

		if (jsonObject.has("serviceNo")) {
			serviceNo = jsonObject.get("serviceNo").toString();
		}
		if (jsonObject.has("patientName")) {
			patientName = "%" + jsonObject.get("patientName").toString() + "%";
		}

		if (jsonObject.has("mobileNo")) {
			mobileNo = "%" + jsonObject.get("mobileNo").toString() + "%";
		}
		List<DgSampleCollectionDt> dgSampleCollectionDts = null;

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Date dt = new Date();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.setTime(dt);
			cal.add(Calendar.DATE, 1);
			Date from = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.add(Calendar.DATE, -15);
			Date to = cal.getTime();

			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class, "DgSampleCollectionDt")
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("dgSampleCollectionHd.hospitalId", hospitalId))
					.add(Restrictions.eq("DgSampleCollectionDt.orderStatus", "C"))
					.add(Restrictions.between("DgSampleCollectionDt.lastChgDate", to, from));

			if (serviceNo != null && StringUtils.isNotBlank(serviceNo)) {
				criteria.add(Restrictions.eq("patient.serviceNo", serviceNo));

			}
			if (patientName != null && StringUtils.isNotEmpty(patientName)) {
				criteria.add(Restrictions.ilike("patient.patientName", patientName));
			}

			if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
				criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
			}

			dgSampleCollectionDts = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDts;
	}

	
	/**
	 * @Description Method: getResultEntryDetails(). get the result entry details.
	 * @param resultEntHdId
	 */
	Long departId = null;
	@SuppressWarnings("unchecked")
	@Override
	//public List<DgSampleCollectionDt> getResultEntryDetails(Long sampleCollectionHdId, Long subchargeCodeId) {
	public List<DgSampleCollectionDt> getResultEntryDetails(Long sampleCollectionHdId) {
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		List<DgSampleCollectionDt> list = null;
		List<Object> invList = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("orderStatus", "C").ignoreCase())
					// .add(Restrictions.eq("dgSampleCollectionHd.departmentId", departId))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId));
					// add internal investigation where clause here
			dgSampleCollectionDtsList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsList;
	}

	/**
	 * @Description:Method: getResultEntryDetailsInvestigations(). get the investigation details according to resultEntryHDId.
	 * @param resultEntrtHdId  
	 */

	@SuppressWarnings("unchecked")
	@Override
	//public List<DgSampleCollectionDt> getResultEntryDetailsInvestigations(Long sampleCollectionHdId, Long subchargeCodeId, String investigationType) {
	//public Map<String, Object> getAllInvestigations(Long sampleCollectionHdId, Long subchargeCodeId)
	public Map<String, Object> getAllInvestigations(Long sampleCollectionHdId)
	
	{
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Session session = null;
		List<Object[]> list = null;
		List<DgSampleCollectionDt> dgSampCollDtsInvList = null;
		List<DgSampleCollectionDt> dtList = null;
		List<Object> investigationList = new ArrayList<Object>();
		List<DgSubMasInvestigation> subInvestigationList = null;
		List<Object> subInvList = new ArrayList<Object>();
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		String comparisionType = "";
		List<DgSampleCollectionDt> invTypeTempList = null;
		List<DgSampleCollectionDt> invListSampledt = new ArrayList<DgSampleCollectionDt>();
		List<DgMasInvestigation> invTypeCriList = new ArrayList<DgMasInvestigation>();
		
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria invTypeCr = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("orderStatus", "C").ignoreCase())
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.eq("maincharge", 2l));
			
			invListSampledt = invTypeCr.list();
			List<Long> invList = new ArrayList<Long>();
			for(DgSampleCollectionDt dt:invListSampledt) {
				invList.add(dt.getInvestigationId());
			}
			
			System.out.println("invList="+invList);
			//get investigation type from dgmasinvestigation	
			if(invList!=null && invList.size()>0) {
			Criteria invTypeCri = session.createCriteria(DgMasInvestigation.class)
					.add(Restrictions.in("investigationId", invList))
					.setProjection(Projections.projectionList()
							.add(Projections.property("investigationType").as("investigationType")))
					.setResultTransformer(Transformers.aliasToBean(DgMasInvestigation.class));
			invTypeCriList = invTypeCri.list();
			}
			
			
			List<String> invTypeListStr = new ArrayList<String>();
			for(DgMasInvestigation inv : invTypeCriList) {
				invTypeListStr.add(inv.getInvestigationType());
			}
			
			
			for(int ii=0;ii<invTypeListStr.size();ii++) {
				if(invTypeListStr.get(ii).equalsIgnoreCase("s")) {
					String investigationType = invTypeListStr.get(ii);
					Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
							/*.createAlias("dgOrderdt", "dgOrderdt")
							.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")*/
							.createAlias("dgMasInvestigation", "dgMasInvestigation")
							.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
							.createAlias("dgSampleCollectionHd.patient", "patient")
							.add(Restrictions.eq("orderStatus", "C").ignoreCase())
							.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
							/* .add(Restrictions.eq("subcharge", subchargeCodeId)) */
							.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType));
					dgSampCollDtsInvList = criteria.list();
					
				}else if(invTypeListStr.get(ii).equalsIgnoreCase("m")) {
					String investigationType = invTypeListStr.get(ii);
					Criteria cr = session.createCriteria(DgSampleCollectionDt.class)
							
							/*.createAlias("dgOrderdt", "dgOrderdt")
							.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")*/
							.createAlias("dgMasInvestigation", "dgMasInvestigation")
							.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
							.createAlias("dgSampleCollectionHd.patient", "patient")
							.add(Restrictions.eq("orderStatus", "C").ignoreCase())
							.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
							/* .add(Restrictions.eq("subcharge", subchargeCodeId)) */
							.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType));

					dtList = cr.list();
					
					if(dtList!=null && dtList.size()>0) {
						Long genderId = dtList.get(0).getDgSampleCollectionHd().getPatient().getAdministrativeSexId();
						// get gender Code
						System.out.println("genderId :: "+genderId);
						List<MasAdministrativeSex> listOfGender = session.createCriteria(MasAdministrativeSex.class)
								.add(Restrictions.eq("administrativeSexId", genderId)).list();
						String genderCode = listOfGender.get(0).getAdministrativeSexCode();
						System.out.println("genderCode :: "+genderCode);

						for (DgSampleCollectionDt dt : dtList) {

							investigationList.add(dt.getInvestigationId());

						}
						
						
						// get the subInvestigationId from dgSubMasInvestigation
						Criteria cr1 = session.createCriteria(DgSubMasInvestigation.class)
								.createAlias("masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN)
								.add(Restrictions.in("investigationId", investigationList))
								.add(Restrictions.eq("status", "Y").ignoreCase())
								.addOrder(Property.forName("orderNo").asc());
						subInvestigationList = cr1.list();

						for (DgSubMasInvestigation subInv : subInvestigationList) {
	                        subInvList.add(subInv.getSubInvestigationId());
							comparisionType = subInv.getComparisonType();
							
							// DG_FIXED_VALUE
							if (comparisionType.equalsIgnoreCase("f")) {
								Criteria cr2 = session.createCriteria(DgFixedValue.class)
										.createAlias("dgSubMasInvestigation", "dgSubMasInvestigation")
										.add(Restrictions.in("subInvestigationId", subInvList))
										.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType));

								fixedValueList = cr2.list();
								mapFixedValue = new HashMap<String, Object>();
								for (int i = 0; i < fixedValueList.size(); i++) {
									mapFixedValue.put(fixedValueList.get(i).getFixedValue().trim(),
											fixedValueList.get(i).getFixedId() + "@@"
													+ fixedValueList.get(i).getFixedValue().trim());

								}
								
							}
							

							// DG_NORMAL_VALUE
							if (comparisionType.equalsIgnoreCase("v")) {
								Criteria crNorVal = session.createCriteria(DgNormalValue.class)
										.createAlias("dgSubMasInvestigation","dgSubMasInvestigation")
										.add(Restrictions.eq("sex", genderCode).ignoreCase());

								if (genderCode.equalsIgnoreCase(genderCode)) {
									crNorVal.add(Restrictions.in("dgSubMasInvestigation.subInvestigationId", subInvList))
											.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType))
											.add(Restrictions.eq("sex", genderCode).ignoreCase());
								}
								normalValList = crNorVal.list();
							}

						}
						
						/*// DG_FIXED_VALUE
						if (comparisionType.equalsIgnoreCase("f")) {
							Criteria cr2 = session.createCriteria(DgFixedValue.class)
									.createAlias("dgSubMasInvestigation", "dgSubMasInvestigation")
									.add(Restrictions.in("subInvestigationId", subInvList))
									.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType));

							fixedValueList = cr2.list();
							mapFixedValue = new HashMap<String, Object>();
							for (int i = 0; i < fixedValueList.size(); i++) {
								mapFixedValue.put(fixedValueList.get(i).getFixedValue().trim(),
										fixedValueList.get(i).getFixedId() + "@@"
												+ fixedValueList.get(i).getFixedValue().trim());

							}
							
						}
						

						// DG_NORMAL_VALUE
						if (comparisionType.equalsIgnoreCase("v")) {
							Criteria crNorVal = session.createCriteria(DgNormalValue.class)
									.createAlias("dgSubMasInvestigation","dgSubMasInvestigation")
									.add(Restrictions.eq("sex", genderCode).ignoreCase());

							if (genderCode.equalsIgnoreCase(genderCode)) {
								crNorVal.add(Restrictions.in("dgSubMasInvestigation.subInvestigationId", subInvList))
										.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType))
										.add(Restrictions.eq("sex", genderCode).ignoreCase());
							}
							normalValList = crNorVal.list();
						}*/
					}
					
				 
				}else {
					String investigationType = invTypeListStr.get(ii);
					Criteria cr1 = session.createCriteria(DgSampleCollectionDt.class).createAlias("dgOrderdt", "dgOrderdt")
							.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")
							.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
							.createAlias("dgSampleCollectionHd.patient", "patient")
							.add(Restrictions.eq("orderStatus", "C").ignoreCase())
							.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
							/* .add(Restrictions.eq("subcharge", subchargeCodeId)) */
							.add(Restrictions.eq("dgMasInvestigations.investigationType", investigationType));
					
					invTypeTempList = cr1.list();
				
				}
			}
			

			mapdata.put("dgSampCollDtsInvList", dgSampCollDtsInvList);
			mapdata.put("subInvestigationList", subInvestigationList);
			mapdata.put("subInvList", subInvList);
			mapdata.put("fixedValueList", fixedValueList);
			mapdata.put("mapFixedValue", mapFixedValue);
			mapdata.put("normalValList", normalValList);
			mapdata.put("invTypeTempList", invTypeTempList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapdata;
	}

	@Override
	public List<DgOrderdt> getAllInvestigationFromDgOrderDt(String orderhdId) {
		
		try {
			
			
		}catch(Exception e) {
			
		}
		return null;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<MasSubChargecode> getSubChargeCode(Long sampleCollectionHdId) {
		Session session = null;
		List<MasSubChargecode> chargecodesList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("masSubChargecode", "masSubChargecode")
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId));
			chargecodesList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return chargecodesList;
	}
	
	/**
	 * @Description submitDgResultEntry().
	 */
	@Override
	public Long submitDgResultEntry(DgResultEntryHd dgResultEntryHd,Session session) {
		//Session session = null;
		Long resultEntryHdId = null;
		Transaction tx = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx = session.beginTransaction();
			session.saveOrUpdate(dgResultEntryHd);
			resultEntryHdId = dgResultEntryHd.getResultEntryId();

			/*tx.commit();*/
			session.flush();
			session.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return resultEntryHdId;
	}

	/**
	 * @Description submitDgResultEntryDt().
	 */
	@Override
	public Long submitDgResultEntryDt(DgResultEntryDt dgResultEntryDt, Session session) {
		//Session session = null;
		//Transaction tx = null;
		Long dgResultEntryDtId = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			//tx = session.beginTransaction();
			dgResultEntryDtId = (Long) session.save(dgResultEntryDt);
			//tx.commit();
			session.flush();
			session.clear();

		} catch (Exception e) {
			//tx.rollback();
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryDtId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgSampCollDtIdFromDgSamCollDts(Long sampleCollectionHdId,
			Long subchargecodeId, Session session) {
		//Session session = null;
		List<DgSampleCollectionDt> sampCollDtlist = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.or(Restrictions.eq("orderStatus", "C"),Restrictions.eq("orderStatus", "R")))
					.add(Restrictions.eq("subcharge", subchargecodeId));
			sampCollDtlist = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return sampCollDtlist;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DgMasInvestigation> getTestOrderNoFromDgMasInvestigation(List<Object> investigationsList, Session session) {
		//Session session = null;
		List<DgMasInvestigation> testOrderNo = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgMasInvestigation.class)
					.add(Restrictions.in("investigationId", investigationsList))
					.add(Restrictions.eq("mainChargecodeId", 2l));
			testOrderNo = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return testOrderNo;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getDgSamCollDtIdFromDgResultEntDts(List<Long> resEntHdIds, Session session) {
		//Session session = null;
		List<Object[]> listObject = null;
		StringBuilder dgresultEntDtqry = new StringBuilder();
		try {

			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dgresultEntDtqry.append(
					"select sample_collection_dt_id, RESULT_ENTRY_HD_ID from DG_RESULT_ENTRY_DT where  RESULT_ENTRY_HD_ID in :resEntHdIds");
			dgresultEntDtqry.append(" group by SAMPLE_COLLECTION_DT_ID, RESULT_ENTRY_HD_ID");

			Query query = session.createSQLQuery(dgresultEntDtqry.toString());
			query.setParameterList("resEntHdIds", resEntHdIds);

			listObject = query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listObject;
	}

	@Override
	public boolean updateDgSampleCollectionDtForResultEntry(List<Long> dgSampleCollectionDts, Session session) {
		//Session session = null;
		boolean flag = false;
		//Transaction tx = null;
		try {

			String sqlquery = "update DG_SAMPLE_COLLECTION_DT set ORDER_STATUS='E' where sample_collection_dt_id in (:listOfIds)";
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();

			//tx = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(sqlquery);
			query.setParameterList("listOfIds", dgSampleCollectionDts);
			int result = query.executeUpdate();

			//tx.commit();
			//session.flush();

			if (result != 0) {
				flag = true;
			} else {
				flag = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public DgResultEntryDt getResultEntryDetailsObject(JSONObject jsonObject) {
		Session session = null;
		List<DgResultEntryDt> entryDtsList = null;
		DgResultEntryDt dgResultEntryDt = null;
		try {
			String resultEntryDtId = jsonObject.get("sampleCollectionDtId").toString();
			resultEntryDtId = JavaUtils.getReplaceString1(resultEntryDtId);
			resultEntryDtId = resultEntryDtId.replaceAll("\"", "");
			String[] resultEntryDtIds = resultEntryDtId.split(",");

			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgResultEntryDt.class)
					.add(Restrictions.eq("resultEntryDetailId", Long.parseLong(resultEntryDtIds[0])));
			entryDtsList = criteria.list();
			if (entryDtsList != null && !entryDtsList.isEmpty()) {
				dgResultEntryDt = entryDtsList.get(0);
			} else {
				dgResultEntryDt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryDt;
	}
	
	@Override
	public boolean updateOrderStatusDgSampleCollectionHd(List<Long> sampleCollectionHdId, Session session) {
		boolean flag = false;
		//Session session = null;
		//Transaction tx = null;
		Long headerId = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			for (int i = 0; i < sampleCollectionHdId.size(); i++) {
				headerId = sampleCollectionHdId.get(0);
			}
			//tx = session.beginTransaction();
			DgSampleCollectionHd dgsampleCollHd = (DgSampleCollectionHd) session.get(DgSampleCollectionHd.class,
					headerId);
			dgsampleCollHd.getOrderStatus();
			if (dgsampleCollHd.getOrderStatus().equalsIgnoreCase("P")) {
				dgsampleCollHd.setOrderStatus("E");
			}
			session.update(dgsampleCollHd);
			//tx.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DgSampleCollectionDt> getDgSampleDtIds(List<Object> invList, Long sampleCollectionHdId) {
		Session session = null;
		List<DgSampleCollectionDt> dtList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(DgSampleCollectionDt.class).add(Restrictions.eq("orderStatus", "C"));

			Criterion invListcr = Restrictions.in("investigationId", invList);
			Criterion hdId = Restrictions.eq("dgSampleCollectionHd", sampleCollectionHdId);
			LogicalExpression andExp = Restrictions.and(invListcr, hdId);

			cr.add(andExp);
			dtList = cr.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dtList;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public DgResultEntryHd getResultEntryObject(Long investigationid, Long orderHdId, Session session) {
		//Session session = null;
		List<DgResultEntryHd> list = null;
		DgResultEntryHd dgResultEntryHd = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dgResultEntryHd = (DgResultEntryHd) session.createCriteria(DgResultEntryHd.class)
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.add(Restrictions.eq("dgMasInvestigation.investigationId", investigationid))
						.createAlias("dgOrderhd", "dgOrderhd")
						.add(Restrictions.eq("dgOrderhd.orderhdId", orderHdId)).uniqueResult();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryHd;
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public DgResultEntryHd getResultEntryObject(Long subchargeId, Long mainChargeCodeId, Long orderHdId, Session session) {
		//Session session = null;
		List<DgResultEntryHd> list = null;
		DgResultEntryHd dgResultEntryHd = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dgResultEntryHd = (DgResultEntryHd) session.createCriteria(DgResultEntryHd.class)
						//.createAlias("dgMasInvestigation", "dgMasInvestigation")
						//.add(Restrictions.eq("dgMasInvestigation.subChargecodeId", subchargeId))
						.add(Restrictions.eq("subChargecodeId", subchargeId))						
						.createAlias("dgOrderhd", "dgOrderhd")
						.add(Restrictions.eq("dgOrderhd.orderhdId", orderHdId))
						.add(Restrictions.eq("mainChargecodeId", mainChargeCodeId))
						.uniqueResult();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryHd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DgResultEntryDt getInvestigationIdFrmResultEntDt(Long resultEnterHdId) {
		Session session = null;
		DgResultEntryDt dgResultEntryDt = null;
		List<DgResultEntryDt> list = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(DgResultEntryDt.class)
					.add(Restrictions.eq("resultEntryId", resultEnterHdId));
				list = cr.list();
			if(CollectionUtils.isNotEmpty(list)) {
				dgResultEntryDt = list.get(0);
			}else {
				return dgResultEntryDt;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryDt;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getHeadeIdFrmDgSampleCollectionDt(List<Long> dgSampleCollectionDts, Session session) {
		//Session session = null;
		StringBuffer sbQuery = new StringBuffer();
		List<Object[]> listObject = null;
		try {
			//session = getHibernateUtils.getHibernateUtlis().OpenSession();
			sbQuery.append("select SAMPLE_COLLECTION_HD_ID, SAMPLE_COLLECTION_DT_ID from DG_SAMPLE_COLLECTION_DT ");
			sbQuery.append(" where SAMPLE_COLLECTION_DT_ID in (:sampleCollDtId) and ORDER_STATUS=:order_status");
			sbQuery.append(" group by SAMPLE_COLLECTION_HD_ID, SAMPLE_COLLECTION_DT_ID");
			
			Query query = session.createSQLQuery(sbQuery.toString());
			query.setParameterList("sampleCollDtId", dgSampleCollectionDts);
			query.setParameter("order_status", "E");
			listObject = query.list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listObject;
	}
	


	/***************************************************************** Result Validation  ********************************************************/
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultValidationWaitingListGrid(JSONObject jsonData) {
		Map<String,Object> map = new HashMap<>();
		Session session=null;
		
		//List<DgResultEntryDt> dgResultEntryDtList = null;
		List<DgResultEntryHd> dgResultEntryHdList = null;
		Long hospitalId = null;	
		 String patientName = "";
		 String serviceNo = "";
		 String mobileNo = "";
		 
		 if (jsonData.has("hospitalId")) {
				hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
			}

			if (jsonData.get("serviceNo") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("serviceNo"))) && jsonData.has("serviceNo")) {
				serviceNo = jsonData.get("serviceNo").toString();
			}
			if (jsonData.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("patientName")))  && jsonData.has("patientName")) {
				patientName = "%" + jsonData.get("patientName").toString() + "%";
			}

			if (jsonData.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("mobileNumber"))) && jsonData.has("mobileNumber")) {
				mobileNo = "%" + jsonData.get("mobileNumber").toString() + "%";
			}
			
		
			try {
				session = getHibernateUtils.getHibernateUtlis().OpenSession();					
					int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
					int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
					int count=0;
				
					Date fromDate = new Date();
					String d1 = HMSUtil.convertDateToStringFormat(fromDate, "dd/MM/yyyy");
					Date fromDate1 = HMSUtil.convertStringTypeDateToDateType(d1);
					
					Calendar c = Calendar.getInstance();
			        c.setTime(fromDate);
			        c.add(Calendar.DATE, 1);
			        Date toDate = c.getTime();
			        String d2 = HMSUtil.convertDateToStringFormat(toDate, "dd/MM/yyyy");
					Date toDate1 = HMSUtil.convertStringTypeDateToDateType(d2);
							
					Criteria criteria = session.createCriteria(DgResultEntryHd.class,"dgResultEntryHd")
								.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
								.createAlias("dgResultEntryHd.patient", "patient")
								.add(Restrictions.eq("hospitalId", hospitalId))
								.add(Restrictions.eq("resultStatus", "E").ignoreCase())
								.add(Restrictions.isNull("verified"))
								.add(Restrictions.eq("mainChargecodeId", new Long(2)));
								/*.setProjection(Projections.projectionList()
										.add(Projections.property("subChargecodeId").as("subChargecodeId"))
										.add(Projections.groupProperty("subChargecodeId")))
										.setResultTransformer(Transformers.aliasToBean(DgResultEntryHd.class));*/
					
					/*Criteria criteria = session.createCriteria(DgResultEntryDt.class,"dgResultEntryDt")
					         .createAlias("dgResultEntryDt.dgResultEntryHd", "dgResultEntryHd")
					        .createAlias("dgResultEntryHd.dgOrderhd", "dgOrderHd")					       
					         .createAlias("dgResultEntryDt.dgOrderdt", "dgOrderdt")	
					         .createAlias("dgResultEntryHd.dgSampleCollectionHd", "dgSampleCollectionHd")							 
							   .createAlias("dgResultEntryDt.dgSampleCollectionDt", "dgSampleCollectionDt")
					           .createAlias("dgResultEntryHd.masHospital", "masHospital",JoinType.LEFT_OUTER_JOIN)					         
					           .createAlias("dgResultEntryDt.dgMasInvestigation", "dgMasInvestigation",JoinType.LEFT_OUTER_JOIN)
					           .createAlias("dgMasInvestigation.masUOM", "masUOM",JoinType.LEFT_OUTER_JOIN)
					           .createAlias("dgResultEntryHd.patient", "patient")
					           .createAlias("patient.masAdministrativeSex", "masAdministrativeSex",JoinType.LEFT_OUTER_JOIN)
					           .createAlias("patient.masRelation", "masRelation",JoinType.LEFT_OUTER_JOIN)					           
					           
					           .add(Restrictions.eq("dgMasInvestigation.mainChargecodeId", 2l))
					            .add(Restrictions.eq("dgResultEntryHd.resultStatus", "P").ignoreCase())
								.add(Restrictions.isNull("dgResultEntryHd.verified"))
								
								.setProjection(Projections.projectionList()
										.add(Projections.property("dgResultEntryHd.sampleCollectionHeaderId").as("sampleCollectionHeaderId"))
										.add(Projections.property("dgResultEntryHd.subChargecodeId").as("subChargecodeId"))
										.add(Projections.property("dgResultEntryHd.patient").as("patient"))
										.add(Projections.property("dgResultEntryHd").as("dgResultEntryHd"))
										.add(Projections.groupProperty("dgResultEntryHd.sampleCollectionHeaderId"))									
										.add(Projections.groupProperty("dgResultEntryHd.subChargecodeId"))
										.add(Projections.groupProperty("dgResultEntryHd.patient"))
										.add(Projections.groupProperty("dgResultEntryHd"))
										.add(Projections.groupProperty("dgResultEntryHd.subChargecodeId"))										
										.add(Projections.groupProperty("dgResultEntryHd.dgSampleCollectionHd")))
								.setResultTransformer(Transformers.aliasToBean(DgResultEntryDt.class));*/
					
					
						
						if( (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) ) {
							Calendar c2 = Calendar.getInstance();
					        c2.setTime(fromDate);
					        c2.add(Calendar.DATE, -15);
					        Date toDate2 = c2.getTime();
					        String date = HMSUtil.convertDateToStringFormat(toDate2, "dd/MM/yyyy");
							Date fromDate3 = HMSUtil.convertStringTypeDateToDateType(date);
							criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate3))
									.add(Restrictions.lt("lastChgDate", toDate1));
						}else {
							criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate1))
							.add(Restrictions.lt("lastChgDate", toDate1));
						}
						
						if (serviceNo != null && StringUtils.isNotBlank(serviceNo)) {
							criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());
						}
						if (patientName != null && StringUtils.isNotEmpty(patientName)) {
							criteria.add(Restrictions.ilike("patient.patientName", patientName));
						}

						if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
							criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
						}

						dgResultEntryHdList = criteria.list();
						count = dgResultEntryHdList.size();
						
						
						criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
						criteria = criteria.setMaxResults(pagingSize);
						dgResultEntryHdList = criteria.list();
						
						map.put("count", count);
						map.put("list", dgResultEntryHdList);
						
					//}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DgResultEntryHd> getResultValidationWaitingList(JSONObject jsonObject) {
		
		Session session=null;
		
		List<DgOrderhd> dgOrderhdsList = null;
		List<DgSampleCollectionHd> dgSampleCollectionHdsList = null;
		List<Visit> visitList = null;
		Long visitId = null;
		Long dgOrderHdId = null;
		Long sampleCollectionHdId = null;
		List<DgResultEntryHd> dgResultEntryHdList = null;
		
		String serviceNo="";
		String patientName="";
		String mobileNo="";
		Long departmentId = null;
		String orderStatus = "";
		
		if(jsonObject.has("serviceNo")) {
			 serviceNo = jsonObject.get("serviceNo").toString();
		}
		if(jsonObject.has("patientName")) {
			patientName = jsonObject.get("patientName").toString();
		}
		if(jsonObject.has("mobileNumber")) {
			 mobileNo = jsonObject.get("mobileNumber").toString();
		}
		
		
		
		Long hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());
			try {
				session = getHibernateUtils.getHibernateUtlis().OpenSession();
				
				//get patientId
				List pidList = new ArrayList();
				List<Patient> patList = session.createCriteria(Patient.class).add(Restrictions.eq("serviceNo", serviceNo)).list();
				for(Patient p:patList) {
					pidList.add(p.getPatientId());
				}
				
				Criteria cr = session.createCriteria(Visit.class)
						.createAlias("patient", "patient")
						.add(Restrictions.eq("patient.serviceNo", serviceNo))
						.add(Restrictions.in("patientId", pidList))
						.add(Restrictions.eq("hospitalId", hospitalId))
						.add(Restrictions.eq("departmentId", new Long(2)))
						.add(Restrictions.eq("visitStatus", "c").ignoreCase());
				visitList = cr.list();
				
				for(Visit v:visitList) {
					visitId = v.getVisitId();
					hospitalId = v.getMmuId();
				}
				
				if(visitId!=null) {
					Criteria criteriaFromDgOrderHd = session.createCriteria(DgOrderhd.class)
							.add(Restrictions.eq("visitId", visitId));
							
					dgOrderhdsList = criteriaFromDgOrderHd.list();
					if(!dgOrderhdsList.isEmpty() && dgOrderhdsList.size()>0) {
						DgOrderhd header = dgOrderhdsList.get(0);
						dgOrderHdId = header.getOrderhdId();
					}
						
				}
				
				if(dgOrderHdId!=null) {
					Criteria crDgSamCollectionHd = session.createCriteria(DgSampleCollectionHd.class)
							.createAlias("patient", "patient")
							.createAlias("dgOrderHd", "dgOrderHd")
							.createAlias("dgOrderHd.visit", "visit")
							.add(Restrictions.eq("visit.visitId", visitId))
							.add(Restrictions.eq("dgOrderHd.orderhdId", dgOrderHdId))
							.add(Restrictions.eq("patient.serviceNo", serviceNo));				
					dgSampleCollectionHdsList = crDgSamCollectionHd.list();
					if(!dgSampleCollectionHdsList.isEmpty() && dgSampleCollectionHdsList.size()>0) {
						DgSampleCollectionHd hd = dgSampleCollectionHdsList.get(0);
						sampleCollectionHdId = hd.getSampleCollectionHdId();
					}
				}
				
				Date dt = new Date();
				Calendar cal = Calendar.getInstance();
				  cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal.setTime(dt);
					cal.add(Calendar.DATE, 1);
					Date from = cal.getTime();
					cal.set(Calendar.HOUR_OF_DAY, 23);
					 cal.add(Calendar.DATE, -15);
					Date to = cal.getTime();
					
					Criteria crr = session.createCriteria(DgSampleCollectionDt.class)
									.add(Restrictions.eq("orderStatus", "E"));
					
					ProjectionList projectionList = Projections.projectionList();
						projectionList.add(Projections.property("orderStatus").as("orderStatus"));
						projectionList.add(Projections.property("sampleCollectionHdId").as("sampleCollectionHdId"));
						projectionList.add(Projections.property("sampleCollectionDtId").as("sampleCollectionDtId"));
					crr.setProjection(projectionList);
					List<Object[]> listObject =  crr.list();
					
					List<Long> dtIds = new ArrayList<Long>();
					List<Long> hdIds = new ArrayList<Long>();
					for(Object[] rows:listObject) {
						orderStatus = rows[0].toString();
						hdIds.add(Long.parseLong(rows[1].toString()));
						dtIds.add(Long.parseLong(rows[2].toString()));
					}
					Criteria criteria = session.createCriteria(DgResultEntryHd.class,"dgResultEntryHd")
								.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
								.createAlias("dgResultEntryHd.patient", "patient")
								//.add(Restrictions.or(Restrictions.eq("resultStatus", "C"),Restrictions.eq("resultStatus", "P")))
								.add(Restrictions.eq("hospitalId", hospitalId))
								.add(Restrictions.in("dgSampleCollectionHd.sampleCollectionHdId", hdIds))								
								.add(Restrictions.between("resultDate", to,from));
						
						if(serviceNo!=null && StringUtils.isNotBlank(serviceNo)) {				
							criteria.add(Restrictions.eq("patient.serviceNo", serviceNo));
							
						}
			dgResultEntryHdList = criteria.list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgResultEntryHdList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultValidationDetails(Long resultEntryHdId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Session session=null;
		List<DgResultEntryHd> dgResultEntryHdList = null;
		List<DgResultEntryDt> dgResultEntryDtInvList = null;
		List<DgResultEntryDt> singleParameterList = null;
		List<DgResultEntryDt> templateParameterList = null;
		session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria criteria = session.createCriteria(DgResultEntryHd.class)
					.add(Restrictions.eq("resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultStatus", "E"));
			dgResultEntryHdList = criteria.list();
			
			Criteria criteria1 = session.createCriteria(DgResultEntryDt.class)
					.createAlias("dgResultEntryHd", "dgResultEntryHd")
					.createAlias("dgMasInvestigation", "dgMasInvestigation")
					.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultDetailStatus", "E"));
			dgResultEntryDtInvList = criteria1.list();
			
			List<Long> investigationList = new ArrayList<Long>();
			for(DgResultEntryDt dt: dgResultEntryDtInvList) {
				investigationList.add(dt.getInvestigationId());
			}
			
			List<DgMasInvestigation> investigationTypeList = new ArrayList<DgMasInvestigation>();
			Criteria cr = session.createCriteria(DgMasInvestigation.class)
					.add(Restrictions.in("investigationId", investigationList))
					.setProjection(Projections.projectionList().add(Projections.property("investigationType").as("investigationType")))
					.setResultTransformer(Transformers.aliasToBean(DgMasInvestigation.class));
			
			investigationTypeList = cr.list();
			
			List<String> investigationTypes = new ArrayList<String>();
			for(DgMasInvestigation dgMasInvestigation: investigationTypeList) {
				investigationTypes.add(dgMasInvestigation.getInvestigationType());
			}
			
			List<DgMasInvestigation> parentInvestigationList = new ArrayList<DgMasInvestigation>();
			for(int i=0;i<investigationTypes.size();i++) {
				if(investigationTypes.get(i).equalsIgnoreCase("m")) {
					String investigationType = investigationTypes.get(i);
					Criteria cr1 = session.createCriteria(DgMasInvestigation.class)
							.add(Restrictions.eq("investigationType", investigationType))
							.add(Restrictions.in("investigationId", investigationList));
					parentInvestigationList = cr1.list();
				}else if(investigationTypes.get(i).equalsIgnoreCase("s")) {
					String investigationType = investigationTypes.get(i);
					Criteria cr2 = session.createCriteria(DgResultEntryDt.class)
							.createAlias("dgResultEntryHd", "dgResultEntryHd")
							.createAlias("dgMasInvestigation", "dgMasInvestigation")
							.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType))
							.add(Restrictions.in("dgMasInvestigation.investigationId", investigationList))
							.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
							.add(Restrictions.eq("resultType", "s"));
					singleParameterList = cr2.list();
				}else {
					Criteria cr3 = session.createCriteria(DgResultEntryDt.class)
							.createAlias("dgResultEntryHd", "dgResultEntryHd")
							.createAlias("dgMasInvestigation", "dgMasInvestigation")
							.add(Restrictions.eq("dgMasInvestigation.investigationType", "t"))
							.add(Restrictions.in("dgMasInvestigation.investigationId", investigationList))
							.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
							.add(Restrictions.eq("resultType", "t"));
					templateParameterList = cr3.list();
				}
			}
			
			map.put("dgResultEntryHdList", dgResultEntryHdList);
			map.put("dgResultEntryDtInvList", dgResultEntryDtInvList);
			map.put("parentInvestigationList", parentInvestigationList);
			map.put("singleParameterList", singleParameterList);
			map.put("templateParameterList", templateParameterList);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}


	///@start 
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInvestigationDtFromDgResultEntryDt(Long resultEntryHdId) {
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Session session=null;
		List<DgResultEntryDt> dgResultEntryDtInvList = null;
		String investigationType = "";
		List<String> investigtionTypeList = new ArrayList<String>();
		List<DgResultEntryDt> dtList = new ArrayList<DgResultEntryDt>();
		List<Object> investigationList = new ArrayList<Object>();
		List<DgSubMasInvestigation> subInvestigationList = null;
		List<Object> subInvList = new ArrayList<Object>();
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		String comparisionType = "";
		List<DgSampleCollectionDt> invTypeTempList = null;
		List resultEntrySingleParameterList = null;
		List<DgResultEntryDt> resultEntryTemplateParameterList = null;
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgResultEntryDt.class)
					.createAlias("dgResultEntryHd", "dgResultEntryHd")
					.createAlias("dgMasInvestigation", "dgMasInvestigation")
					.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultDetailStatus", "P"));
			dgResultEntryDtInvList = criteria.list();
			
		
			mapdata.put("dgResultEntryDtInvList", dgResultEntryDtInvList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapdata;
	}
	
	///@end
	
	
	
	
	
/*	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getInvestigationDtFromDgResultEntryDt(Long resultEntryHdId) {
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Session session=null;
		List<DgResultEntryDt> dgResultEntryDtInvList = null;
		String investigationType = "";
		List<String> investigtionTypeList = new ArrayList<String>();
		List<DgResultEntryDt> dtList = new ArrayList<DgResultEntryDt>();
		List<Object> investigationList = new ArrayList<Object>();
		List<DgSubMasInvestigation> subInvestigationList = null;
		List<Object> subInvList = new ArrayList<Object>();
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		String comparisionType = "";
		List<DgSampleCollectionDt> invTypeTempList = null;
		List resultEntrySingleParameterList = null;
		List<DgResultEntryDt> resultEntryTemplateParameterList = null;
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgResultEntryDt.class)
					.createAlias("dgResultEntryHd", "dgResultEntryHd")
					.createAlias("dgMasInvestigation", "dgMasInvestigation")
					.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultDetailStatus", "P"));
			dgResultEntryDtInvList = criteria.list();
			
			//getInvesi
			
			for(DgResultEntryDt dt:dgResultEntryDtInvList) {
				investigationType = dt.getDgMasInvestigation().getInvestigationType();
				investigtionTypeList.add(investigationType);
			}
			
		for(int i=0; i<investigtionTypeList.size();i++) {
			if(investigtionTypeList.get(i).equalsIgnoreCase("s")) {
				
				Criteria cr = session.createCriteria(DgResultEntryDt.class)
						.createAlias("dgResultEntryHd", "dgResultEntryHd")
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
						.add(Restrictions.eq("resultDetailStatus", "P"))
						.add(Restrictions.eq("resultType", "s").ignoreCase());
				
				resultEntrySingleParameterList = cr.list();
				
			}else if(investigtionTypeList.get(i).equalsIgnoreCase("m")){

				Criteria cr = session.createCriteria(DgResultEntryDt.class)
						.createAlias("dgResultEntryHd", "dgResultEntryHd")
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
						.add(Restrictions.eq("resultDetailStatus", "P"))
						.add(Restrictions.eq("resultType", "m").ignoreCase());
				
				dtList = cr.list();
				Long genderId = dtList.get(0).getDgResultEntryHd().getDgSampleCollectionHd().getPatient().getAdministrativeSexId();
				// get gender Code
				
				List<MasAdministrativeSex> listOfGender = session.createCriteria(MasAdministrativeSex.class)
												.add(Restrictions.eq("administrativeSexId", genderId)).list();
				String genderCode = listOfGender.get(0).getAdministrativeSexCode();
				for (DgResultEntryDt dt : dtList) {
					investigationList.add(dt.getInvestigationId());

				}
				
				// get the subInvestigationId from dgSubMasInvestigation
				Criteria cr1 = session.createCriteria(DgSubMasInvestigation.class)
						.add(Restrictions.in("investigationId", investigationList))
						.add(Restrictions.eq("status", "Y").ignoreCase());
				subInvestigationList = cr1.list();

				for (DgSubMasInvestigation subInv : subInvestigationList) {
					subInvList.add(subInv.getSubInvestigationId());
					comparisionType = subInv.getComparisonType();

				}

				// DG_FIXED_VALUE
				if (comparisionType.equalsIgnoreCase("f")) {
					Criteria cr2 = session.createCriteria(DgFixedValue.class)
							.createAlias("dgSubMasInvestigation", "dgSubMasInvestigation")
							.add(Restrictions.in("subInvestigationId", subInvList))
							.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType));

					fixedValueList = cr2.list();
					mapFixedValue = new HashMap<String, Object>();
					for (int ii = 0; ii < fixedValueList.size(); i++) {
						mapFixedValue.put(fixedValueList.get(ii).getFixedValue().trim(),
								fixedValueList.get(ii).getFixedId() + "@@"
										+ fixedValueList.get(i).getFixedValue().trim());

					}
				}

				// DG_NORMAL_VALUE
				if (comparisionType.equalsIgnoreCase("v")) {
					Criteria crNorVal = session.createCriteria(DgNormalValue.class).createAlias("dgSubMasInvestigation",
							"dgSubMasInvestigation");

					if (genderCode.equalsIgnoreCase(genderCode)) {
						crNorVal.add(Restrictions.in("dgSubMasInvestigation.subInvestigationId", subInvList))
								.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType))
								.add(Restrictions.eq("sex", genderCode).ignoreCase());
					}
					normalValList = crNorVal.list();
				}
				
			
			}else {
				Criteria cr = session.createCriteria(DgResultEntryDt.class)
						.createAlias("dgResultEntryHd", "dgResultEntryHd")
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
						.add(Restrictions.eq("resultDetailStatus", "P"))
						.add(Restrictions.eq("resultType", "t").ignoreCase());
				
				resultEntryTemplateParameterList = cr.list();
			}
		}
		
			mapdata.put("resultEntrySingleParameterList", resultEntrySingleParameterList);
			mapdata.put("resultEntryMultiParameterList", dtList);
			mapdata.put("resultEntryTemplateParameterList", resultEntryTemplateParameterList);			
			mapdata.put("dgResultEntryDtInvList", dgResultEntryDtInvList);
			mapdata.put("subInvestigationList", subInvestigationList);
			mapdata.put("fixedValueList", fixedValueList);
			mapdata.put("normalValList", normalValList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapdata;
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DgSubMasInvestigation> getSubInvestigationDetails(List listSubInvId) {
		Session session = null;
		List<DgSubMasInvestigation> dgSubMasInvestigations = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dgSubMasInvestigations = session.createCriteria(DgSubMasInvestigation.class)
						.add(Restrictions.in("subInvestigationId", listSubInvId)).list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSubMasInvestigations;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<DgResultEntryDt> getAllResultEntDtId(List<Long> subInvIds,List<Long> InvIds, Long hdId) {
		Session session = null;
		List<DgResultEntryDt> dtList = null;
		Criterion cr1 = null;
		Criterion cr2 = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(DgResultEntryDt.class);
			if(subInvIds==null) {
				cr1 = Restrictions.in("investigationId", InvIds);
				cr.add(cr1);
			}
			else {
				cr1 = Restrictions.in("subInvestigationId", subInvIds);
				Criterion cr3 = Restrictions.in("investigationId", subInvIds);
				LogicalExpression lg=Restrictions.or(cr1, cr3);
						cr.add(lg);
			}
				
				cr2 = Restrictions.eq("resultEntryId", hdId);
				cr.add(cr2);
				//LogicalExpression andExp = Restrictions.and(cr1, cr2);
				//cr.add(andExp);
				dtList = cr.list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dtList;
	}*/

	
	@SuppressWarnings("unchecked")
	@Override
	public List<DgResultEntryDt> getAllResultEntDtId(Long resultEntryHdId, String[] parentInvestigationIdArray) {
		Session session = null;
		List<DgResultEntryDt> dtList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			Long[] data = new Long[parentInvestigationIdArray.length];
			for (int i = 0; i < parentInvestigationIdArray.length; i++) {
			  data[i] = Long.valueOf(parentInvestigationIdArray[i]);
			}
			
			Criteria cr = session.createCriteria(DgResultEntryDt.class)
					.add(Restrictions.eq("resultEntryId", resultEntryHdId))
					.add(Restrictions.in("investigationId", data));
				dtList = cr.list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dtList;
	}
	
	@Override
	public boolean submitResultValidationDetails(String[] resultDtIds ,String[] remarks, String[] resultValues, String[] rangeStatus, String[] normalRangeValues, 
			String[] subInvestigationIdArray, String[] parentInvIdsArrayValues) {
		boolean flag = false;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			int count=0;
			Long updated=null;
			
			// Map<Long, Object> resultRangeMap  = validateResultEnteredRange(subInvestigationIdArray, resultValues, normalRangeValues);
			//Map<Long, Object> resultRangeMap  = validateResultEnteredRangeForUpdate(subInvestigationIdArray,  resultValues,  normalRangeValues, parentInvIdsArrayValues);
			 
			for(String l:resultDtIds) {
				DgResultEntryDt dgResultEntryDt =dgResultEntryDtDao.find(Long.parseLong(l));
				if(remarks.length>0 && count<remarks.length) {
					if(remarks[count]!=null && !remarks[count].toString().equalsIgnoreCase("")) {
						dgResultEntryDt.setRemarks(remarks[count]);
					}else {
						dgResultEntryDt.setRemarks("");
					}
				}else {
					dgResultEntryDt.setRemarks("");
				}
				
				 
				if(resultValues.length>0 && count<resultValues.length) {
					if(resultValues[count]!=null && !resultValues[count].toString().equalsIgnoreCase("")) {
						dgResultEntryDt.setResult(resultValues[count]);
					}else {
						dgResultEntryDt.setResult("");
					}
					
				}
				
				if(rangeStatus.length>0 && count<rangeStatus.length) {
					if(rangeStatus[count]!=null && !rangeStatus[count].toString().equalsIgnoreCase("")) {
						dgResultEntryDt.setRangeStatus(rangeStatus[count]);
					}else {
						dgResultEntryDt.setRangeStatus("");
					}
					
				}
				
				dgResultEntryDt.setValidated("V"); 
				dgResultEntryDt.setResultDetailStatus("C");
				dgResultEntryDtDao.saveOrUpdate(dgResultEntryDt);
				updated=dgResultEntryDt.getResultEntryId();
				count++;
				
			}
			
			if(count!=0) {
				flag = true;
			}else {
				return flag;
			}
			session.flush();
			session.clear();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DgResultEntryDt> getResultEntDtIds(Long resultEntHdId) {
		Session session = null;
		List<DgResultEntryDt> dtList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			dtList = session.createCriteria(DgResultEntryDt.class)
						.add(Restrictions.eq("resultEntryId", resultEntHdId))
						//.add(Restrictions.eq("validated", "V"))
						.list();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dtList;
	}



	@Override
	public boolean updateResultEntryHd(Long resultEntHdId, Long userId) {
		Session session = null;
		boolean flag = false;
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			String hqlQuery = " update dg_result_entry_hd set verified =:verified , result_status=:result_status, RESULT_VERIFIED_BY=:RESULT_VERIFIED_BY where result_entry_hd_id in (:resultEntHdId)";
			Query query = session.createSQLQuery(hqlQuery.toString());
			query.setParameter("resultEntHdId", resultEntHdId);
			query.setParameter("verified", "V");
			query.setParameter("result_status", "C");
			query.setParameter("RESULT_VERIFIED_BY", userId);
			Integer updated = query.executeUpdate();
			if(updated!=0) {
				flag = true;
			}else {
				return flag;
			}
			session.flush();
			session.clear();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	}


	@SuppressWarnings("unchecked")
	@Override
	public DgSampleCollectionDt getDgSampleCollectionDt(long sampleCollectionHeaderId) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		List<DgSampleCollectionDt> dgSampleCollectionDt = (List<DgSampleCollectionDt>) session.createCriteria(DgSampleCollectionDt.class)
				
				.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHeaderId)).list();
		
		return dgSampleCollectionDt.get(0);
		
	}

	/**************************** Lab History *******************************/
	@SuppressWarnings("unchecked")
	@Override
	public List<Patient> getPatientList(JSONObject jsonObject) {
		
		Session session = null;
		Criteria criteria = null;
		List<Patient> pList = null;
		String serviceNo="";
		
		Criterion cr1 = null;
		Criterion cr2 = null;
		if(jsonObject.has("serviceNo")) {
			if(!jsonObject.isNull("serviceNo"))
				serviceNo = jsonObject.get("serviceNo").toString();
		}
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			criteria = session.createCriteria(Patient.class);			
					cr1 = Restrictions.eq("serviceNo", serviceNo).ignoreCase();
					cr2 = Restrictions.eq("uhidNo", serviceNo);
					LogicalExpression orExp = Restrictions.or(cr1, cr2);
					pList = criteria.add(orExp).list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return pList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getLabHistory(JSONObject jsonObject) {
		Session session = null;
		Criteria criteria = null;
		String serviceNo = "";
		Long patientId = null;
		String investigationName = "";
		
		Criterion cr1 = null;
		Criterion cr2 = null;
		List<DgResultEntryDt> dtsList = null; 
		Map<String, Object> map = new HashMap<String, Object>();
		Date toDate1=null;
		
		Date fromDate =null;
		Date toDate =null;
		
		Long hospitalId = null;
		Long investigationId = null;
		
		if(jsonObject.has("hospitalId")) {
			if(!jsonObject.get("hospitalId").equals("") && jsonObject.get("hospitalId")!=null) {
				hospitalId = Long.parseLong(jsonObject.get("hospitalId").toString());
				
			}
		}
		
		if(jsonObject.has("fromDate")) {
			if(!jsonObject.get("fromDate").equals("") && jsonObject.get("fromDate")!=null) {
				 fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonObject.get("fromDate")));	
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
			}
		}
		
		if(jsonObject.has("toDate")) {
			if(!jsonObject.get("toDate").equals("") && jsonObject.get("toDate")!=null) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonObject.get("toDate")));
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate1 = c.getTime();
			}
		}
		
		if(jsonObject.has("serviceNo")) {
			if(!jsonObject.isNull("serviceNo"))
				serviceNo = jsonObject.get("serviceNo").toString();
		}
		
		if(jsonObject.has("patientId")) {
			if(!jsonObject.get("patientId").toString().equalsIgnoreCase("0") && StringUtils.isNotBlank(jsonObject.get("patientId").toString()))
				patientId = Long.parseLong(jsonObject.get("patientId").toString());
		}
		
		if(jsonObject.has("investigationName") && jsonObject.get("investigationName")!="") {
			if(!jsonObject.isNull("investigationName"))
				investigationName = jsonObject.get("investigationName").toString();
			int index1 = investigationName.lastIndexOf("[");
			int index2 = investigationName.lastIndexOf("]");
			index1++;
			investigationId = Long.parseLong(investigationName.substring(index1,index2));
			System.out.println("investigationId :: "+investigationId);
		}
		
		
		try {
			int pageNo = Integer.parseInt(String.valueOf(jsonObject.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			Long investigationIdd=null;
			
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			/*if(investigationName!="" && StringUtils.isNotBlank(investigationName)) {
				Criteria crr = session.createCriteria(DgMasInvestigation.class)
						.add(Restrictions.ilike("investigationName", investigationName));
				List<DgMasInvestigation> investigationList = crr.list();
				if(investigationList!=null && investigationList.size()>0)
					investigationIdd = investigationList.get(0).getInvestigationId();
			}*/
			//authenticate service No.
			boolean flag = authenticateServiceNumber(serviceNo, session);
			if(flag==true) {
				/*Criteria cr = session.createCriteria(Patient.class)
						.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase())
						.setProjection(Projections.projectionList().add(Projections.property("unitId").as("unitId")))
						.setResultTransformer(Transformers.aliasToBean(Patient.class));
				
				List<Patient> pList = cr.list();*/
				Criteria cr = session.createCriteria(MasEmployee.class)
						.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase())
						.setProjection(Projections.projectionList().add(Projections.property("icgUnitId").as("icgUnitId")))
						.setResultTransformer(Transformers.aliasToBean(MasEmployee.class));
				List<MasEmployee> pList = cr.list();
				
				if(pList!=null && pList.size()>0) {
					//Long unitIdFromPatient = pList.get(0).getUnitId();
					Long unitIdFromPatient = (long) 0;
					Criteria cri1 = session.createCriteria(MasHospital.class).add(Restrictions.eq("hospitalId", hospitalId));
					List<MasHospital> hospitalsList =  cri1.list();
					Long unitIdFromMasUnit =  hospitalsList.get(0).getMasUnit().getUnitId();
					
					StringBuilder queryBuilder = new StringBuilder();
					queryBuilder.append(" SELECT COUNT(*) as counter, A.MI_UNIT FROM VU_MAS_MIUNIT A " + 
										" WHERE A.MI_UNIT in (SELECT MI_UNIT FROM VU_MAS_MIUNIT WHERE UNIT_ID=:unitIdFromPatient) AND " + 
										" A.UNIT_ID=:unitIdFromMasUnit ");
					
					queryBuilder.append(" group by A.MI_UNIT ");
					
					Query query2 = session.createSQLQuery(queryBuilder.toString());
					query2.setParameter("unitIdFromPatient", unitIdFromPatient);
					query2.setParameter("unitIdFromMasUnit", unitIdFromMasUnit);
					
				    List<Object[]> objectList = query2.list();
				    int counter = objectList.size();
					
					if(counter>0) {
						criteria = session.createCriteria(DgResultEntryDt.class)
								.createAlias("dgResultEntryHd", "dgResultEntryHd")
								.createAlias("dgMasInvestigation", "dgMasInvestigation", JoinType.LEFT_OUTER_JOIN)
								.createAlias("dgResultEntryHd.patient", "patient")
								//.createAlias("masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN)
								.add(Restrictions.isNotNull("result"))
								.add(Restrictions.eq("dgResultEntryHd.mainChargecodeId", 2l));
													
								cr1 = Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase();
								cr2 = Restrictions.eq("patient.uhidNo", serviceNo).ignoreCase();
								LogicalExpression orExp = Restrictions.or(cr1, cr2);
								
								criteria.addOrder(Order.desc("lastChgDate"));
								criteria.add(orExp);
								
								if(patientId!=null) {
									criteria.add(Restrictions.eq("patient.patientId", patientId))
										.addOrder(Order.desc("patient.patientId"))
										.addOrder(Order.desc("lastChgDate"));
								}
								
								if(fromDate!=null && toDate1!=null) {
									criteria.add(Restrictions.between("lastChgDate", fromDate, toDate1))
										.addOrder(Order.desc("lastChgDate"));
								}
								
								if(investigationId!=null) {
									criteria.add(Restrictions.eq("dgMasInvestigation.investigationId", investigationId))
									.addOrder(Order.desc("investigationId"));
								}
								
								dtsList = criteria.list();
								count = dtsList.size();
								
								criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
								criteria = criteria.setMaxResults(pagingSize);
								dtsList = criteria.list();
								
								map.put("dtsList", dtsList);
								map.put("count", count);
								map.put("status", "success");
						
					}else {
						map.put("dtsList", dtsList);
						map.put("count", count);
						map.put("status", "failure");
						}
				}else {
					map.put("dtsList", dtsList);
					map.put("count", count);
					map.put("status", "failure");
				}
				
				
			}else {
				map.put("msg", "Service No does not exist");
				map.put("status", "errorInServiceNo");
			}
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private boolean authenticateServiceNumber(String serviceNo, Session session) {
		boolean flag = false;
		try {
			Criteria crr = session.createCriteria(Patient.class)
					.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase());
			List<Patient> patientList = crr.list();
			if(patientList!=null && patientList.size()>0) {
				flag = true;
			}else {
				return flag;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
		
	}


	/*********************************************** OutSide Patient Waiting List ********************************************/
	@Override
	public Map<String, Object> getOutSidePatientWaitingList(JSONObject jsonObject) {
		Long hospitalId = null;
		String serviceNo = "", patientName="", mobileNo = "";
		if(jsonObject.has("hospitalId")) {
			hospitalId = Long.parseLong(String.valueOf(jsonObject.get("hospitalId")));
		}
		
		 
		Map<String, Object> mapobj = new LinkedHashMap<String, Object>();		
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			int pageNo = Integer.parseInt(String.valueOf(jsonObject.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
			Date fromDate = new Date();
			
			// convert date to calendar current date
	        Calendar c = Calendar.getInstance();
	        c.setTime(fromDate);
	        c.add(Calendar.DATE, 1);
	        Date toDate = c.getTime();
	        
			String fromDate1 = HMSUtil.convertDateToStringFormat(fromDate, "dd-MMM-yy");
			String toDate1 = HMSUtil.convertDateToStringFormat(toDate, "dd-MMM-yy");
			
			
			
			
			
			if (jsonObject.get("serviceNo") != null && StringUtils.isNotBlank(String.valueOf(jsonObject.get("serviceNo"))) && jsonObject.has("serviceNo")) {
				serviceNo = jsonObject.get("serviceNo").toString();
			}
			if (jsonObject.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonObject.get("patientName")))  && jsonObject.has("patientName")) {
				patientName = "%" + jsonObject.get("patientName").toString() + "%";
			}

			if (jsonObject.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonObject.get("mobileNumber"))) && jsonObject.has("mobileNumber")) {
				mobileNo = "%" + jsonObject.get("mobileNumber").toString() + "%";
			}
			
			String mainChargeCode=HMSUtil.getProperties("js_messages_en.properties", "mainchargeCodeLab");
			MasMainChargecode mmcc=null;
			mmcc =opdMasterDao. getMainChargeCode(mainChargeCode);
			Long	MAIN_CHARGECODE_ID=mmcc.getMainChargecodeId();
				
		String LAB_MARK = "O";
		StringBuilder sampleForOutSideWaitingListQury = new StringBuilder();
			
		sampleForOutSideWaitingListQury .append( "select patient.PATIENT_ID, patient.SERVICE_NO, patient.PATIENT_NAME, patient.RELATION_ID, dgorderdt.LAB_MARK, " ); 
		sampleForOutSideWaitingListQury .append(" dgorderhd.ORDERHD_ID,dgorderhd.ORDER_date, patient.EMPLOYEE_NAME, masdepartment.DEPARTMENT_NAME," ); 
		sampleForOutSideWaitingListQury .append(" patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH,administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO, masrelation.relation_Name " );
		sampleForOutSideWaitingListQury .append(" from DG_ORDER_HD dgorderhd " );
		sampleForOutSideWaitingListQury .append(" LEFT OUTER join DG_ORDER_DT dgorderdt on dgorderhd.ORDERHD_ID = dgorderdt.ORDERHD_ID "); 
		sampleForOutSideWaitingListQury .append(" LEFT OUTER join PATIENT patient on patient.PATIENT_ID=dgorderhd.PATIENT_ID " ); 
		sampleForOutSideWaitingListQury .append(" LEFT OUTER join VU_MAS_RELATION masrelation on masrelation.RELATION_ID=patient.RELATION_ID "); 
		sampleForOutSideWaitingListQury .append(" LEFT OUTER join MAS_ADMINISTRATIVE_SEX administrativeSex on administrativeSex.ADMINISTRATIVE_SEX_ID=patient.ADMINISTRATIVE_SEX_ID ");
		sampleForOutSideWaitingListQury .append(" LEFT OUTER join MAS_DEPARTMENT masdepartment on dgorderhd.department_id = masdepartment.DEPARTMENT_ID ");
		sampleForOutSideWaitingListQury .append(" left outer join DG_MAS_INVESTIGATION masInvestigation on dgorderdt.INVESTIGATION_ID = masInvestigation.INVESTIGATION_ID ");
		sampleForOutSideWaitingListQury .append(" where dgorderhd.ORDER_STATUS =:ORDER_STATUS" );			 
		sampleForOutSideWaitingListQury .append(" and dgorderhd.HOSPITAL_ID= :hospitalId" );
		sampleForOutSideWaitingListQury.append(" and masInvestigation.MAIN_CHARGECODE_ID=:MAIN_CHARGECODE_ID ");
		sampleForOutSideWaitingListQury .append(" and dgorderdt.LAB_MARK=:LAB_MARK" ); 
			
		
			
			Date fromDate3 = null;
			if( (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) ) {
				Calendar c2 = Calendar.getInstance();
		        c2.setTime(fromDate);
		        c2.add(Calendar.DATE, -15);
		        Date toDate2 = c2.getTime();
		        String date = HMSUtil.convertDateToStringFormat(toDate2, "dd/MM/yyyy");
				fromDate3 = HMSUtil.convertStringTypeDateToDateType(date);
				sampleForOutSideWaitingListQury .append(" and dgorderhd.LAST_CHG_DATE > :fromDate and dgorderhd.LAST_CHG_DATE < :toDate" );
				
			}else {
				sampleForOutSideWaitingListQury .append(" and dgorderhd.LAST_CHG_DATE > :fromDate and dgorderhd.LAST_CHG_DATE < :toDate" );
			}
			
			String serNo ="";
			if (jsonObject.has("serviceNo")) {
				serviceNo = jsonObject.get("serviceNo").toString();
				//serNo = "%"+serviceNo+"%";
				if (jsonObject.get("serviceNo").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("serviceNo").toString().trim())) {
					
					sampleForOutSideWaitingListQury.append(" and upper(patient.service_no) = upper(:serviceNo) ");													
					
				}
			}
				
			if (jsonObject.has("patientName")) {
				patientName = jsonObject.get("patientName").toString();
				if (jsonObject.get("patientName").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("patientName").toString().trim())) {
					
					sampleForOutSideWaitingListQury .append(" and upper(patient.patient_name) like upper(:patient_name) ");													
					
				}
			}
			
			
			if (jsonObject.has("mobileNumber")) {
				mobileNo = jsonObject.get("mobileNumber").toString();
				if (jsonObject.get("mobileNumber").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("mobileNumber").toString().trim())) {
															
					sampleForOutSideWaitingListQury .append(" and patient.MOBILE_NUMBER =:MOBILE_NUMBER ");													
					
				}
			}
			
			sampleForOutSideWaitingListQury .append(" group by patient.PATIENT_ID, patient.SERVICE_NO, patient.PATIENT_NAME, patient.RELATION_ID, dgorderdt.LAB_MARK, "); 
			sampleForOutSideWaitingListQury .append(" dgorderhd.ORDERHD_ID ,dgorderhd.ORDER_date, patient.EMPLOYEE_NAME," ); 
			sampleForOutSideWaitingListQury .append(" masdepartment.DEPARTMENT_NAME,patient.MOBILE_NUMBER,patient.DATE_OF_BIRTH, administrativeSex.ADMINISTRATIVE_SEX_NAME,dgorderhd.ORDER_NO, masrelation.relation_Name ");
		
			
			Query query = session.createSQLQuery(sampleForOutSideWaitingListQury.toString());
			query.setParameter("ORDER_STATUS", "P");
			query.setParameter("hospitalId", hospitalId);
			query.setParameter("MAIN_CHARGECODE_ID", MAIN_CHARGECODE_ID);
			
			if(fromDate3 == null) {
				query.setParameter("fromDate", fromDate1);
			}else {
				query.setParameter("fromDate", fromDate3);
			}			
			query.setParameter("toDate", toDate1);
			query.setParameter("LAB_MARK", LAB_MARK);
			
			if (jsonObject.has("serviceNo")) {
				if (jsonObject.get("serviceNo").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("serviceNo").toString().trim())) {
					query.setParameter("serviceNo", serviceNo);
				}
			}
			if (jsonObject.has("patientName")) {
				if (jsonObject.get("patientName").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("patientName").toString().trim())) {
					query.setParameter("patient_name", "%"+patientName+"%");
				}
				
			}
			if (jsonObject.has("mobileNumber")) {
				if (jsonObject.get("mobileNumber").toString().length() > 0 && StringUtils.isNotBlank(jsonObject.get("mobileNumber").toString().trim())) {
					query.setParameter("MOBILE_NUMBER", "%"+mobileNo+"%");
				}
				
			}
			
			List<Object[]> dgOrderhdsList = query.list();
			count = dgOrderhdsList.size();
			
			query = query.setFirstResult(pagingSize * (pageNo - 1));
			query = query.setMaxResults(pagingSize);
			dgOrderhdsList = query.list();
			mapobj.put("dgOrderhdsList", dgOrderhdsList);
			mapobj.put("count", count);
		
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapobj;
	}
	/**
	 * @Descritption: get the out side patient Details
	 */

	@Override
	public List<DgOrderhd> getOutSidePatientDetails(JSONObject jsonObject) {
		Session session = null;
		Long orderhdId = null;
		List<DgOrderhd> hdList = null;
		try {
			
			if(jsonObject.has("orderHdId")) {
				orderhdId = Long.parseLong(String.valueOf(jsonObject.get("orderhdId")));
			}
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria c =  session.createCriteria(DgOrderhd.class)
					.createAlias("patient", "patient", JoinType.LEFT_OUTER_JOIN)
					.createAlias("masHospital", "masHospital", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("orderhdId", orderhdId));
			
			hdList = c.list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return hdList;
	}
	
	@Override
	public Map<String, Object> getOutSidePatientInvestigationDetails(JSONObject jsonObject) {
		Session session = null;
		Long orderhdId = null;
		
		if(jsonObject.has("orderHdId")){
		orderhdId = Long.parseLong(String.valueOf(jsonObject.get("orderHdId")));
		}
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}

	/**
	 * @Description: Result Entry Update.
	 */

	@Override
	public Map<String,Object> getResultUpdateWaitingList(JSONObject jsonData) {
		Map<String,Object> map = new HashMap<>();
		Session session=null;
		
		//List<DgResultEntryDt> dgResultEntryDtList = null;
		List<DgResultEntryHd> dgResultEntryHdList = null;
		Long hospitalId = null;	
		 String patientName = "";
		 String serviceNo = "";
		 String mobileNo = "";
		 
		 if (jsonData.has("hospitalId")) {
				hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
			}

			
			if (jsonData.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("patientName")))  && jsonData.has("patientName")) {
				patientName = "%" + jsonData.get("patientName").toString() + "%";
			}

			if (jsonData.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("mobileNumber"))) && jsonData.has("mobileNumber")) {
				mobileNo = "%" + jsonData.get("mobileNumber").toString() + "%";
			}
			
		
			try {
				session = getHibernateUtils.getHibernateUtlis().OpenSession();					
					int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
					int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
					int count=0;
				
					Date fromDate = new Date();
					String d1 = HMSUtil.convertDateToStringFormat(fromDate, "dd/MM/yyyy");
					Date fromDate1 = HMSUtil.convertStringTypeDateToDateType(d1);
					
					Calendar c = Calendar.getInstance();
			        c.setTime(fromDate);
			        c.add(Calendar.DATE, 1);
			        Date toDate = c.getTime();
			        String d2 = HMSUtil.convertDateToStringFormat(toDate, "dd/MM/yyyy");
					Date toDate1 = HMSUtil.convertStringTypeDateToDateType(d2);
					System.out.println("hospitalId="+hospitalId);
					Criteria criteria = session.createCriteria(DgResultEntryHd.class,"dgResultEntryHd")
								.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
								.createAlias("dgResultEntryHd.patient", "patient")
								.add(Restrictions.eq("mmuId", hospitalId))
								.add(Restrictions.eq("resultStatus", "C").ignoreCase())
								.add(Restrictions.eq("verified","V").ignoreCase())
								.add(Restrictions.eq("mainChargecodeId", new Long(2)));
																
						
			/*
			 * if( (patientName != null && !patientName.equals("") &&
			 * !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") &&
			 * !mobileNo.equals("null")) ) { Calendar c2 = Calendar.getInstance();
			 * c2.setTime(fromDate); c2.add(Calendar.DATE, -15); Date toDate2 =
			 * c2.getTime(); String date = HMSUtil.convertDateToStringFormat(toDate2,
			 * "dd/MM/yyyy"); Date fromDate3 =
			 * HMSUtil.convertStringTypeDateToDateType(date); criteria =
			 * criteria.add(Restrictions.ge("lastChgDate", fromDate3))
			 * .add(Restrictions.lt("lastChgDate", toDate1)); }else { criteria =
			 * criteria.add(Restrictions.ge("lastChgDate", fromDate1))
			 * .add(Restrictions.lt("lastChgDate", toDate1)); }
			 */
						
						if (patientName != null && StringUtils.isNotEmpty(patientName)) {
							criteria.add(Restrictions.ilike("patient.patientName", patientName));
						}

						if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
							criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
						}

						dgResultEntryHdList = criteria.list();
						count = dgResultEntryHdList.size();
						
						
						criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
						criteria = criteria.setMaxResults(pagingSize);
						dgResultEntryHdList = criteria.list();
						
						map.put("count", count);
						map.put("list", dgResultEntryHdList);
						
					//}	
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultEntryUpdateDetails(Long resultEntryHdId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Session session=null;
		List<DgResultEntryHd> dgResultEntryHdList = null;
		List<DgResultEntryDt> dgResultEntryDtInvList = null;
		List<DgResultEntryDt> dgResultEntryDtInvList1 = null;
		List<DgResultEntryDt> singleParameterList = null;
		List<DgResultEntryDt> templateParameterList = null;
		List<DgMasInvestigation> parentInvestigationList = new ArrayList<DgMasInvestigation>();
		List<DgSubMasInvestigation> subInvestList = new ArrayList<DgSubMasInvestigation>();
		List subInvList = new ArrayList();
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		List<String> investigationTypes = new ArrayList<String>();
		String comparisionType = "";
		
		session = getHibernateUtils.getHibernateUtlis().OpenSession();
		try {
			Criteria criteria = session.createCriteria(DgResultEntryHd.class)
					.add(Restrictions.eq("resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultStatus", "C"))
					.add(Restrictions.eq("verified", "V"));
			dgResultEntryHdList = criteria.list();
			
			Criteria criteria1 = session.createCriteria(DgResultEntryDt.class)
					.createAlias("dgResultEntryHd", "dgResultEntryHd")
					.createAlias("dgMasInvestigation", "dgMasInvestigation")
					.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
					.add(Restrictions.eq("resultDetailStatus", "C"))
					.add(Restrictions.eq("validated", "V"));
			dgResultEntryDtInvList1 = criteria1.list();
			
			if(dgResultEntryDtInvList1!=null && dgResultEntryDtInvList1.size()>0) {
				
				List<Long> investigationList = new ArrayList<Long>();
				for(DgResultEntryDt dt: dgResultEntryDtInvList1) {
					investigationList.add(dt.getInvestigationId());
				}
				
				
				List<DgMasInvestigation> investigationTypeList = new ArrayList<DgMasInvestigation>();
				Criteria cr = session.createCriteria(DgMasInvestigation.class)
						.add(Restrictions.in("investigationId", investigationList))
						.setProjection(Projections.projectionList().add(Projections.property("investigationType").as("investigationType")))
						.setResultTransformer(Transformers.aliasToBean(DgMasInvestigation.class));
				
				investigationTypeList = cr.list();
				System.out.println("investigationTypeList :: "+investigationTypeList);
				
				
				for(DgMasInvestigation dgMasInvestigation: investigationTypeList) {
					investigationTypes.add(dgMasInvestigation.getInvestigationType());
				}
				
				
				for(int i=0;i<investigationTypes.size();i++) {
					if(investigationTypes.get(i).equalsIgnoreCase("m")) {
						String investigationType = investigationTypes.get(i);
						Criteria cr3 = session.createCriteria(DgMasInvestigation.class)
								.add(Restrictions.eq("investigationType", investigationType))
								.add(Restrictions.in("investigationId", investigationList));
						parentInvestigationList = cr3.list();
						
						Criteria criteria11 = session.createCriteria(DgResultEntryDt.class)
								.createAlias("dgResultEntryHd", "dgResultEntryHd")
								.createAlias("dgMasInvestigation", "dgMasInvestigation")
								.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
								.add(Restrictions.eq("resultDetailStatus", "C"))
								.add(Restrictions.eq("validated", "V"));
						dgResultEntryDtInvList = criteria11.list();
						
						if(dgResultEntryDtInvList!=null && dgResultEntryDtInvList.size()>0) {
							
							Long genderId = dgResultEntryDtInvList.get(0).getDgResultEntryHd().getPatient().getAdministrativeSexId();
							// get gender Code
							System.out.println("genderId :: "+genderId);
							List<MasAdministrativeSex> listOfGender = session.createCriteria(MasAdministrativeSex.class)
									.add(Restrictions.eq("administrativeSexId", genderId)).list();
							String genderCode = listOfGender.get(0).getAdministrativeSexCode();
							System.out.println("genderCode :: "+genderCode);
							
							/*List<Long> investigationList = new ArrayList<Long>();
							for(DgResultEntryDt dt: dgResultEntryDtInvList) {
								investigationList.add(dt.getInvestigationId());
							}*/
							
							// get the subInvestigationId from dgSubMasInvestigation
							Criteria cr1 = session.createCriteria(DgSubMasInvestigation.class)
									.createAlias("masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN)
									.add(Restrictions.in("investigationId", investigationList))
									.add(Restrictions.eq("status", "Y").ignoreCase());
							subInvestList = cr1.list();
							
							for (DgSubMasInvestigation subInv : subInvestList) {
			                    subInvList.add(subInv.getSubInvestigationId());
								comparisionType = subInv.getComparisonType();

							}
							
							// DG_FIXED_VALUE
							if (comparisionType.equalsIgnoreCase("f")) {
								Criteria cr2 = session.createCriteria(DgFixedValue.class)
										.createAlias("dgSubMasInvestigation", "dgSubMasInvestigation")
										.add(Restrictions.in("subInvestigationId", subInvList))
										.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType));
								fixedValueList = cr2.list();
								
								mapFixedValue = new HashMap<String, Object>();
								for (int ii = 0; ii < fixedValueList.size(); ii++) {
									mapFixedValue.put(fixedValueList.get(ii).getFixedValue().trim(),
											fixedValueList.get(ii).getFixedId() + "@@"
													+ fixedValueList.get(ii).getFixedValue().trim());

								}
								
							}
							
							// DG_NORMAL_VALUE
							if (comparisionType.equalsIgnoreCase("v")) {
								Criteria crNorVal = session.createCriteria(DgNormalValue.class)
										.createAlias("dgSubMasInvestigation","dgSubMasInvestigation")
										.add(Restrictions.eq("sex", genderCode).ignoreCase());

								if (genderCode.equalsIgnoreCase(genderCode)) {
									crNorVal.add(Restrictions.in("dgSubMasInvestigation.subInvestigationId", subInvList))
											.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType))
											.add(Restrictions.eq("sex", genderCode).ignoreCase());
								}
								normalValList = crNorVal.list();
							}
						
					}
					}else if(investigationTypes.get(i).equalsIgnoreCase("s")) {
						String investigationType1 = investigationTypes.get(i);
						Criteria cr4 = session.createCriteria(DgResultEntryDt.class)
								.createAlias("dgResultEntryHd", "dgResultEntryHd")
								.createAlias("dgMasInvestigation", "dgMasInvestigation")
								.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType1))
								.add(Restrictions.in("dgMasInvestigation.investigationId", investigationList))
								.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
								.add(Restrictions.eq("resultType", "s"));
						singleParameterList = cr4.list();
					}else {
						Criteria cr5 = session.createCriteria(DgResultEntryDt.class)
								.createAlias("dgResultEntryHd", "dgResultEntryHd")
								.createAlias("dgMasInvestigation", "dgMasInvestigation")
								.add(Restrictions.eq("dgMasInvestigation.investigationType", "t"))
								.add(Restrictions.in("dgMasInvestigation.investigationId", investigationList))
								.add(Restrictions.eq("dgResultEntryHd.resultEntryId", resultEntryHdId))
								.add(Restrictions.eq("resultType", "t"));
						templateParameterList = cr5.list();
					}
				}
			}
			
			
			map.put("dgResultEntryHdList", dgResultEntryHdList);
			map.put("dgResultEntryDtInvList", dgResultEntryDtInvList);
			map.put("parentInvestigationList", parentInvestigationList);
			map.put("singleParameterList", singleParameterList);
			map.put("templateParameterList", templateParameterList);
			
			map.put("normalValList", normalValList);
			map.put("mapFixedValue", mapFixedValue);
			map.put("mapFixedValue", mapFixedValue);
						
				
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	
	}


	@Override
	public boolean updateResultEntryDetails(String[] resultDtIds, String[] remarks, String[] resultValues, Long userId,
			String[] subInvestigationIdArray, String[] normalRangeValue, String[] parentInvestigationIdArray ) {

		boolean flag = false;
		Session session=null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			int count=0;
			Long updated=null;
			String rangeVal = "";
			//Map<Long, Object> resultRangeMap  = validateResultEnteredRange(subInvestigationIdArray, resultValues, normalRangeValue);
			Map<Long, Object> resultRangeMap  = validateResultEnteredRangeForUpdate(subInvestigationIdArray,  resultValues,  normalRangeValue, parentInvestigationIdArray);
			////
			if(subInvestigationIdArray!=null && subInvestigationIdArray.length>0) {
			//if(parentInvestigationIdArray!=null && parentInvestigationIdArray.length>0) {
				for(String l:resultDtIds) {
					DgResultEntryDt dgResultEntryDt =dgResultEntryDtDao.find(Long.parseLong(l));
					if(remarks.length>0 && count<remarks.length) {
						if(remarks[count]!=null && !remarks[count].toString().equalsIgnoreCase("")) {
							dgResultEntryDt.setRemarks(remarks[count]);
						}else {
							dgResultEntryDt.setRemarks("");
						}
					}else {
						dgResultEntryDt.setRemarks("");
					}
					
					 
					if(resultValues.length>0 && count<resultValues.length) {
						if(resultValues[count]!=null && !resultValues[count].toString().equalsIgnoreCase("")) {
							Timestamp lastUpdatedDate = new Timestamp(System.currentTimeMillis());
							dgResultEntryDt.setResult(resultValues[count]);
							dgResultEntryDt.setLastUpdatedDate(lastUpdatedDate);
							dgResultEntryDt.setLastUpdatedBy(userId);
						}else {
							dgResultEntryDt.setResult("");
							dgResultEntryDt.setLastUpdatedDate(null);
							dgResultEntryDt.setLastUpdatedBy(userId);
						}
						
					}
					
					if(normalRangeValue!=null) {
						if(resultRangeMap.containsKey(Long.parseLong(subInvestigationIdArray[count]))) {
							 rangeVal=(String) resultRangeMap.get(Long.parseLong(subInvestigationIdArray[count]));
							 if(rangeVal!=null) {
								 dgResultEntryDt.setRangeStatus(rangeVal);
							 }else {
								 dgResultEntryDt.setRangeStatus("");
							 }
							 
						}else {
							dgResultEntryDt.setRangeStatus("");
						}
					}else {
						dgResultEntryDt.setRangeStatus("");
					}
				
					
					dgResultEntryDtDao.saveOrUpdate(dgResultEntryDt);
					updated=dgResultEntryDt.getResultEntryId();
					count++;
					
				}
				
			}
			else if(parentInvestigationIdArray!=null && parentInvestigationIdArray.length>0) {
				for(String l:resultDtIds) {
					DgResultEntryDt dgResultEntryDt =dgResultEntryDtDao.find(Long.parseLong(l));
					if(remarks.length>0 && count<remarks.length) {
						if(remarks[count]!=null && !remarks[count].toString().equalsIgnoreCase("")) {
							dgResultEntryDt.setRemarks(remarks[count]);
						}else {
							dgResultEntryDt.setRemarks("");
						}
					}else {
						dgResultEntryDt.setRemarks("");
					}
					
					 
					if(resultValues.length>0 && count<resultValues.length) {
						if(resultValues[count]!=null && !resultValues[count].toString().equalsIgnoreCase("")) {
							Timestamp lastUpdatedDate = new Timestamp(System.currentTimeMillis());
							dgResultEntryDt.setResult(resultValues[count]);
							dgResultEntryDt.setLastUpdatedDate(lastUpdatedDate);
							dgResultEntryDt.setLastUpdatedBy(userId);
						}else {
							dgResultEntryDt.setResult("");
							dgResultEntryDt.setLastUpdatedDate(null);
							dgResultEntryDt.setLastUpdatedBy(userId);
						}
						
					}
					if(normalRangeValue!=null) {
						if(resultRangeMap.containsKey(Long.parseLong(parentInvestigationIdArray[count]))) {
							 rangeVal=(String) resultRangeMap.get(Long.parseLong(parentInvestigationIdArray[count]));
							 if(rangeVal!=null) {
								 dgResultEntryDt.setRangeStatus(rangeVal);
							 }else {
								 dgResultEntryDt.setRangeStatus("");
							 }
							 
						}else {
							dgResultEntryDt.setRangeStatus("");
						}
					}else {
						dgResultEntryDt.setRangeStatus("");
					}
					
					
					dgResultEntryDtDao.saveOrUpdate(dgResultEntryDt);
					updated=dgResultEntryDt.getResultEntryId();
					count++;
					
				}
			}
			
			if(count!=0) {
				flag = true;
			}else {
				return flag;
			}
			session.flush();
			session.clear();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;
	
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Object> validateResultEnteredRange(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		try {
			String value = "";
			if(resultValue!=null && normalRangeValue!=null ) {
				//for(int i=0; i<normalRangeValue.length;i++) {
				for(int i=0; i<resultValue.length;i++) {
					if(StringUtils.isNotEmpty(resultValue[i]) && !normalRangeValue[i].isEmpty()) {
						String rangeValue = normalRangeValue[i];
						 String[] rangesplit = rangeValue.split("-");
						 String lowervalue = rangesplit[0];
						 String uppervalue = rangesplit[1];
						 
						 if(Double.valueOf(resultValue[i])<Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])>Double.valueOf(uppervalue)) {
							 String outRange = "OR";
							 map.put(Long.parseLong(subInvestigationIdsArray[i]), outRange);
							 
						 }else if(Double.valueOf(resultValue[i])>Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])<Double.valueOf(uppervalue)){
							 String innerRange = "IR"; 
							 map.put(Long.parseLong(subInvestigationIdsArray[i]), innerRange);
						 }
					 }else {
						 map.put(Long.parseLong(subInvestigationIdsArray[i]), null);
					 }
					}
					 
			}else {
				
			}
			
			
			}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, Object> validateResultEnteredRangeForUpdate(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue, String[] parentInvestigationIdArray ) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		try {
			String value = "";
			
			if(subInvestigationIdsArray!=null && subInvestigationIdsArray.length>0) {
				for(int i=0; i<normalRangeValue.length;i++) {
					 String rangeValue = normalRangeValue[i];
					 String[] rangesplit = rangeValue.split("-");
					 String lowervalue = rangesplit[0];
					 String uppervalue = rangesplit[1];
					 
					 if(Double.valueOf(resultValue[i])<Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])>Double.valueOf(uppervalue)) {
						 String outRange = "OR";
						 map.put(Long.parseLong(subInvestigationIdsArray[i]), outRange);
						 
					 }else if(Double.valueOf(resultValue[i])>Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])<Double.valueOf(uppervalue)){
						 String innerRange = "IR"; 
						 map.put(Long.parseLong(subInvestigationIdsArray[i]), innerRange);
					 }
					 
				 }
			}else if(parentInvestigationIdArray!=null && parentInvestigationIdArray.length>0) {
				for(int i=0; i<normalRangeValue.length;i++) {
					 String rangeValue = normalRangeValue[i];
					 String[] rangesplit = rangeValue.split("-");
					 String lowervalue = rangesplit[0];
					 String uppervalue = rangesplit[1];
					 
					 if(Double.valueOf(resultValue[i])<Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])>Double.valueOf(uppervalue)) {
						 String outRange = "OR";
						 map.put(Long.parseLong(parentInvestigationIdArray[i]), outRange);
						 
					 }else if(Double.valueOf(resultValue[i])>Double.valueOf(lowervalue) || Double.valueOf(resultValue[i])<Double.valueOf(uppervalue)){
						 String innerRange = "IR"; 
						 map.put(Long.parseLong(parentInvestigationIdArray[i]), innerRange);
					 }
					 
					 
				 }
			}
			
			
			
			}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> validateResultEnteredRangeForUpadate(String[] subInvestigationIdsArray, String[] resultValue, String[] normalRangeValue, List<Long> resultDtId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String value = "";
			String lowerValue = "";
			String upperValue = null;
			
			for(int i=0;i<normalRangeValue.length;i++) {
			if(!normalRangeValue[i].equalsIgnoreCase("")) {
				String rangeValue = normalRangeValue[i];
				String[] rangesplit = rangeValue.split("-");
					for(int j=0;j<rangesplit.length;j++) {
						 lowerValue = rangesplit[0];
						 upperValue = rangesplit[1];
					}
					
					int lValue = resultValue[i].toString().compareTo(lowerValue);
					int UValue = resultValue[i].toString().compareTo(upperValue);
					if(lValue<-1 || UValue>0) {
							String outRange = "OR"+"@@"+resultValue[i]; //outRange
							map.put(""+resultDtId.get(i), outRange);
						}
					else if(lValue>-1 || UValue > 1){
							String innerRange = "IR"+"@@"+resultValue[i]; //innerRange
							map.put(""+resultDtId.get(i), innerRange);
						}
					else if(lValue == -1) {
						String outRange = "OR"+"@@"+resultValue[i]; //outRange
						map.put(""+resultDtId.get(i), outRange);
					}
					
					
						
					}
			}
			Integer countForResult=0;
			Map<Object,Object>mapResult=new HashMap<>();
			for(Long l :resultDtId) {
				
				mapResult.put(l, resultValue[countForResult]);
				countForResult++;
			}
			map.put("mapResult", mapResult);
			}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}*/


	@Override
	public Map<String, Object> getSampleRejectedWaitingList(JSONObject jsonData) {
		Session session=null;
		List<DgSampleCollectionDt> dgSampleCollectionDts = null;
		Map<String,Object> map = new HashMap<>();
		 Long hospitalId = null;
		 String patientName = "";
		 String serviceNo = "";
		 String mobileNo = "";
		 
		 if (jsonData.has("hospitalId")) {
				hospitalId = Long.parseLong(jsonData.get("hospitalId").toString());
			}

			if (jsonData.get("serviceNo") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("serviceNo"))) && jsonData.has("serviceNo")) {
				serviceNo = jsonData.get("serviceNo").toString();
			}
			if (jsonData.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("patientName")))  && jsonData.has("patientName")) {
				patientName = "%" + jsonData.get("patientName").toString() + "%";
			}

			if (jsonData.get("mobileNumber") != null && StringUtils.isNotBlank(String.valueOf(jsonData.get("mobileNumber"))) && jsonData.has("mobileNumber")) {
				mobileNo = "%" + jsonData.get("mobileNumber").toString() + "%";
			}
			
		 
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
		
			Date fromDate = new Date();
			String d1 = HMSUtil.convertDateToStringFormat(fromDate, "dd/MM/yyyy");
/*			String d1 = HMSUtil.changeDateToddMMyyyy(fromDate);*/
			Date fromDate1 = HMSUtil.convertStringTypeDateToDateType(d1);
			
			Calendar c = Calendar.getInstance();
	        c.setTime(fromDate);
	        c.add(Calendar.DATE, 1);
	        Date toDate = c.getTime();
	        String d2 = HMSUtil.convertDateToStringFormat(toDate, "dd/MM/yyyy");
			Date toDate1 = HMSUtil.convertStringTypeDateToDateType(d2);
			
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class, "DgSampleCollectionDt")
						.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
						.createAlias("dgSampleCollectionHd.patient", "patient")
						.add(Restrictions.eq("dgSampleCollectionHd.hospitalId", hospitalId))
						.add(Restrictions.eq("DgSampleCollectionDt.orderStatus", "R"))
						.setProjection(Projections.projectionList()
								.add(Projections.property("sampleCollectionHdId").as("sampleCollectionHdId"))
								.add(Projections.property("subcharge").as("subcharge"))
								.add(Projections.groupProperty("sampleCollectionHdId"))									
								.add(Projections.groupProperty("subcharge")))
								.setResultTransformer(Transformers.aliasToBean(DgSampleCollectionDt.class));
			
				if( (serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) ) {
					Calendar c2 = Calendar.getInstance();
			        c2.setTime(fromDate);
			        c2.add(Calendar.DATE, -15);
			        Date toDate2 = c2.getTime();
			        String date = HMSUtil.convertDateToStringFormat(toDate2, "dd/MM/yyyy");
					Date fromDate3 = HMSUtil.convertStringTypeDateToDateType(date);
					criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate3))
							.add(Restrictions.lt("lastChgDate", toDate1));
				}else {
					criteria = criteria.add(Restrictions.ge("lastChgDate", fromDate1))
					.add(Restrictions.lt("lastChgDate", toDate1));
				}
			
				if (serviceNo != null && StringUtils.isNotBlank(serviceNo)) {
					criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());

				}
				if (patientName != null && StringUtils.isNotEmpty(patientName)) {
					criteria.add(Restrictions.ilike("patient.patientName", patientName));
				}

				if (mobileNo != null && StringUtils.isNotBlank(mobileNo)) {
					criteria.add(Restrictions.ilike("patient.mobileNumber", mobileNo));
				}
				
			dgSampleCollectionDts = criteria.list();
			count = dgSampleCollectionDts.size();
			
			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			dgSampleCollectionDts = criteria.list();
		
			map.put("count", count);
			map.put("list", dgSampleCollectionDts);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}


	/*@Override
	public Map<Long, Object> validateResultEnteredRangeForSingleParameter(JSONArray investigationId, JSONArray result,
			JSONArray normalRange) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		try {
			Long lowerValue = null;
			Long upperValue = null;
			String lowerValue = "";
			String upperValue = "";
			for(int i=0;i<normalRange.length();i++) {
				String rangeValue = normalRange.getString(i);
				String[] rangesplit = rangeValue.split("-");
					for(int j=0;j<rangesplit.length;j++) {
						 lowerValue = rangesplit[0];
						 upperValue = rangesplit[1];
					}
					int lValue = result.get(i).toString().compareTo(lowerValue);
					int UValue = result.get(i).toString().compareTo(upperValue);
					
					if(lValue<-1 || UValue>0) {
						//if(result.getString(i)<lowerValue || result.getString(i)>upperValue) {
							String outRange = "OR"; //outRange
							map.put(Long.parseLong(investigationId.getString(i)), outRange);
						}
					else if(lValue<-1 || UValue < -2){
						//else if(Long.parseLong(result.getString(i))>lowerValue || Long.parseLong(result.getString(i))<upperValue){
							String innerRange = "IR"; //innerRange
							map.put(Long.parseLong(investigationId.getString(i)), innerRange);
						}
						
					}
			
			}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}*/


	/*@Override
	public List<DgSampleCollectionDt> getSampleRejectedResultEntryDetails(Long sampleCollectionHdId,
			Long subchargeCodeId) {
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		List<DgSampleCollectionDt> list = null;
		List<Object> invList = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.patient", "patient")
					.add(Restrictions.eq("orderStatus", "R").ignoreCase())
					// .add(Restrictions.eq("dgSampleCollectionHd.departmentId", departId))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.eq("subcharge", subchargeCodeId));
			dgSampleCollectionDtsList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsList;
	}
*/

	@Override
	public Map<String, Object> getAllRejectedInvestigations(Long sampleCollectionHdId, Long subchargeCodeId,
			String investigationType) {
		Map<String, Object> mapdata = new HashMap<String, Object>();
		Session session = null;
		List<Object[]> list = null;
		List<DgSampleCollectionDt> dgSampCollDtsInvList = null;
		List<DgSampleCollectionDt> dtList = null;
		List<Object> investigationList = new ArrayList<Object>();
		List<DgSubMasInvestigation> subInvestigationList = null;
		List<Object> subInvList = new ArrayList<Object>();
		List<DgFixedValue> fixedValueList = null;
		Map<String, Object> mapFixedValue = null;
		List<DgNormalValue> normalValList = null;
		String comparisionType = "";
		List<DgSampleCollectionDt> invTypeTempList = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			/*investigationType = single*/
			if (investigationType.equalsIgnoreCase("s")) {
				Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
						/*.createAlias("dgOrderdt", "dgOrderdt")
						.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")*/
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
						.createAlias("dgSampleCollectionHd.patient", "patient")
						.add(Restrictions.eq("orderStatus", "R").ignoreCase())
						.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
						.add(Restrictions.eq("subcharge", subchargeCodeId))
						.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType));
				dgSampCollDtsInvList = criteria.list();
			}
			/*investigationType = multiple*/
			if (investigationType.equalsIgnoreCase("m")) {
				Criteria cr = session.createCriteria(DgSampleCollectionDt.class)
						/*.createAlias("dgOrderdt", "dgOrderdt")
						.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")*/
						.createAlias("dgMasInvestigation", "dgMasInvestigation")
						.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
						.createAlias("dgSampleCollectionHd.patient", "patient")
						.add(Restrictions.eq("orderStatus", "R").ignoreCase())
						.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
						.add(Restrictions.eq("subcharge", subchargeCodeId))
						.add(Restrictions.eq("dgMasInvestigation.investigationType", investigationType));

				dtList = cr.list();
				Long genderId = dtList.get(0).getDgSampleCollectionHd().getPatient().getAdministrativeSexId();
				// get gender Code

				List<MasAdministrativeSex> listOfGender = session.createCriteria(MasAdministrativeSex.class)
						.add(Restrictions.eq("administrativeSexId", genderId)).list();
				String genderCode = listOfGender.get(0).getAdministrativeSexCode();
				

				for (DgSampleCollectionDt dt : dtList) {

					investigationList.add(dt.getInvestigationId());

				}
				
				
				// get the subInvestigationId from dgSubMasInvestigation
				Criteria cr1 = session.createCriteria(DgSubMasInvestigation.class)
						.createAlias("masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN)
						.add(Restrictions.in("investigationId", investigationList))
						.add(Restrictions.eq("status", "Y").ignoreCase());
				subInvestigationList = cr1.list();

				for (DgSubMasInvestigation subInv : subInvestigationList) {

					subInvList.add(subInv.getSubInvestigationId());
					comparisionType = subInv.getComparisonType();

				}

				// DG_FIXED_VALUE
				if (comparisionType.equalsIgnoreCase("f")) {
					Criteria cr2 = session.createCriteria(DgFixedValue.class)
							.createAlias("dgSubMasInvestigation", "dgSubMasInvestigation")
							.add(Restrictions.in("subInvestigationId", subInvList))
							.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType));

					fixedValueList = cr2.list();
					mapFixedValue = new HashMap<String, Object>();
					for (int i = 0; i < fixedValueList.size(); i++) {
						mapFixedValue.put(fixedValueList.get(i).getFixedValue().trim(),
								fixedValueList.get(i).getFixedId() + "@@"
										+ fixedValueList.get(i).getFixedValue().trim());

					}
				}

				// DG_NORMAL_VALUE
				if (comparisionType.equalsIgnoreCase("v")) {
					Criteria crNorVal = session.createCriteria(DgNormalValue.class)
							.createAlias("dgSubMasInvestigation","dgSubMasInvestigation")
							.add(Restrictions.eq("sex", genderCode).ignoreCase());

					if (genderCode.equalsIgnoreCase(genderCode)) {
						crNorVal.add(Restrictions.in("dgSubMasInvestigation.subInvestigationId", subInvList))
								.add(Restrictions.eq("dgSubMasInvestigation.comparisonType", comparisionType))
								.add(Restrictions.eq("sex", genderCode).ignoreCase());
					}
					normalValList = crNorVal.list();
				}
			}
			/*investigationType = template*/
			if(investigationType.equalsIgnoreCase("t")) {
				Criteria cr1 = session.createCriteria(DgSampleCollectionDt.class).createAlias("dgOrderdt", "dgOrderdt")
						.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations")
						.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
						.createAlias("dgSampleCollectionHd.patient", "patient")
						.add(Restrictions.eq("orderStatus", "R").ignoreCase())
						.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHdId))
						.add(Restrictions.eq("subcharge", subchargeCodeId))
						.add(Restrictions.eq("dgMasInvestigations.investigationType", investigationType));
				
				invTypeTempList = cr1.list();
			}

			mapdata.put("dgSampCollDtsInvList", dgSampCollDtsInvList);
			mapdata.put("subInvestigationList", subInvestigationList);
			mapdata.put("subInvList", subInvList);
			mapdata.put("fixedValueList", fixedValueList);
			mapdata.put("mapFixedValue", mapFixedValue);
			mapdata.put("normalValList", normalValList);
			mapdata.put("invTypeTempList", invTypeTempList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapdata;
	}


	@Override
	public Map<String, Object> getSampleRejectedDetails(JSONObject jsonObject) {

		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Long subchargeCodeId = Long.parseLong(jsonObject.get("subchargeCodeId").toString());
		Long sampleCollectionHeaderId = Long.parseLong(jsonObject.get("sampleCollectionHdId").toString());

		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.createAlias("dgSampleCollectionHd.dgOrderHd", "dgOrderHd")
					.createAlias("dgSampleCollectionHd.patient", "patient").add(Restrictions.eq("orderStatus", "R"))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHeaderId))
					.add(Restrictions.eq("subcharge", subchargeCodeId));

			dgSampleCollectionDtsList = criteria.list();

			map.put("dgSampleCollectionDtsList", dgSampleCollectionDtsList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return map;
	
	}


	@Override
	public List<DgSampleCollectionDt> getAllRejectedInvestigations(JSONObject jsonObject) {

		Session session = null;
		List<DgSampleCollectionDt> dgSampleCollectionDtsValidateSampleList = null;
		Long subchargeCodeId = Long.parseLong(jsonObject.get("subchargeCodeId").toString());
		Long sampleCollectionHeaderId = Long.parseLong(jsonObject.get("sampleCollectionHdId").toString());
		
		String mainChargeCode=HMSUtil.getProperties("js_messages_en.properties", "mainchargeCodeLab");
		MasMainChargecode mmcc=null;
		mmcc =opdMasterDao. getMainChargeCode(mainChargeCode);
		Long	MAIN_CHARGECODE_ID=mmcc.getMainChargecodeId();
		
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					/*.createAlias("dgOrderdt", "dgOrderdt")
					.createAlias("dgOrderdt.dgMasInvestigations", "dgMasInvestigations",JoinType.LEFT_OUTER_JOIN)*/
					.createAlias("dgMasInvestigation", "dgMasInvestigation",JoinType.LEFT_OUTER_JOIN)
					.createAlias("dgSampleCollectionHd", "dgSampleCollectionHd")
					.add(Restrictions.eq("orderStatus", "R"))
					.add(Restrictions.eq("dgSampleCollectionHd.sampleCollectionHdId", sampleCollectionHeaderId))
					.add(Restrictions.eq("subcharge", subchargeCodeId))
					.add(Restrictions.eq("maincharge", MAIN_CHARGECODE_ID));

			dgSampleCollectionDtsValidateSampleList = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgSampleCollectionDtsValidateSampleList;
	
	}


	@Override
	public List<DgSampleCollectionDt> getSampleCollectionDtsIdForReject(Long sampleCollectionHdId, Long subchargeCodeId, Long investID) {
		Session session = null;
		List<DgSampleCollectionDt> list = new ArrayList<DgSampleCollectionDt>();
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();

			Criteria criteria = session.createCriteria(DgSampleCollectionDt.class)
					.add(Restrictions.eq("sampleCollectionHdId", sampleCollectionHdId))
					.add(Restrictions.eq("subcharge", subchargeCodeId))
					.add(Restrictions.eq("orderStatus", "R"))
					.add(Restrictions.eq("investigationId", investID));
			list = criteria.list();
		} catch (Exception e) {

		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return list;
	}


	@Override
	public boolean updateDgSampleCollectionDtForReject(List<DgSampleCollectionDt> sampleCollectionDtIds, String[] reason, String[] additionalRemarks, String acceptflag) {
		Session session = null;
		boolean flag = false;
		Transaction tx = null;

		try {
			String s = "";
			String comma = "";
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			tx = session.beginTransaction();
			List<Long> listOfIds = new ArrayList<Long>();
			int count=0;
			Long result = null;
			if(acceptflag.equalsIgnoreCase("A")) {
			for (DgSampleCollectionDt dt : sampleCollectionDtIds) {
				
				if(reason.length>0 && count<reason.length) {
					if(reason[count]!=null && !reason[count].toString().equalsIgnoreCase("")) {
						dt.setReason(reason[count]);
					}else {
						dt.setReason("");
					}
				}else {
					dt.setReason("");
				}
				
				if(additionalRemarks.length>0 && count<additionalRemarks.length) {
					if(additionalRemarks[count]!=null && !additionalRemarks[count].toString().equalsIgnoreCase("")) {
						dt.setAdditionalRemarks(additionalRemarks[count]);
					}else {
						dt.setAdditionalRemarks("");
					}
				}else {
					dt.setAdditionalRemarks("");
				}
				
				
				dt.setOrderStatus("P");
				
				dgSampleCollectionDtDao.saveOrUpdate(dt);
			    result = dt.getSampleCollectionDtId();
			count++;
			}
		}
			
			tx.commit();
			session.flush();
			session.clear();
			if (result != 0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return flag;

	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findAuthenticateUHID(HashMap<String, Object> jsondata) {
		Map<String, Object> map = new HashMap<String, Object>();
		String status="";
		Session session =null;
		String uhidNo = "";
		
		if(jsondata.containsKey("uhidNo")) {
			if(!jsondata.get("uhidNo").equals("") && jsondata.get("uhidNo")!=null) {
				uhidNo = jsondata.get("uhidNo").toString().toUpperCase();
				
			}
		}
		
		
		
		try {
			Map<String, Object> mapdata = new HashMap<String, Object>();
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			
			if(!uhidNo.equalsIgnoreCase("")  && StringUtils.isNotBlank(uhidNo)) {
				Criteria cr = session.createCriteria(Patient.class)
						.add(Restrictions.eq("uhidNo",uhidNo));
				
				List<Patient>listPatient=cr.list();
				if(CollectionUtils.isNotEmpty(listPatient)) {
					status="success";
					mapdata = getLabHistoryDetails(jsondata);
					map.put("mapdata", mapdata);
					map.put("status", status);
				}
				else {
					status="error";
					map.put("status", status);
				}
			}else {
				status="error";
				map.put("status", status);
				
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return map;
	}


	public Map<String, Object> getLabHistoryDetails(HashMap<String, Object> jsondata) {

		Session session = null;
		Criteria criteria = null;
		
		List<DgResultEntryDt> dtsList = null; 
		Map<String, Object> map = new HashMap<String, Object>();
		Long hospitalId = null;
		String uhidNo = "";
		
		Date toDate1=null;		
		Date fromDate =null;
		Date toDate =null;
		String investigationName="";
		Long investigationId=null;
		Long patientId = null;
		String fromDateNewVal="";
		String toDateNewVal="";
		if(jsondata.containsKey("fromDate") && jsondata.get("fromDate")!=null) {
			if(!jsondata.get("fromDate").equals("") && jsondata.get("fromDate")!=null) {
				 fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("fromDate")));	
				Calendar c = Calendar.getInstance();
				c.setTime(fromDate);
				fromDateNewVal=HMSUtil.converStringDateFormatToStringDate(jsondata.get("fromDate").toString(),"dd/MM/yyyy","yyyy-MM-dd") ;
			}
		}
		
		if(jsondata.containsKey("toDate") && jsondata.get("toDate")!=null) {
			if(!jsondata.get("toDate").equals("") && jsondata.get("toDate")!=null) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsondata.get("toDate")));
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				//c.add(Calendar.DATE, 1);
				toDate1 = c.getTime();
			
				toDateNewVal=HMSUtil.converStringDateFormatToStringDate(jsondata.get("toDate").toString(),"dd/MM/yyyy","yyyy-MM-dd") ;
			}
		}
		
		/*
		if(jsondata.containsKey("investigationName") && jsondata.get("investigationName")!="") {
			investigationName = jsondata.get("investigationName").toString();
			int index1 = investigationName.lastIndexOf("[");
			int index2 = investigationName.lastIndexOf("]");
			index1++;
			investigationId = Long.parseLong(investigationName.substring(index1,index2));
			System.out.println("investigationId :: "+investigationId);
		}
		*/
		
		
		

		
		
		System.out.println("pageno="+jsondata.get("pageNo"));
		try {
			int pageNo = Integer.parseInt(String.valueOf(jsondata.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			criteria = session.createCriteria(DgResultEntryDt.class)
					.createAlias("dgResultEntryHd", "dgResultEntryHd",JoinType.LEFT_OUTER_JOIN) 
					/* .createAlias("dgMasInvestigation", "dgMasInvestigation") */
					.createAlias("dgResultEntryHd.patient", "patient",JoinType.LEFT_OUTER_JOIN)
					.createAlias("dgResultEntryHd.masMmu", "mmu",JoinType.LEFT_OUTER_JOIN)
					//.createAlias("masUOM", "masUOM", JoinType.LEFT_OUTER_JOIN)
					
					.add(Restrictions.isNotNull("result"))
					.add(Restrictions.eq("dgResultEntryHd.mainChargecodeId", 2l));
					
					criteria.addOrder(Order.desc("lastChgDate"));
					
			/**/
					
					
					if(fromDate!=null && toDate1!=null) {
						criteria.add(Restrictions.between("lastChgDate", fromDate, toDate1))
							.addOrder(Order.desc("lastChgDate"));
						   
						 
					}
					

					if(jsondata.containsKey("mmuId") && jsondata.get("mmuId")!="") {
						
						String mmu_id= jsondata.get("mmuId").toString();
						
						Long mmu_Long_id = Long.parseLong(mmu_id);
					
						criteria.add(Restrictions.eq("mmu.mmuId", mmu_Long_id));
						
					}
					
					if(investigationId!=null) {
						criteria.add(Restrictions.eq("dgMasInvestigation.investigationId", investigationId))
						.addOrder(Order.desc("investigationId"));
					}
					
					String patientName="";
					if (jsondata.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsondata.get("patientName"))) ) {
						patientName = "%" + jsondata.get("patientName").toString() + "%";
						
						criteria.add(Restrictions.ilike("patient.patientName", patientName));
					}
					
					String mobileNo="";
					if (jsondata.get("mobileNo") != null && StringUtils.isNotBlank(String.valueOf(jsondata.get("mobileNo"))) ) {
						mobileNo = jsondata.get("mobileNo").toString().trim();
						
						criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo));
					}
					
					if(fromDate!=null && toDate1!=null) {
						criteria.add(Restrictions.between("lastChgDate", fromDate, toDate1))
							.addOrder(Order.desc("lastChgDate"));
					}
					
					/*
					if(jsondata.containsKey("investigationName") && jsondata.get("investigationName")!="") {
						investigationName = jsondata.get("investigationName").toString();
						int index1 = investigationName.lastIndexOf("[");
						int index2 = investigationName.lastIndexOf("]");
						index1++;
						investigationId = Long.parseLong(investigationName.substring(index1,index2));
						System.out.println("investigationId :: "+investigationId);
					}
					*/
					
					if(investigationId!=null) {
						criteria.add(Restrictions.eq("dgMasInvestigation.investigationId", investigationId))
						.addOrder(Order.desc("investigationId"));
					}
					
					
					
						 String qry = "select dt.RESULT_ENTRY_HD_ID from dg_result_entry_dt dt \r\n" + 
								"left outer join dg_result_entry_hd hd on dt.RESULT_ENTRY_HD_ID=hd.RESULT_ENTRY_HD_ID\r\n" + 
								"left outer join mas_mmu m on hd.mmu_id=m.mmu_id\r\n" + 
								"left outer join patient p on hd.patient_id=p.patient_id where hd.mmu_id is not null and dt.RESULT is not null and hd.MAIN_CHARGECODE_ID=2 ";			
									
					
					
						if (jsondata.get("patientName") != null && StringUtils.isNotBlank(String.valueOf(jsondata.get("patientName"))) )  {
							String pName = "'%" + jsondata.get("patientName").toString() + "%'";
							qry= qry +" and p.patient_name ilike "+pName;
						}	
						
						if(jsondata.containsKey("mmuId") && jsondata.get("mmuId")!="") {
							
							String mmu_id= jsondata.get("mmuId").toString();
							
							int mmu_Long_id = Integer.parseInt(mmu_id);
						
							qry= qry +" and hd.mmu_id="+mmu_Long_id;
							
						}
						
						if (jsondata.get("mobileNo") != null && StringUtils.isNotBlank(String.valueOf(jsondata.get("mobileNo"))) ) {
							mobileNo = jsondata.get("mobileNo").toString().trim();
							qry= qry +" and p.mobile_number='"+mobileNo+"'";
						}
						
						if(fromDate!=null && toDate1!=null) {
							  
							qry= qry +" and dt.LAST_CHG_DATE >= '"+fromDateNewVal+"' and dt.LAST_CHG_DATE <= '"+toDateNewVal+"' ";
						}
						System.out.println("qry="+qry);
						
						Query queryHiber = (Query) session.createSQLQuery(qry);					

						List<Object[]> objectList = (List<Object[]>) queryHiber.list();				
					
					
					count = objectList.size();
					
					criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
					criteria = criteria.setMaxResults(pagingSize);
					dtsList = criteria.list();
					
					System.out.println("dtsList="+dtsList.size());
			
			map.put("dtsList", dtsList);
			map.put("count", count);
			map.put("status", "success");
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	
	}


	@SuppressWarnings("unchecked")
	@Override
	public String checkAuthenticateUser(HashMap<String, Object> jsondata) {
		String status="";
		Session session =null;
		String uhidNo = "";
		Long patientId = null;
		if(jsondata.containsKey("uhidNo")) {
			if(!jsondata.get("uhidNo").equals("") && jsondata.get("uhidNo")!=null) {
				uhidNo = jsondata.get("uhidNo").toString();
				System.out.println("uhidNo :: "+uhidNo);
			}
		}
		
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria criteria = session.createCriteria(Patient.class).add(Restrictions.ilike("uhidNo", jsondata.get("uhidNo").toString()));
					
					List<Patient>listPatient=criteria.list();
					if(listPatient!=null && listPatient.size()>0) {
						patientId = listPatient.get(0).getPatientId();
						Criteria cr = session.createCriteria(Patient.class)
								.add(Restrictions.ilike("uhidNo",uhidNo))
								.add(Restrictions.eq("patientId", patientId));
						
								ProjectionList projectionList = Projections.projectionList();
								projectionList.add(Projections.property("patientId").as("patientId"))
								 .add(projectionList) ;
						List<Patient>listPatient1=cr.list();
						if(CollectionUtils.isNotEmpty(listPatient1)) {
							status="success";
						}
						else {
							status="error";
						}
					}else {
						status="error";
					}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		return status;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String checkAuthenticateServiceNo(HashMap<String, Object> jsondata) {
		Session session = null;
		String authenticateStatus = null;
		String serviceNo = "";
		if(jsondata.containsKey("serviceNo")) {
			if(!jsondata.get("serviceNo").equals("") && jsondata.get("serviceNo")!=null) {
				serviceNo = jsondata.get("serviceNo").toString();
				System.out.println("serviceNo :: "+serviceNo);
			}
		}
		
		try {
			session=getHibernateUtils.getHibernateUtlis().OpenSession();
			
				Criteria crr = session.createCriteria(Patient.class)
						.add(Restrictions.eq("serviceNo", serviceNo).ignoreCase());
				List<Patient> patientList = crr.list();
				if(patientList!=null && patientList.size()>0) {
					authenticateStatus = "success";
				}else {
					authenticateStatus = "failure";
				}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		
		return authenticateStatus;
	}


	@Override
	public List<DgMasInvestigation> getInvestigationList(Map<String, Object> requestData) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<DgMasInvestigation> list = new ArrayList<>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(DgMasInvestigation.class).add(Restrictions.eq("status", "Y").ignoreCase());

		
			list = criteria.addOrder(Order.asc("investigationId")).list();
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	
	}


	/*@SuppressWarnings("unchecked")
	@Override
	public List<DgMasInvestigation> getAllInvestigationsForAllInvestigation(List investigationList) {
		List<DgMasInvestigation> dgMasInvestigations = null;
		Session session = null;
		try {
			session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr =  session.createCriteria(DgMasInvestigation.class)
						.add(Restrictions.eq("investigationType", "m").ignoreCase())
						.add(Restrictions.in("investigationId", investigationList))
						.setProjection(Projections.projectionList()
								.add(Projections.property("investigationId").as("investigationId"))
								.add(Projections.property("investigationType").as("investigationType"))
								.add(Projections.property("investigationName").as("investigationName")))
								.setResultTransformer(Transformers.aliasToBean(DgMasInvestigation.class));
						
			
			dgMasInvestigations = cr.list();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return dgMasInvestigations;
	}*/
	
	 
	@Override
	public  MasCamp getMasCampFromMMUId(Long campId,Session session){
         MasCamp  masCamp = null;;
          try {
        	masCamp = (MasCamp) session.createCriteria(MasCamp.class).add(Restrictions.eq("campId",campId)).uniqueResult();
               
        }catch(Exception e) {
            e.printStackTrace();
        } 

        return masCamp;
    }
}
