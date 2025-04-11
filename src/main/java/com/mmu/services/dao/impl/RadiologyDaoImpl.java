package com.mmu.services.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.RadiologyDao;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.entity.RidcEntity;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

@Repository
public class RadiologyDaoImpl implements RadiologyDao {
	
	@Autowired
	GetHibernateUtils getHibernateUtils;	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultValidation(HashMap<String, Object> inputJson) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String,Object> map = new HashMap<>();
		Map<String,String> map2 = new HashMap<>();
		
		try{
			long id = Long.parseLong(String.valueOf(inputJson.get("id")));
			long userId = Long.parseLong(String.valueOf(inputJson.get("userId")));
			List<DgResultEntryDt> list = session.createCriteria(DgResultEntryDt.class)
					.add(Restrictions.eq("resultEntryDetailId", id)).list();
			
			String sql1 = "select * from dg_result_entry_dt where RESULT_ENTRY_DT_ID=:id";
			Query query1 = session.createSQLQuery(sql1)
			.addScalar("RESULT", StandardBasicTypes.STRING)
			.addScalar("result_entry_dt_id", StandardBasicTypes.LONG) ;
			query1.setLong("id", id);  
			List<Object[]> result1 = query1.list();
			String resultEntry="";
			for(Object[] obj : result1) {
				resultEntry = obj[0].toString();
			}
			System.out.println("result "+resultEntry);
			
			String sql = "select * from dg_result_entry_dt dt, RIDC_UPLOAD re where re.ridc_Id = dt.ridc_ID and dt.RESULT_ENTRY_DT_ID=:dtId";
			//String sql = "select * from dg_result_entry_dt dt, RIDC_ENTITY re where dt.RESULT_ENTRY_DT_ID=:dtId";  
			Query query = session.createSQLQuery(sql)
			.addScalar("document_Name", StandardBasicTypes.STRING)
			.addScalar("document_Id", StandardBasicTypes.LONG)
			.addScalar("document_Format", StandardBasicTypes.STRING);
			query.setParameter("dtId", id);   
			
			List<Object[]> result = query.list(); 
			String dId = null;
			String docName = "";
			String dFormat = "";
			for(Object[] rows:result) {
				//Object[]rows=aa;
				  //resultEntry = StringEscapeUtils.escapeHtml4(rows[0].toString());
				  //resultEntry = rows[0].toString();
				  System.out.println("resultEntry "+resultEntry);
				  docName = HMSUtil.convertNullToEmptyString(rows[0].toString());
				  dId = String.valueOf(rows[1]);				  
				  dFormat = HMSUtil.convertNullToEmptyString(String.valueOf(rows[2]));
			}
			//String resultEntry = StringEscapeUtils.escapeHtml4(rows[0].toString());
			Users userData = (Users) session.createCriteria(Users.class)
					.add(Restrictions.eq("userId", userId)).uniqueResult();
			map.put("html1", resultEntry);	
			map.put("docName", docName);	
			map.put("dId", dId);
			map.put("dFormat", dFormat);
			map.put("list", list);
			map.put("userData", userData);
			return map;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}


