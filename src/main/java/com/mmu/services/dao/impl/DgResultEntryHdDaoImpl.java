package com.mmu.services.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgResultEntryHdDao;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DgResultEntryHd;
import com.mmu.services.hibernateutils.GetHibernateUtils;
@Repository
@Transactional
public class DgResultEntryHdDaoImpl extends GenericDaoImpl<DgResultEntryHd, Long>  implements DgResultEntryHdDao {
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	 
	@Override
	@Transactional
	public List<DgResultEntryHd>findByOrderIdAndVisitId(Long visitId,Long orderHdId){
		Criterion cr1=Restrictions.eq("visitId", visitId);
		Criterion cr2=Restrictions.eq("orderHdId", orderHdId);
		return  findByCriteria(cr1,cr2);
		
	}
	
	@Override
	@Transactional
	public List<DgResultEntryHd>findByOrderIdAndPatientId(Long patientId,Long orderHdId){
		Criterion cr1=Restrictions.eq("patientId", patientId);
		Criterion cr2=Restrictions.eq("orderHdId", orderHdId);
		return  findByCriteria(cr1,cr2);
		
	}
	
	@Override
	public Long saveOrUpdateDgResultEntryHdInv(DgResultEntryHd dgResultEntryHd) {
		Session session=null;
		Long dgResultEntryHdId=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			session.saveOrUpdate(dgResultEntryHd);
			dgResultEntryHdId=dgResultEntryHd.getResultEntryId();
			//session.flush();
	        //session.clear();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		 
		return dgResultEntryHdId;
	}

	@Override
	@Transactional
	public DgResultEntryHd getDgResultEntryHdByInvestigationIdAndOrderhdId(Long subChargecodeId,Long mainChargecodeId,Long dgOrderHdId) {
		DgResultEntryHd dgResultEntryHd=null;
		List<DgResultEntryHd>listDgResultEntryHd=null;
		Session session=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DgResultEntryHd.class);
			 
			
			Criterion cr1=Restrictions.eq("subChargecodeId", subChargecodeId);
			Criterion cr2=Restrictions.eq("mainChargecodeId",mainChargecodeId);
			Criterion cr3=Restrictions.eq("orderHdId",dgOrderHdId);
			
			//listDgOrderdt= findByCriteria(cr1,cr2);
			cr11.add(cr1).add(cr2) .add(cr2).add(cr3) ;
			listDgResultEntryHd=cr11.list();
			if(CollectionUtils.isNotEmpty(listDgResultEntryHd)) {
				dgResultEntryHd=listDgResultEntryHd.get(0);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		  
		return dgResultEntryHd;
	}
	
}
