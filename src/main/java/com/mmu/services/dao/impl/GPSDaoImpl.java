package com.mmu.services.dao.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.GPSDao;
import com.mmu.services.entity.MasCamp;
import com.mmu.services.entity.MasDistrict;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;

//import oracle.net.aso.e;

@Repository
@Transactional
public class GPSDaoImpl implements GPSDao {
	
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();

	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Override
	public Map<String, Object> getAllDistricts(HashMap<String, Object> jsondata) {


		Map<String, Object> map = new HashMap<String, Object>();
		List<MasCamp> campList = new ArrayList<MasCamp>();
		
		Long distId =  Long.parseLong(String.valueOf(jsondata.get("distId")));
		Criteria cr = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			SQLQuery  query  = session.createSQLQuery("select city_id from mas_city where district_id ="+distId ); 
			
			List<Object> cityId = query.list();
			List<Long> cityIds = new ArrayList<Long>();
			
			for(Object row : cityId){
				cityIds.add(new Long(row.toString()));
			}
			
			List<Long> mmuToIgnore = new ArrayList<Long>();
			mmuToIgnore.add(new Long(34));
			mmuToIgnore.add(new Long(39));
			mmuToIgnore.add(new Long(44));
			
			//System.out.println("cityIds---> "+cityIds);
			
			LocalDate today = LocalDate.now();
			Date campDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
			cr = session.createCriteria(MasCamp.class)
					    .createAlias("masCity", "masCity")
					    .createAlias("masMMU", "mmu")
					    .add(Restrictions.in("masCity.cityId", cityIds))
					    .add(Restrictions.eq("campDate", campDate))
						.add(Restrictions.not(Restrictions.in("mmu.mmuId", mmuToIgnore)))
						.add(Restrictions.eq("weeklyOff", "Camp"))
						.addOrder(Order.asc("mmu.mmuId"));
			
			campList = cr.list();
			
			map.put("campList", campList);
			
			cr = session.createCriteria(MasDistrict.class)
						.add(Restrictions.eq("districtId", distId));

			MasDistrict dist = (MasDistrict) cr.list().get(0);

			map.put("dist", dist);
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	
	
	}

	@Override
	public List<MasDistrict> getDistrictList(Map<String, Object> requestData) {

		List<MasDistrict> list = new ArrayList<MasDistrict>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			list = session.createCriteria(MasDistrict.class).add(Restrictions.eq("status", "Y").ignoreCase()).addOrder(Order.asc("districtName"))
					.list();

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	
	}

	@Override
	public List<Object[]> getCampDetails(LocalDate today, Long mmuId) {


		List<Object[]> campList = new ArrayList<Object[]>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			SQLQuery  query  = session.createSQLQuery("select start_time,end_time,location,landmark from mas_camp where camp_date='"+today+"' and mmu_id ="+mmuId ); 
			campList = query.list();
			return campList;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return campList;
	
	
	}

	@Override
	public List<Users> getUserDetails(String mmu) {


		List<Users> list = new ArrayList<Users>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			mmu = mmu.concat(",");
			//System.out.println("MMU iD ----> jk--> "+mmu);
			
			list = session.createCriteria(Users.class).add(Restrictions.eq("mmuId", mmu))
					                                  .add(Restrictions.eq("userFlag", new Long(1)))
					                                  .createAlias("employeeId", "employeeId")
					                                  .add(Restrictions.isNotNull("employeeId.employeeId"))
													  .list();
			//System.out.println("list size--> "+list.size());

			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}

		return list;
	
	
	}

	@Override
	public Map<String, Object> getCampInfoAllDistrict(HashMap<String, Object> jsondata) {



		Map<String, Object> map = new HashMap<String, Object>();
		List<MasCamp> campList = new ArrayList<MasCamp>();
		
		Criteria cr = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			LocalDate today = LocalDate.now();
			Date campDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
			cr = session.createCriteria(MasCamp.class)
					    .add(Restrictions.eq("campDate", campDate))
						.add(Restrictions.eq("weeklyOff", "Camp"));
			
			campList = cr.list();
			
			Map<Long, List<MasCamp>> campMap = new HashMap<Long, List<MasCamp>>();
			List<MasCamp> campListObj = null ;
			
			for(MasCamp campObj : campList) {
				Long distId = campObj.getMasCity().getMasDistrict().getDistrictId();
				if(campMap.containsKey(distId)){
					campListObj = campMap.get(distId);
				}else{
					campListObj = new ArrayList<MasCamp>();
				}
				campListObj.add(campObj);
				campMap.put(distId,campListObj);
				   
			}
			
			map.put("campMap", campMap);
			
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return map;
	
	
	
	}

}