	@Override
	public String getRankUsingServiceNo(String serviceN) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String query = "SELECT r.rank_name, r.rank_id from VU_MAS_rank r, VU_MAS_EMPLOYEE e where r.RANK_CODE = e.RANK_ID and e.service_no =:serviceNo";
		//.setResultTransformer( Transformers.aliasToBean(StudentDTO.class) )
		SQLQuery queryString = (SQLQuery) session.createSQLQuery(query);
		queryString.setParameter("serviceNo", serviceN);
		Object[] masRankData = (Object[]) queryString.uniqueResult();
		String rank = "";
		if(masRankData != null) {
			rank = String.valueOf(HMSUtil.convertNullToEmptyString(masRankData[0]));
		}
		return rank;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getResultPrintingData(HashMap<String, Object> jsonData) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String,Object> map = new HashMap<>();
		try {
			long hospitalId = Long.parseLong(String.valueOf(jsonData.get("hospitalId")));
			String serviceNo = String.valueOf(jsonData.get("serviceNo"));
			String patientName = String.valueOf(jsonData.get("patientName"));
			String mobileNo = String.valueOf(jsonData.get("mobileNo"));
			String investigationName = String.valueOf(jsonData.get("investigationName"));
			
			Date fromDate = null;
			/*if (jsonData.get("from_date") != null && !jsonData.get("from_date").equals("")) {
				fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonData.get("from_date")));
			}*/
			Date toDate = null;
			/*if (jsonData.get("to_date") != null && !jsonData.get("to_date").equals("")) {
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonData.get("to_date")));
			}*/
			int pageNo = Integer.parseInt(String.valueOf(jsonData.get("pageNo")));
			int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
			int count=0;
			Long investigationIdd = null;
			
			/*if(jsonData.containsKey("investigationName")) {
				Criteria crr = session.createCriteria(DgMasInvestigation.class)
						.add(Restrictions.ilike("investigationName", investigationName));
				List<DgMasInvestigation> investigationList = crr.list();
				if(investigationList!=null && investigationList.size()>0)
					investigationIdd = investigationList.get(0).getInvestigationId();
			}*/
			
			Criteria criteria = session.createCriteria(DgResultEntryDt.class)
					.add(Restrictions.or(Restrictions.eq("resultDetailStatus", "C").ignoreCase(),Restrictions.eq("resultDetailStatus", "E").ignoreCase()))
					//.add(Restrictions.eq("resultType", "T").ignoreCase())
					.createAlias("dgResultEntryHd", "hdid")
					.createAlias("dgMasInvestigation", "dmi")
					//.add(Restrictions.eq("hdid.hospitalId", hospitalId))    as per vinay sir
					.add(Restrictions.eq("dmi.mainChargecodeId", new Long(1)))
					.addOrder(Order.desc("hdid.resultDate"));
			
			if((serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) || (patientName != null && !patientName.equals("") && !patientName.equals("null")) || (mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null"))){
				criteria = criteria.createAlias("hdid.patient", "patient");
						//.createAlias("orderHd.patient", "patient");
			}
			if(serviceNo != null && !serviceNo.equals("") && !serviceNo.equals("null")) {				
				criteria = criteria.add(Restrictions.eq("patient.serviceNo", serviceNo).ignoreCase());
			}
			 
			if(patientName != null && !patientName.equals("") && !patientName.equals("null")) {
				String pName = "%"+patientName+"%";
				criteria = criteria.add(Restrictions.ilike("patient.patientName", pName));
			}
			
			if(mobileNo != null && !mobileNo.equals("") && !mobileNo.equals("null")) {				
				criteria = criteria.add(Restrictions.eq("patient.mobileNumber", mobileNo));
			}
			if (jsonData.get("from_date") != null && !jsonData.get("from_date").equals("")) {
				System.out.println("from date is "+jsonData.get("from_date"));
				fromDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonData.get("from_date")));
				criteria = criteria.add(Restrictions.ge("hdid.resultDate", fromDate));
			}
			if (jsonData.get("to_date") != null && !jsonData.get("to_date").equals("")) {
				System.out.println("to date is "+jsonData.get("to_date"));
				toDate = HMSUtil.convertStringTypeDateToDateType(String.valueOf(jsonData.get("to_date")));
				Calendar c = Calendar.getInstance(); 
				c.setTime(toDate);  
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				criteria = criteria.add(Restrictions.lt("hdid.resultDate", toDate));
			}
			if(jsonData.get("investigationName") != null && !jsonData.get("investigationName").equals("") && !jsonData.get("investigationName").equals("null")) {
				criteria = criteria.add(Restrictions.eq("dmi.investigationName", investigationName).ignoreCase());
			}
			//criteria = criteria.addOrder(Order.asc("resultEntryDetailId"));
			
			List<DgResultEntryDt> list = criteria.list();
			count = list.size();
			
			criteria = criteria.setFirstResult(pagingSize * (pageNo - 1));
			criteria = criteria.setMaxResults(pagingSize);
			list = criteria.list();
			
			map.put("count", count);
			map.put("list", list);
			
			return map;			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}


	@Override
	public Map<String, Object> saveDocumentData(HashMap<String, Object> inputJson) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String,Object> map = new HashMap<>();
		Transaction tx = session.beginTransaction();
		String result = "";
		try {
			
			System.out.println("Document Id after uploading to RIDC SERVER inside RadiologyDaoImpl.java"+String.valueOf(inputJson.get("documentId")));
			
			System.out.println("Inside Save in RIDC Upload table docId"+inputJson.get("documentId"));
			System.out.println("Inside Save in RIDC Upload table doc NAme"+inputJson.get("documentName"));
			
			System.out.println("Inside Save in RIDC Upload table docFormat"+inputJson.get("documentFormat"));
			System.out.println("Inside Save in RIDC Upload table doc url"+inputJson.get("documentUrl"));
			String documentId = "";
			if(inputJson.get("alfrescoId")!=null)
			  documentId = String.valueOf(inputJson.get("alfrescoId"));
			String documentName ="";
			if(inputJson.get("documentName")!=null)
			  documentName = String.valueOf(inputJson.get("documentName"));
			String encryptedName ="";
			if(inputJson.get("encryptedName")!=null)
			  encryptedName = String.valueOf(inputJson.get("encryptedName"));
			String documentFormat = "";
			if(inputJson.get("documentFormat")!=null)
			  documentFormat = String.valueOf(inputJson.get("documentFormat"));
			String documentUrl = "";
			if(inputJson.get("documentUrl")!=null)
			  documentUrl = String.valueOf(inputJson.get("documentUrl"));
			
			RidcEntity ridcUpload = new RidcEntity();
			//ridcUpload.setDocumentId(documentName);
			ridcUpload.setAlfrescoId(documentId);
			ridcUpload.setDocumentFormat(documentFormat);
			ridcUpload.setDocumentName(documentName);
			ridcUpload.setEncryptedName(encryptedName);	
			ridcUpload.setDocumentUrl(documentUrl);
			String ridcId = String.valueOf(session.save(ridcUpload));
			tx.commit();
			result = "success";		
			map.put("result", result);
			map.put("ridcId", ridcId);
		}catch(Exception ex) {
			result = "error";
			map.put("result", result);
			map.put("ridcId", 0);
			tx.rollback();
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	}

}
