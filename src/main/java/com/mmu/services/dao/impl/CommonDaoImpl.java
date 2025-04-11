package com.mmu.services.dao.impl;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.CommonDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.MasAdministrativeSex;
import com.mmu.services.entity.MasDocument;
import com.mmu.services.entity.PatientDocumentDetail;
import com.mmu.services.entity.RidcEntity;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.service.impl.OpdServiceImpl;

@Repository
@Transactional
public class CommonDaoImpl implements CommonDao {
    
	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;
	
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override
	 * 
	 * @org.springframework.transaction.annotation.Transactional public String
	 * getAdminSexCode(HashMap<String, Object> jsondata) { String genderCode ="";
	 * if(jsondata.get("genderId")!=null &&
	 * StringUtils.isNotEmpty(jsondata.get("genderId").toString())) { Long
	 * genderId=(Long.valueOf(String.valueOf(jsondata.get("genderId")))); Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * List<MasAdministrativeSex> listOfGender =
	 * session.createCriteria(MasAdministrativeSex.class)
	 * .add(Restrictions.eq("administrativeSexId", genderId)).list(); genderCode =
	 * listOfGender.get(0).getAdministrativeSexCode(); } return genderCode; }
	 * 
	 * @Override
	 * 
	 * @org.springframework.transaction.annotation.Transactional public
	 * List<DgMasInvestigation>
	 * getDgMasInvestigationByInvestigationId(HashMap<String, Object> jsondata){
	 * Long
	 * investigationId=(Long.valueOf(String.valueOf(jsondata.get("investiongationId"
	 * )))); Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(DgMasInvestigation.class);
	 * cr.add(Restrictions.eq("investigationId", investigationId)); return
	 * cr.list(); }
	 * 
	 * @Override public List<MasDocument> getDocumentList(HashMap<String, Object>
	 * jsondata) { Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(MasDocument.class); cr.add(Restrictions.eq("status",
	 * "Y").ignoreCase()); ProjectionList projectionList =
	 * Projections.projectionList();
	 * projectionList.add(Projections.property("documentCode").as("documentCode"));
	 * projectionList.add(Projections.property("documentId").as("documentId"));
	 * projectionList.add(Projections.property("documentName").as("documentName"));
	 * cr.setProjection(projectionList); List<MasDocument> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(MasDocument.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list; }
	 * 
	 * @Override public List<PatientDocumentDetail>
	 * getPatientDocumentDetail(HashMap<String, Object> jsondata){ String
	 * visitId=(String) jsondata.get("visitId");
	 * visitId=OpdServiceImpl.getReplaceString(visitId);
	 * 
	 * 
	 * Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	 * Criteria cr = session.createCriteria(PatientDocumentDetail.class);
	 * cr.add(Restrictions.eq("visitId", Long.parseLong(visitId))); ProjectionList
	 * projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("patientDocumentId").as(
	 * "patientDocumentId"));
	 * projectionList.add(Projections.property("documentId").as("documentId"));
	 * projectionList.add(Projections.property("documentRemarks").as(
	 * "documentRemarks"));
	 * projectionList.add(Projections.property("ridcId").as("ridcId"));
	 * cr.setProjection(projectionList); List<PatientDocumentDetail> list =
	 * cr.setResultTransformer(new
	 * AliasToBeanResultTransformer(PatientDocumentDetail.class)).list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list;
	 * 
	 * }
	 * 
	 * @Override public List<RidcEntity> getRidcDocument(HashMap<String, String>
	 * jsondata) { Long ridcId = null;
	 * ridcId=(Long.valueOf(String.valueOf(jsondata.get("ridcId")))); Session
	 * session = getHibernateUtils.getHibernateUtlis().OpenSession(); Criteria cr =
	 * session.createCriteria(RidcEntity.class); cr.add(Restrictions.eq("ridcId",
	 * ridcId));
	 * 
	 * ProjectionList projectionList = Projections.projectionList();
	 * projectionList.add(Projections.property("alfrescoId").as("alfrescoId"));
	 * projectionList.add(Projections.property("documentName").as("documentName"));
	 * projectionList.add(Projections.property("documentFormat").as("documentFormat"
	 * )); //
	 * projectionList.add(Projections.property("encryptedName").as("encryptedName"))
	 * ;
	 * 
	 * cr.setProjection(projectionList);
	 * 
	 * @SuppressWarnings("unchecked") List<RidcEntity> list = cr.list();
	 * getHibernateUtils.getHibernateUtlis().CloseConnection(); return list;
	 * 
	 * }
	 */
}
