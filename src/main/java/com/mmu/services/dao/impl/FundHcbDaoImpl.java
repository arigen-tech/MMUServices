package com.mmu.services.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.FundHcbDao;
import com.mmu.services.entity.CaptureInterestDt;
import com.mmu.services.entity.CaptureInterestHd;
import com.mmu.services.entity.FundHcb;
import com.mmu.services.entity.MasStoreFinancial;
import com.mmu.services.entity.Patient;
import com.mmu.services.hibernateutils.GetHibernateUtils;

@Repository
@Transactional
public class FundHcbDaoImpl implements FundHcbDao{

	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@Override
	public Long getHcbFund() {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundHcb.class);
			List<FundHcb> list = criteria.addOrder(Order.desc("fundHcbId")).list();
			
			return list.get(0).getHcbBalance();
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}

	@Override
	public FundHcb getFundHcb(Long upss_id,Long head_type_id,Date currentDate) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundHcb.class);
			Criterion upssIDExcpectaion= Restrictions.eq("upssId", upss_id);
			Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
			Criterion currentDateExpectation= Restrictions.eq("hcbDate", currentDate);
			Conjunction logicalAndExpression
            = Restrictions.and(upssIDExcpectaion,
            		headTypeExpectation,currentDateExpectation);
			criteria.add(logicalAndExpression);
			List<FundHcb> list = criteria.list();
			if(!list.isEmpty())
				return list.get(0);
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}

	@Override
	public FundHcb getFundHcb(Long upss_id,Long head_type_id) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundHcb.class);
			Criterion upssIDExcpectaion= Restrictions.eq("upssId", upss_id);
			Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
			LogicalExpression logicalAndExpression
            = Restrictions.and(upssIDExcpectaion,
            		headTypeExpectation);
			criteria.add(logicalAndExpression);
			List<FundHcb> list = criteria.addOrder(Order.desc("fundHcbId")).list();
			if(!list.isEmpty()) {
				return list.get(0);
			}
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}

	@Override
	public Long saveHcbFund(Long upss_id, Long head_type_id, Date currentDate, Long openingBalance, Long drCR,
			Long hcbBalance) {
	return null;

	}

	@Override
	public Long updateFundHcb(FundHcb fundHcb) {
		   Long result=(long) 1;
	        Transaction tx=null;
	        Session session=null;
	        try {
	            session = getHibernateUtils.getHibernateUtlis().OpenSession();
	            tx = session.beginTransaction();
	            
	            FundHcb fundHcbDetails = (FundHcb) session.get(FundHcb.class, fundHcb.getFundHcbId());
	           // fundHcbDetails.setOpeningBalance(fundHcb.getOpeningBalance());
	            fundHcbDetails.setDebitCredit(fundHcb.getDebitCredit());
	            fundHcbDetails.setHcbBalance(fundHcb.getHcbBalance());
	           
				 session.update(fundHcbDetails);
	            tx.commit();
	            session.flush();
	        }catch(Exception e) {
	            e.printStackTrace();
	            result = (long) 0;
	        }finally {
	            getHibernateUtils.getHibernateUtlis().CloseConnection();
	        }

	        return result;

	}

	@Override
	public Long saveFundHcb(FundHcb fundHcb) {
		Long result=null;
        Transaction tx=null;
        Session session=null;
        try {
        	
            session = getHibernateUtils.getHibernateUtlis().OpenSession();
            tx = session.beginTransaction();
            result =  (Long) session.save(fundHcb);
            tx.commit();
            session.flush();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            getHibernateUtils.getHibernateUtlis().CloseConnection();
        }

        return result;
	}
	
	@Override
	public Long getFundOpernationalBalance(Long upss_id,Long head_type_id) {
			FundHcb fundHcb = null;
			long availableBalance = 0;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			try {
				fundHcb = (FundHcb) session.createCriteria(FundHcb.class).add(Restrictions.eq("upssId", upss_id))
						.add(Restrictions.eq("headTypeId", head_type_id))
						.addOrder(Order.desc("fundHcbId"));
				if (fundHcb != null) {
					availableBalance = fundHcb.getHcbBalance();
				}

			} catch (Exception e) {
				getHibernateUtils.getHibernateUtlis().CloseConnection();
				e.printStackTrace();
			}
			return availableBalance;
		}
	
	@Override
	 public Map<String, Object> getFundOpernationalUpdatedBalance(Long upss_id,Long cityId,Long head_type_id,String phase) {
			Session session1 = getHibernateUtils.getHibernateUtlis().OpenSession();
			Map<String, Object> map = new HashMap<>();
			List<String> searchList = null;
			try {
				String Query = "select hcb_balance,opening_balance from fund_hcb where upss_id="+upss_id+" and city_id="+cityId+" and head_type_id="+head_type_id+" and phase='"+phase+"' order by fund_hcb_id desc limit 1";
				System.out.println(Query);
				if (Query != null) {
					searchList = session1.createSQLQuery(Query).list();
				} else {
					System.out.println("No Record Found");
				}
				map.put("list", searchList);
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
			return map;
		}
	
	@Override
	 
	public FundHcb getFundHcbCityLevel(Long upss_id,Long cityId,Long head_type_id,String phase) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundHcb.class);
			Criterion upssIDExcpectaion= Restrictions.eq("upssId", upss_id);
			Criterion cityIdExcpectaion= Restrictions.eq("cityId", cityId);
			Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
			Criterion headTypeExpectation1= Restrictions.eq("phase", phase);
			Conjunction logicalAndExpression
            = Restrictions.and(upssIDExcpectaion,cityIdExcpectaion,
            		headTypeExpectation,headTypeExpectation1);
			criteria.add(logicalAndExpression);
			List<FundHcb> list = criteria.addOrder(Order.desc("fundHcbId")).list();
			if(!list.isEmpty()) {
				return list.get(0);
			}
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}

	@Override
	public FundHcb getFundHcbCity(Long upss_id, Long cityId, Long head_type_id, Date currentDate,String phase) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(FundHcb.class);
			Criterion upssIDExcpectaion= Restrictions.eq("upssId", upss_id);
			Criterion cityIDExcpectaion= Restrictions.eq("cityId", cityId);
			Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
			Criterion currentDateExpectation= Restrictions.eq("hcbDate", currentDate);
			Criterion phaseExpectation= Restrictions.eq("phase", phase);
			Conjunction logicalAndExpression
            = Restrictions.and(upssIDExcpectaion,cityIDExcpectaion,
            		headTypeExpectation,currentDateExpectation,phaseExpectation);
			criteria.add(logicalAndExpression);
			List<FundHcb> list = criteria.addOrder(Order.desc("fundHcbId")).list();
			if(!list.isEmpty())
				return list.get(0);
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}

	
	 
	@Override
 	public Long getCaptureHdCityPhaseLevel(Long upss_id,Long cityId,Long head_type_id,String phase) {
		Long interest=0l;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(CaptureInterestDt.class).createAlias("captureInterestHd", "captureInterestHd");
			Criterion upssIDExcpectaion= Restrictions.eq("districtId", upss_id);
			Criterion cityIdExcpectaion= Restrictions.eq("cityId", cityId);
			Criterion headTypeExpectation= Restrictions.eq("headTypeId", head_type_id);
			//Criterion headTypeExpectation1=Restrictions.and(Restrictions.eq("captureInterestHd.phase", phase),Restrictions.eq("captureInterestHd.status", "A")
				//	,Restrictions.eq("captureInterestHd.financialId", financialId));
			Criterion headTypeExpectation1=Restrictions.and(Restrictions.eq("captureInterestHd.phase", phase),Restrictions.eq("captureInterestHd.status", "A"));
			Conjunction logicalAndExpression
            = Restrictions.and(upssIDExcpectaion,cityIdExcpectaion,
            		headTypeExpectation,headTypeExpectation1);
			criteria.add(logicalAndExpression);
			List<CaptureInterestDt> list = criteria.list();
			
			for (CaptureInterestDt captureInterestDt : list) {
				interest+=captureInterestDt.getInterest();
			}
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return interest;
	}

	@Override
	 
	public MasStoreFinancial getMasStoreFinancilaYear(String year) {
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria = session.createCriteria(MasStoreFinancial.class);
			Criterion upssIDExcpectaion= Restrictions.eq("financialYear", year);
		 	criteria.add(upssIDExcpectaion);
			List<MasStoreFinancial> list = criteria.list();
			if(!list.isEmpty())
				return list.get(0);
	}catch(Exception e) {
		
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
		return null;
	}


	
}
