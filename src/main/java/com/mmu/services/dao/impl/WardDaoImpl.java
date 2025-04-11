package com.mmu.services.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.WardDao;
import com.mmu.services.entity.MasDepartment;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;




@Repository
@Transactional
public class WardDaoImpl implements WardDao {
	
	@Autowired
	GetHibernateUtils getHibernateUtils;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasDepartment> getWardDepartment(Map<String, Object> inputJson) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Map<String,Object> map = new HashMap<>();
		try {
			String type = String.valueOf(HMSUtil.getProperties("adt.properties", "WARD_DEPARTMENT_TYPE").trim()); 
			
			List<MasDepartment> departmentList = session.createCriteria(MasDepartment.class)
			.createAlias("masDepartmentType", "mdt")
			.add(Restrictions.eq("mdt.departmentTypeCode", type)).list();
			
			return departmentList;
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return null;
	}
}
