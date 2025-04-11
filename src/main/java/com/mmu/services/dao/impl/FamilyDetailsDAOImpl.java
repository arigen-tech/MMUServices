package com.mmu.services.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.FamilyDetailsDAO;
import com.mmu.services.entity.FamilyDetail;
import com.mmu.services.hibernateutils.GetHibernateUtils;

@Repository
@Transactional
public class FamilyDetailsDAOImpl implements FamilyDetailsDAO{

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Override
	@Transactional
	public List<FamilyDetail> getFamilyDetailsByVisitId(HashMap<String, Object> jsondata) {
		List<FamilyDetail> listFamilyDetails = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Long visitId=null;
			if(jsondata.get("visitId")!=null)
			  visitId=Long.parseLong(jsondata.get("visitId").toString());
			
			listFamilyDetails = session.createCriteria(FamilyDetail.class).add(Restrictions.eq("visitId", visitId))
					 .list();
		}catch(Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listFamilyDetails;
	}	
	@Override
	@Transactional
	public FamilyDetail getFamilyDetailsByFamilyId(Long familyId) {
		FamilyDetail familyDetails=null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 
			familyDetails = (FamilyDetail) session.createCriteria(FamilyDetail.class).add(Restrictions.eq("familyId", familyId)).uniqueResult();
		}catch(Exception e) {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return familyDetails;
	}

}
