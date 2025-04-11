package com.mmu.services.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mmu.services.dao.MedicalExamDAO;
import com.mmu.services.dao.SystemAdminDao;
import com.mmu.services.entity.MasDesignation;
import com.mmu.services.entity.MasDesignationMapping;
import com.mmu.services.entity.MasEmployee;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasRank;
import com.mmu.services.entity.MasRelation;
import com.mmu.services.entity.MasRole;
import com.mmu.services.entity.MasUnit;
import com.mmu.services.entity.OpdDisposalDetail;
import com.mmu.services.entity.Patient;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
@Repository
public class SystemAdminDaoImpl implements SystemAdminDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();
	final String vu_mas_miunit=databaseScema+"."+"vu_mas_miunit";
	final String vu_mas_unit=databaseScema+"."+"vu_mas_unit";
	@Autowired 
	GetHibernateUtils getHibernateUtils;
	@Autowired 
	MedicalExamDAO medicalExamDAO;
	@SuppressWarnings("unchecked")
	@Override
	public List<MasHospital> getMasHospitalListForAdmin() {
		List<MasHospital> hospitalList = new ArrayList<MasHospital>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria criteria  = session.createCriteria(MasHospital.class).createAlias("masUnit","masUnit");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("masUnit.unitId").as("hospitalId"));
			projectionList.add(Projections.property("hospitalName").as("hospitalName"));
			projectionList.add(Projections.property("masUnit.unitCode").as("unitCode"));
			projectionList.add(Projections.property("hospitalId").as("hospitalIdForU"));
			criteria.setProjection(projectionList);
			
			criteria.add(Restrictions.eq("status","y").ignoreCase());
			criteria.addOrder(Order.asc("hospitalName")).list();
			List<Object[]> listObject=criteria.list();
			if (listObject != null) {
					for (Iterator<?> it = listObject.iterator(); it.hasNext();) {
						MasHospital masHospital=new MasHospital();
						
						MasUnit masUnit=null;
						masUnit=new MasUnit();
						Object[] row = (Object[]) it.next();
						if(row[0]!=null) {
							//masHospital.setHospitalId((long) row[0]);
							masUnit.setUnitId((long) row[0]);
							masHospital.setMasUnit(masUnit);
						}
						if(row[1]!=null) {
							masHospital.setHospitalName(row[1].toString());
							
						}
						if(row[2]!=null) {
							
							masUnit.setUnitCode(row[2].toString());
							masHospital.setMasUnit(masUnit);
						}
						if(row[3]!=null) {
							masHospital.setHospitalId(Long.parseLong(row[3].toString()));
							
						}
						hospitalList.add(masHospital);
					}
					
				}
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return hospitalList;
	}
	
	////////////////////////////////////////////////////////////////
	
	@Override
	@Transactional
	public List<Object[]> getUnitListForAdminByUnitode(HashMap<String, Object> jsondata) {
		 List<Object[]> listObject=null;
		try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 StringBuilder sbQuery = new StringBuilder();
		 String unitCodeId= jsondata.get("unitCodeVal").toString();
		    sbQuery.append(" select  UNIT_ID,unit_code CENTITY ,unit_name DESCR ,MI_UNIT " );
		    sbQuery.append("  from " +vu_mas_unit); 
		    sbQuery.append(" where  mi_unit=:miUnit ORDER BY DESCR ASC ");
		    SQLQuery  query = session.createSQLQuery(sbQuery.toString());

		    query.setParameter("miUnit", unitCodeId);
		    
		     listObject = query.list();
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
		return listObject;
	}

	
	/////////////////////////////////////////////////////////////
	
	
	
	 
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MasEmployee> getMasEmployeeForAdmin(String serviceNo) {
		List<MasEmployee> listMasEmployee = new ArrayList<MasEmployee>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			listMasEmployee = session.createCriteria(MasEmployee.class)
					.add(Restrictions.eq("serviceNo",serviceNo).ignoreCase()).addOrder(Order.asc("employeeName"))
					//.add(Restrictions.eq("status","y").ignoreCase())
					 .list();
			
		}catch (Exception e) {
		//	getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			//getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listMasEmployee;
	}
	
