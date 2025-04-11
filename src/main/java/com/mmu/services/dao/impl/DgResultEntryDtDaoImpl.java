package com.mmu.services.dao.impl;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.DgResultEntryDtDao;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgResultEntryDt;
import com.mmu.services.hibernateutils.GetHibernateUtils;
@Repository
@Transactional
public class DgResultEntryDtDaoImpl extends GenericDaoImpl<DgResultEntryDt, Long>  implements DgResultEntryDtDao {
	@Autowired
	GetHibernateUtils getHibernateUtils;
	@Override
	public Long saveOrUpdateDgResultEntryDtInv(DgResultEntryDt dgResultEntryDt) {
		Session session=null;
		Long dgResultEntryDetailId=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			session.saveOrUpdate(dgResultEntryDt);
			dgResultEntryDetailId=dgResultEntryDt.getResultEntryDetailId();
			session.flush();
	        session.clear();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		 
		return dgResultEntryDetailId;
	} 
}
