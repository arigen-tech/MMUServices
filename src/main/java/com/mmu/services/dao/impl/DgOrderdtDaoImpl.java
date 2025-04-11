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

import com.mmu.services.dao.DgOrderdtDao;
import com.mmu.services.entity.DgOrderdt;
import com.mmu.services.entity.DgOrderhd;
import com.mmu.services.entity.DiverOrderDet;
import com.mmu.services.entity.Visit;
import com.mmu.services.hibernateutils.GetHibernateUtils;
@Repository
@Transactional
public class DgOrderdtDaoImpl extends GenericDaoImpl<DgOrderdt, Long>  implements DgOrderdtDao {
	@Autowired
	GetHibernateUtils getHibernateUtils;
	@Override
	public Long saveOrUpdateDgOrderdtInv(DgOrderdt dgOrderdt) {
		Session session=null;
		Long dgOrderdtId=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			session.saveOrUpdate(dgOrderdt);
			dgOrderdtId=dgOrderdt.getOrderdtId();
			session.flush();
	        session.clear();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return dgOrderdtId;
	} 
	
	
	@Override
	@Transactional
	public DgOrderdt getDgOrderdtByInvestigationIdAndOrderhdId(Long investigationId,Long orderhdId) {
		DgOrderdt dgOrderdt=null;
		List<DgOrderdt>listDgOrderdt=null;
		Session session=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DgOrderdt.class);
			Criterion cr1=Restrictions.eq("investigationId", investigationId);
			Criterion cr2=Restrictions.eq("orderhdId", orderhdId);
			//listDgOrderdt= findByCriteria(cr1,cr2);
			cr11.add(cr1).add(cr2);
			listDgOrderdt=cr11.list();
			if(CollectionUtils.isNotEmpty(listDgOrderdt)) {
				dgOrderdt=listDgOrderdt.get(0);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();  
		return dgOrderdt;
	}

	
	@Override
	@Transactional
	public DgOrderdt getDgOrderdtByOrderDtId(Long orderDtId ) {
		DgOrderdt dgOrderdt=null;
		List<DgOrderdt>listDgOrderdt=null;
		Session session=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DgOrderdt.class);
			Criterion cr1=Restrictions.eq("orderdtId", orderDtId);
			cr11.add(cr1) ;
			listDgOrderdt=cr11.list();
			if(CollectionUtils.isNotEmpty(listDgOrderdt)) {
				dgOrderdt=listDgOrderdt.get(0);
			}
			
		}
		catch(Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();  
		return dgOrderdt;
	}
	
	
	@Override
	@Transactional
	public List<DgOrderdt> getDgOrderHdtByVisitIdForDiver(Long visitId,Long patientId) {
		List<DgOrderdt>listDgOrderdt=null;
		Session session=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DgOrderdt.class,"dgOrderdt").createAlias("dgOrderHd", "dgOrderHd");
			Criterion cr1=Restrictions.eq("dgOrderHd.visitId", visitId);
			Criterion cr2=Restrictions.isNotNull("dgOrderdt.diverAction");
			 
			cr11.add(cr1) ;
			if(cr2!=null)
			cr11.add(cr2);
			listDgOrderdt=cr11.list();
			 
		}
		catch(Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection(); 
		return listDgOrderdt;
	}
	
	
	@Override
	@Transactional
	public DiverOrderDet getDiverDgOrderdtByOrderDtId(Long diverOrderDtId ) {
		DiverOrderDet diverOrderDet=null;
		List<DiverOrderDet>listDiverOrderDet=null;
		Session session=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DiverOrderDet.class);
			Criterion cr1=Restrictions.eq("orderDetId", diverOrderDtId);
			cr11.add(cr1) ;
			listDiverOrderDet=cr11.list();
			if(CollectionUtils.isNotEmpty(listDiverOrderDet)) {
				diverOrderDet=listDiverOrderDet.get(0);
			}
			
		}
		catch(Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}
		getHibernateUtils.getHibernateUtlis().CloseConnection();  
		return diverOrderDet;
	}
	
	@Override
	@Transactional
	public List<DiverOrderDet> getDiverDgOrderHdtByVisitIdForDiver(Long visitId,Long patientId) {
		List<DiverOrderDet>listDiverOrderDet=null;
		Session session=null;
		Criterion cr1=null;
		Criterion cr2=null;
		Criteria cr11=null;
		try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			  cr11=session.createCriteria(DiverOrderDet.class,"diverOrderdt");
			  cr1=Restrictions.eq("diverOrderdt.visitId", visitId);
			//Criterion cr2=Restrictions.isNotNull("dgOrderdt.diverAction");
			 
			cr11.add(cr1) ;
			//if(cr2!=null)
			//cr11.add(cr2);
			listDiverOrderDet=cr11.list();
			 if(CollectionUtils.isEmpty(listDiverOrderDet)) {
				 List<Visit>listVisit=null;
				 session= getHibernateUtils.getHibernateUtlis().OpenSession();
					  cr11=session.createCriteria(Visit.class,"diverOrderdt");
					  cr1=Restrictions.eq("diverOrderdt.patientId", patientId);
					 // cr2=Restrictions.eq("diverOrderdt.appointmentTypeId", 2l);
					  cr11.add(cr1) ;
					  //cr11.add(cr2) ;
					  
					  listVisit=cr11.list();
					  if(listVisit!=null && listVisit.size()>0) {
						  Visit visit=listVisit.get(0);
						  if(visit!=null) {
							  visitId=visit.getVisitId();
								session= getHibernateUtils.getHibernateUtlis().OpenSession();
								  cr11=session.createCriteria(DiverOrderDet.class,"diverOrderdt");
								  cr1=Restrictions.eq("diverOrderdt.visitId", visitId);
								//Criterion cr2=Restrictions.isNotNull("dgOrderdt.diverAction");
								 
								cr11.add(cr1) ;
								//if(cr2!=null)
								//cr11.add(cr2);
								listDiverOrderDet=cr11.list();
						  }
						  
					  }
			 }
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		  
		return listDiverOrderDet;
	}
	
	@Override
	@Transactional
	public List<DgOrderdt> getDgOrderdtByDiverId(Long orderDetId) {
		List<DgOrderdt>listDgOrderdt=null;
		Session session=null;
		//try{
			session= getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr11=session.createCriteria(DgOrderdt.class,"dgOrderDt");
			Criterion cr1=Restrictions.eq("dgOrderDt.orderDetId", orderDetId);
			 
			cr11.add(cr1) ;
			//if(cr2!=null)
			//cr11.add(cr2);
			listDgOrderdt=cr11.list();
	//	}
		/*catch(Exception e) {
			e.printStackTrace();
		}*/
			getHibernateUtils.getHibernateUtlis().CloseConnection();		
		return listDgOrderdt;
	}
	
	
	}