@SuppressWarnings("unchecked")
@Override
	public List<MasDesignation> getAllMasDesigation() {
		List<MasDesignation> listMasDesignation = null;
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			listMasDesignation = session.createCriteria(MasDesignation.class)
					.add(Restrictions.eq("status","y").ignoreCase()).addOrder(Order.asc("designationName")).
					 list();
			
		}catch (Exception e) {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			e.printStackTrace();
		}finally {
			getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return listMasDesignation;
	}


@SuppressWarnings("unchecked")
@Override
public List<Users> getUserByServiceNoAndHospital(String serviceNo,Long hospitalId) {
	List<Users> listUsers = null;
	try {
		//hospitalId:this is unitCode 
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listUsers = session.createCriteria(Users.class)
				 .add(Restrictions.eq("serviceNo",serviceNo).ignoreCase()).addOrder(Order.asc("firstName")).
				 list();
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listUsers;
}



@Override
public Long saveOrUsers(Users users) {
	Session session=null;
	Long userId=null;
	Transaction tx=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		tx=session.beginTransaction();
		session.saveOrUpdate(users);
		userId=users.getUserId();
		session.flush();
        session.clear();
		tx.commit();
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		 
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
	return userId;
}

@SuppressWarnings("unchecked")
@Override
public Map<String,Object>  getAllUsers(Integer pageNo ,String serviceNumber) {
	 Map<String,Object> map=new HashMap<>();
	List<Users> listUsers = null;
	int count=0;
	try {
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Users.class)
				.add(Restrictions.eq("adminFlag","S").ignoreCase());
		if(StringUtils.isNotEmpty(serviceNumber)) {
			cr.add(Restrictions.eq("serviceNo", serviceNumber).ignoreCase());
		}
		listUsers=cr.list();
		count = listUsers.size();
		
		cr = cr.setFirstResult(pagingSize * (pageNo - 1));
		cr = cr.setMaxResults(pagingSize);
		listUsers = cr.list();
		map.put("count", count);
		map.put("list", listUsers);
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return map;
}
@SuppressWarnings("unchecked")
@Override
public Users getUserbyUserId(Long userId) {
	List<Users> listUsers = null;
	Users users=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listUsers = session.createCriteria(Users.class)
				 .add(Restrictions.eq("userId",userId)).addOrder(Order.asc("userName")).
				 list();
		if(CollectionUtils.isNotEmpty(listUsers)) {
			users=listUsers.get(0);
		}
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return users;
}

@Override
public Long activateDeActivatUser(Users users,String status) {
	Session session=null;
	Long userId=null;
	Transaction tx=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		tx=session.beginTransaction();
		//users.setStatus(status);
		session.saveOrUpdate(users);
		userId=users.getUserId();
		session.flush();
        session.clear();
		tx.commit();
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		 
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
	return userId;
}

@SuppressWarnings("unchecked")
@Override
public MasDesignationMapping getMassDesibyDesiId(Long designationId,Long unitId) {
	List<MasDesignationMapping> listmasDesignationMapping = null;
	MasDesignationMapping masDesignationMapping=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listmasDesignationMapping = session.createCriteria(MasDesignationMapping.class)
				 .add(Restrictions.eq("designationId",designationId)).add(Restrictions.eq("unitId",unitId)).
				 list();
		if(CollectionUtils.isNotEmpty(listmasDesignationMapping)) {
			masDesignationMapping=listmasDesignationMapping.get(0);
		}
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return masDesignationMapping;
}

@SuppressWarnings("unchecked")
@Override
public MasDesignationMapping getMassDesiByMassDesignationMappingId(Long masDesgiId ) {
	List<MasDesignationMapping> listmasDesignationMapping = null;
	MasDesignationMapping masDesignationMapping=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listmasDesignationMapping = session.createCriteria(MasDesignationMapping.class)
				 .add(Restrictions.eq("id",masDesgiId)). 
				 list();
		if(CollectionUtils.isNotEmpty(listmasDesignationMapping)) {
			masDesignationMapping=listmasDesignationMapping.get(0);
		}
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return masDesignationMapping;
}


@Override
public Long saveAndUpdateMasDesignation(MasDesignationMapping masDesignationMapping) {
	Session session=null;
	Long masId=null;
	Transaction tx=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		tx=session.beginTransaction();
		session.saveOrUpdate(masDesignationMapping);
		masId=masDesignationMapping.getId();
		session.flush();
        session.clear();
		tx.commit();
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		 
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
	return masId;
}
@SuppressWarnings("unchecked")
@Override
public  Map<String,Object> getAllMassDesignation(Integer pageNo) {
	List<MasDesignationMapping> listmasDesignationMapping = null;
	 Map<String,Object> map=new HashMap<>();
	 int count=0;
	try {
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		 Criteria cr = session.createCriteria(MasDesignationMapping.class);
		 listmasDesignationMapping=cr.list();
		 if(CollectionUtils.isNotEmpty(listmasDesignationMapping))
			 count = listmasDesignationMapping.size();
		
		cr = cr.setFirstResult(pagingSize * (pageNo - 1));
		cr = cr.setMaxResults(pagingSize);
		
		listmasDesignationMapping = cr.list();
		map.put("count", count);
		map.put("listmasDesignationMapping", listmasDesignationMapping);		 
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return map;
}

@SuppressWarnings("unchecked")
@Override
public List< MasDesignationMapping> getMassDesignationIdMasId(Long masId) {
	 List<MasDesignationMapping> listMasDesignationMapping = null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasDesignationMapping =   session.createCriteria(MasDesignationMapping.class).add(Restrictions.eq("id",masId)) .list();
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listMasDesignationMapping;
}


@SuppressWarnings("unchecked")
@Override
public List<MasDesignationMapping> getMassDesiByUnitId(Long unitId) {
	List<MasDesignationMapping> listmasDesignationMapping = null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listmasDesignationMapping = session.createCriteria(MasDesignationMapping.class)
				 .add(Restrictions.eq("unitId",unitId)).
				 list();
		 
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listmasDesignationMapping;
}

@SuppressWarnings("unchecked")
@Override
public String getMasDesignationByDesignationId(String masDesigtionId) {
	List<MasDesignation> listMasDesignation = null;
	String designationName="";
	String designationId="";
	
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String [] massDesignationArray=masDesigtionId.split(",");
		List<Long>listMasssDesi=new ArrayList<>();
		for(String ss:massDesignationArray) {
			listMasssDesi.add(Long.parseLong(ss.trim()));
		}
		listMasDesignation = session.createCriteria(MasDesignation.class) .add(Restrictions.in("designationId",listMasssDesi)).
				 list();
		if(CollectionUtils.isNotEmpty(listMasDesignation)) {
			int count=0;
			for(MasDesignation masDesignation:listMasDesignation) {
				if(count==0) {
					designationName=masDesignation.getDesignationName();
					designationId=""+masDesignation.getDesignationId();
				}
				else {
					designationName+=","+masDesignation.getDesignationName();
					designationId+=","+masDesignation.getDesignationId();
				}
				count++;
			}
		}
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return designationName+"##"+designationId;
}


 
@SuppressWarnings("unchecked")
@Override
public List< MasRole> getMasRole(HashMap<String, Object> jsondata) {
	 List<MasRole> listMasRole = null;
	 Criteria cr1=null;
	 Criterion co1=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		cr1 =   session.createCriteria(MasRole.class);
		if(jsondata!=null && jsondata.get("adminFlgValue")!=null && jsondata.get("adminFlgValue").toString().equalsIgnoreCase("U")) {
			co1=Restrictions.ne("roleName","SYSTEM ADMIN").ignoreCase();
			cr1.add(co1);
			cr1.addOrder(Order.asc("roleName")) ;
		}
	else {
		cr1.addOrder(Order.asc("roleName")) ;
	}
				
		listMasRole=cr1.list();
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listMasRole;
}
@Override
public List< MasRole> getMasRole() {
	 List<MasRole> listMasRole = null;
	 Criteria cr1=null;
	 Criterion co1=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		cr1 =   session.createCriteria(MasRole.class).addOrder(Order.asc("roleName")) ;
		 
		listMasRole=cr1.list();
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listMasRole;
}

@SuppressWarnings("unchecked")
@Override
public  Map<String,Object> getAllUsers(Long unitId,Long userId,Integer pageNo,String serviceNumber) {
	List<Users> listUsers = null;
	 Map<String,Object> map=new HashMap<>();
		int count=0; 
	try {
		int pagingSize = Integer.parseInt(HMSUtil.getProperties("adt.properties", "pageSize").trim());
		MasHospital masHospital=new MasHospital();
		masHospital.setHospitalId(unitId);
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cri = session.createCriteria(Users.class)
				 .add(Restrictions.eq("masHospital",masHospital));
		 //Criterion c1=Restrictions.eq("adminFlag","U").ignoreCase();
		 /* Criterion c2=Restrictions.eq("userId",userId);
		 LogicalExpression andExp = Restrictions.or(c1, c2);
		 cri.add(andExp);*/
		 //cri.add(c1);
		if(StringUtils.isNotEmpty(serviceNumber)) {
			cri.add(Restrictions.eq("serviceNo", serviceNumber).ignoreCase());
		}
		listUsers=cri.list();
		count = listUsers.size();
		
		cri = cri.setFirstResult(pagingSize * (pageNo - 1));
		cri = cri.setMaxResults(pagingSize);
		listUsers = cri.list();
		map.put("count", count);
		map.put("list", listUsers);
		
		 
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return map;
}


@SuppressWarnings("unchecked")
@Override
public String getMasRoleByRoleId(String masRoles) {
	List<MasRole> listMasRole = null;
	String roleName="";
	String rolessId="";
	try {
		if(StringUtils.isNotEmpty(masRoles)) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String [] massRolesArray=masRoles.split(",");
		List<Long>listRoles=new ArrayList<>();
		if(massRolesArray!=null)
		for(String ss:massRolesArray) {
			listRoles.add(Long.parseLong(ss.trim()));
		}
		listMasRole = session.createCriteria(MasRole.class) .add(Restrictions.in("roleId",listRoles)).
				 list();
		if(CollectionUtils.isNotEmpty(listMasRole)) {
			int count=0;
			for(MasRole masRole:listMasRole) {
				if(count==0) {
					roleName=masRole.getRoleName();
					rolessId=""+masRole.getRoleId();
				}
				else {
					roleName+=","+masRole.getRoleName();
					rolessId+=","+masRole.getRoleId();
				}
				count++;
			}
		}
		}
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return roleName+"##"+rolessId;
}

@Override
public Users getUsersByUserId(Long userId) {
	 Users user = null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		user = (Users) session.createCriteria(Users.class)
				 .add(Restrictions.eq("userId",userId)).uniqueResult();
				// list();
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return user;
}


@SuppressWarnings("unchecked")
@Override
public  MasRole getMasRoleOfRoleName() {
	 List<MasRole> listMasRole = null;
	 MasRole masRole=null;
	 try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasRole =   session.createCriteria(MasRole.class).add(Restrictions.eq("roleName","UNIT ADMIN").ignoreCase()).add(Restrictions.eq("status","y").ignoreCase())
				.addOrder(Order.asc("roleName")) .list();
		 if(CollectionUtils.isNotEmpty(listMasRole)) {
			 masRole=listMasRole.get(0);
		 }
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return masRole;
}



@SuppressWarnings("unchecked")
@Override
public List<MasEmployee> getMasEmployeeForAdmin(String serviceNo,MasHospital masHospital,String flag) {
	List<MasEmployee> listMasEmployee = new ArrayList<MasEmployee>();
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		MasUnit masUnit=null;
		if(flag.equalsIgnoreCase("u")) {
			masUnit=getMasHospitalByHospitalId(masHospital.getHospitalId());
		}
		if(flag.equalsIgnoreCase("s")) {
		  masUnit=getMasUnitByUnitId(masHospital.getHospitalId());
		}
		Criterion cro=null;
		cro=Restrictions.eq("masUnit",masUnit.getUnitCode());
		
		Criteria cr = session.createCriteria(MasEmployee.class)
				.add(Restrictions.eq("serviceNo",serviceNo).ignoreCase());
				if(flag.equalsIgnoreCase("s")) {
					cr.add(cro);
				}
				//.add(Restrictions.eq("status","y").ignoreCase())
				cr.addOrder(Order.asc("employeeName"));
		 listMasEmployee= cr.list();
		
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listMasEmployee;
}



@SuppressWarnings("unchecked")
@Override
public MasDesignationMapping getMasDesignationMappingForUnitId(Long unitId) {
	 List<MasDesignationMapping> listMasDesignationMapping = null;
	 MasDesignationMapping masDesignationMapping=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasDesignationMapping =  session.createCriteria(MasDesignationMapping.class)
				 .add(Restrictions.eq("unitId",unitId)).
				list();
		if(CollectionUtils.isNotEmpty(listMasDesignationMapping)) {
			masDesignationMapping=listMasDesignationMapping.get(0);
		}
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return masDesignationMapping;
}


@SuppressWarnings("unchecked")
@Override
public String getMasDesignationByDesignationIdNotAvial(String masDesigtionId,String masDesignationOfUser) {
	List<MasDesignation> listMasDesignation = null;
	String designationName="";
	String designationId="";
	
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		String [] massDesignationArray=masDesigtionId.split(",");
		List<Long>listMasssDesi=new ArrayList<>();
		if(massDesignationArray!=null)
		for(String ss:massDesignationArray) {
			listMasssDesi.add(Long.parseLong(ss.trim()));
		}
		String [] massDesignationArrayUser=null;
		List<Long>listMasssDesiUser=null;
		if(StringUtils.isNotBlank(masDesignationOfUser)) {
		  massDesignationArrayUser=masDesignationOfUser.split(",");
		 listMasssDesiUser=new ArrayList<>();
		if(massDesignationArrayUser!=null)
		for(String ss:massDesignationArrayUser) {
			listMasssDesiUser.add(Long.parseLong(ss.trim()));
		}
		
		}
		if(CollectionUtils.isNotEmpty(listMasssDesiUser))
		listMasssDesi.removeAll(listMasssDesiUser);
		
		
		if(CollectionUtils.isNotEmpty(listMasssDesi))
		listMasDesignation = session.createCriteria(MasDesignation.class) .add(Restrictions.in("designationId",listMasssDesi)).
				 list();
		if(CollectionUtils.isNotEmpty(listMasDesignation)) {
			int count=0;
			for(MasDesignation masDesignation:listMasDesignation) {
				if(count==0) {
					designationName=masDesignation.getDesignationName();
					designationId=""+masDesignation.getDesignationId();
				}
				else {
					designationName+=","+masDesignation.getDesignationName();
					designationId+=","+masDesignation.getDesignationId();
				}
				count++;
			}
		}
		 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return designationName+"##"+designationId;
}


@SuppressWarnings("unchecked")
@Override
public String getMasRoleByRoleIdForMapping(String masRoles) {
	List<MasRole> listMasRole = null;
	String roleName="";
	String rolessId="";
	try {
		if(StringUtils.isNotEmpty(masRoles)) {
		String [] massRolesArray=masRoles.split(",");
		List<Long>listRoles=new ArrayList<>();
		if(massRolesArray!=null)
		for(String ss:massRolesArray) {
			listRoles.add(Long.parseLong(ss.trim()));
		}
		
		List<MasRole>listMasRoleValue=getMasRole();
		List<Long>listMasRoleIds=new ArrayList<>();
		for(MasRole mm:listMasRoleValue) {
			listMasRoleIds.add(mm.getRoleId());
		}
		listMasRoleIds.removeAll(listRoles);
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();

		listMasRole = session.createCriteria(MasRole.class) .add(Restrictions.in("roleId",listMasRoleIds)).
				 list();
		if(CollectionUtils.isNotEmpty(listMasRole)) {
			int count=0;
			for(MasRole masRole:listMasRole) {
				if(count==0) {
					roleName=masRole.getRoleName();
					rolessId=""+masRole.getRoleId();
				}
				else {
					roleName+=","+masRole.getRoleName();
					rolessId+=","+masRole.getRoleId();
				}
				count++;
			}
		}
		}
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return roleName+"##"+rolessId;
}

public MasUnit getMasUnitByUnitId(Long unitId) {
	MasUnit masUnit=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		masUnit =  (MasUnit) session.createCriteria(MasUnit.class)
				 .add(Restrictions.eq("unitId",unitId)).uniqueResult();
				 
	 
	}catch (Exception e) {
	//	getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	} 
	return masUnit;
}
@Override
public MasUnit getMasHospitalByHospitalId(Long hospitalId) {
	List<MasHospital> listMasHospital=null;
	MasUnit masUnit=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasHospital =    session.createCriteria(MasHospital.class)
				 .add(Restrictions.eq("hospitalId",hospitalId)).list();
		if(CollectionUtils.isNotEmpty(listMasHospital)) {
			masUnit=listMasHospital.get(0).getMasUnit();
		}		 
	 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace(); 
	} 
	return masUnit;
}

@Override
public List<MasHospital> getMasHospitalByUnitId(Long unitId) {
	List<MasHospital> listMasHospital=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasHospital =    session.createCriteria(MasHospital.class)
				 .add(Restrictions.eq("masUnit.unitId",unitId)).addOrder(Order.asc("hospitalName")).list();
				 
	 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	} 
	return listMasHospital;
}


@Override
public List<MasEmployee> getAllServiceByUnitId(HashMap<String, Object> jsondata) {
	List<MasEmployee> listMasEmployee=null;
	try {
		if(jsondata.get("unitId")!=null) {
			MasUnit masUnit=null;
			if(jsondata.get("flag")!=null && jsondata.get("flag").toString().equalsIgnoreCase("u") ||  jsondata.get("flag").toString().equalsIgnoreCase("us")) {
				if(jsondata.get("unitId")!=null && jsondata.get("unitId")!="") {
				masUnit=getMasHospitalByHospitalId(Long.parseLong(jsondata.get("unitId").toString()));
				if(jsondata.get("flag").toString().equalsIgnoreCase("us")) {
					 masUnit=getMasUnitByUnitId(Long.parseLong(jsondata.get("unitId").toString()));
				}
				}
			}
			else {
		  masUnit=getMasUnitByUnitId(Long.parseLong(jsondata.get("unitId").toString()));
			}
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		
		listMasEmployee =    session.createCriteria(MasEmployee.class)
				 .add(Restrictions.eq("masUnit",masUnit.getUnitCode())).addOrder(Order.asc("employeeName")).list();
		}		 
	 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	} 
	return listMasEmployee;
 

}

@Override
public Users getUsersByUserIdAndHospitalId(Long userId,Long hospital) {
	 Users user = null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		MasHospital masHospital=new MasHospital();
		masHospital.setHospitalId(hospital);
		user = (Users) session.createCriteria(Users.class)
				 .add(Restrictions.eq("userId",userId)).add(Restrictions.eq("masHospital",masHospital)).uniqueResult();
				// list();
		
	}catch (Exception e) {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return user;
}
@Override
public MasRank getRankByRankCode(String rankCode) {
	 List<MasRank>listMasRank   = null;
	 MasRank masRank=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cri =   session.createCriteria(MasRank.class)
				 .add(Restrictions.eq("rankCode",rankCode));
		listMasRank	= cri.list();
		if(CollectionUtils.isNotEmpty(listMasRank)) {
			masRank	=listMasRank.get(0);
		}
		
	}catch (Exception e) {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return masRank;
}


@Override
public  List<OpdDisposalDetail> getOpdDisposalDetailList(Long visitId) {
	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	Criteria cr = session.createCriteria(OpdDisposalDetail.class);
	cr.add(Restrictions.eq("visitId", visitId));

	ProjectionList projectionList = Projections.projectionList();
	projectionList.add(Projections.property("disposalDetailsId").as("disposalDetailsId"));
	projectionList.add(Projections.property("disposalDays").as("disposalDays"));
	projectionList.add(Projections.property("disposalId").as("disposalId"));

	cr.setProjection(projectionList);
	 List<OpdDisposalDetail> list = cr.setResultTransformer(new AliasToBeanResultTransformer(OpdDisposalDetail.class)).list();
	getHibernateUtils.getHibernateUtlis().CloseConnection();
	return list;
}
@Override
public OpdDisposalDetail getOpdDisposalDetailObj(Long disposalDetailsId) {
	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	return (OpdDisposalDetail) session.createCriteria(OpdDisposalDetail.class)
	.add(Restrictions.eq("disposalDetailsId", disposalDetailsId)).uniqueResult();
}

@Override
public List<OpdDisposalDetail> getOpdDisposalDetailListForVisitId(Long visitID,Long patientId,Long disposalId){
	Criterion cri1=Restrictions.eq("visitId", visitID);
	Criterion cri2=Restrictions.eq("patientId",  patientId);
	Criterion cri3=Restrictions.eq("disposalId", disposalId);
	List<OpdDisposalDetail>listOpdDisposalDetail=null;
	
	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
	Criteria cr = session.createCriteria(OpdDisposalDetail.class);
	cr.add(cri1 );
	cr.add(cri2 );
	cr.add(cri3 );
	listOpdDisposalDetail=cr.list();
	return listOpdDisposalDetail;
}

@Override
public void saveOrUpdateOpdDisposalDetail(OpdDisposalDetail opdDisposalDetail){
	Session session=null;
	Transaction tx=null;
	try{
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		tx=session.beginTransaction();
		session.saveOrUpdate(opdDisposalDetail);
		session.flush();
        session.clear();
		tx.commit();
		
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		 
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		
		}
	 
}



@Transactional
@Override
public void saveOrUpdateOpdDisposalDetailForOpd(OpdDisposalDetail opdDisposalDetail){
	Session session=null;
	 
		session= getHibernateUtils.getHibernateUtlis().OpenSession();
		 
		session.saveOrUpdate(opdDisposalDetail);
		session.flush();
        session.clear();
	 
}


@Override
public List<MasHospital> getMasHospitalByUnitCode(String unitCode) {
	List<MasHospital> listMasHospital=null;
	try {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		listMasHospital =    session.createCriteria(MasHospital.class).createAlias("masUnit", "masUnit")
				 .add(Restrictions.eq("masUnit.unitCode",unitCode)).list();
				 
	 
	}catch (Exception e) {
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	} 
	return listMasHospital;
}

/*@SuppressWarnings("unchecked")
@Override
public List<Patient> getPatientForAdmin(String serviceNo) {
	List<Patient> listPatient = null;
	try {
		String ralationNameVal = HMSUtil.getProperties("js_messages_en.properties", "relationName");
		//MasRelation masRelation=medicalExamDAO.checkRelationByName(ralationNameVal.trim());
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Long relationId=null;
		if(masRelation!=null) {
		  relationId=masRelation.getRelationId();
	
		listPatient =   session.createCriteria(Patient.class)
				.add(Restrictions.and(Restrictions.eq("serviceNo", serviceNo).ignoreCase(),Restrictions.eq("relationId", relationId))).list();
				
		}
	}catch (Exception e) {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
		e.printStackTrace();
	}finally {
		//getHibernateUtils.getHibernateUtlis().CloseConnection();
	}
	return listPatient;
}*/
}
