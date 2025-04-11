package com.mmu.services.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mmu.services.dao.OpdMasterDao;
import com.mmu.services.entity.DgMasInvestigation;
import com.mmu.services.entity.HierarchyData;
import com.mmu.services.entity.MasDepartment;

import com.mmu.services.entity.MasEmpanelledHospital;
import com.mmu.services.entity.MasFrequency;
import com.mmu.services.entity.MasHospital;
import com.mmu.services.entity.MasIcd;
import com.mmu.services.entity.MasItemClass;
import com.mmu.services.entity.MasItemType;
import com.mmu.services.entity.MasMainChargecode;
import com.mmu.services.entity.MasNursingCare;
import com.mmu.services.entity.MasSpeciality;
import com.mmu.services.entity.MasStoreItem;
import com.mmu.services.entity.MasStoreSection;
import com.mmu.services.entity.MasStoreUnit;
import com.mmu.services.entity.MasTreatmentInstruction;
import com.mmu.services.entity.OpdPatientDetail;
import com.mmu.services.entity.OpdTemplate;
import com.mmu.services.entity.OpdTemplateInvestigation;
import com.mmu.services.entity.OpdTemplateMedicalAdvice;
import com.mmu.services.entity.OpdTemplateTreatment;
import com.mmu.services.entity.Users;
import com.mmu.services.hibernateutils.GetHibernateUtils;
import com.mmu.services.utils.HMSUtil;
import com.mmu.services.utils.ProjectUtils;

//import oracle.jdbc.OracleTypes;

@Repository
public class OpdMasterDaoImpl implements OpdMasterDao {
	String databaseScema = HMSUtil.getProperties("adt.properties", "currentSchema").trim();


	@Autowired
	GetHibernateUtils getHibernateUtils;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<MasDepartment> getDepartmentList(HashMap<String, Object> map) {
        Long departmentTypeId=null;
		String hID = (String)map.get("hospitalID");
		Long hospitalID = Long.parseLong(hID);	
		String departmentTypeCode = HMSUtil.getProperties("adt.properties", "DEPARTMENT_TYPE_CODE").trim();
		 if(departmentTypeCode!=null && departmentTypeCode!="")
			{	
				MasDepartment mss=null;
				mss = getDepartmentCode(departmentTypeCode);
				departmentTypeId=mss.getDepartmentId();
				System.out.println("departmentTypeId :::::::" +departmentTypeId);
			}
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();			
			//list = session.createSQLQuery("select m.department_name, m.department_id from mas_department m join mas_department_type mt on m.DEPARTMENT_TYPE_ID = mt.DEPARTMENT_TYPE_ID where m.STATUS = 'y'	and mt.DEPARTMENT_TYPE_CODE = 'OPD' order by department_name asc").list();
			List<MasDepartment> departmentList = session.createCriteria(MasDepartment.class)	
					.add(Restrictions.eq("status", "Y").ignoreCase())
					.createAlias("masDepartmentType", "dt")
					.add(Restrictions.eq("dt.departmentTypeId",departmentTypeId))
					.addOrder(Order.asc("departmentName"))
					.list();
			System.out.println("size is "+departmentList.size());
		    getHibernateUtils.getHibernateUtlis().CloseConnection();
		    
			return departmentList;
	}

	@Override
	public Users checkEmp(Long i) {

		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Users.class);
		cr.add(Restrictions.eq("userId", i));
		Users list = (Users) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public Users checkUser(Long i) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(Users.class);
		cr.add(Restrictions.eq("userId", i));
		Users list = (Users) cr.uniqueResult();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<MasIcd> getIcd(HashMap<String, String> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasIcd.class);
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
		if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
			String icdName=jsondata.get("icdName");
			cr.add(Restrictions.ilike("icdName", "%"+icdName+"%"));	
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("icdId").as("icdId"));
		projectionList.add(Projections.property("icdCode").as("icdCode"));
		projectionList.add(Projections.property("icdName").as("icdName"));
		projectionList.add(Projections.property("communicableFlag").as("communicableFlag"));
		projectionList.add(Projections.property("infectionsFlag").as("infectionsFlag"));

		cr.setProjection(projectionList);
		if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
			cr.setFirstResult(0);
			cr.setMaxResults(30);
		}
		List<MasIcd> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<DgMasInvestigation> getInvestigationList(String mainChargeCode,String doctorId,HashMap<String, String> jsondata) {
		Long mainChargeId=null;
		System.out.println("mainChargeCode "+mainChargeCode);
		System.out.println("DoctorIdddddddddddddddddddddd>>>>>>>>>>>>>>>>>>:::::::"+doctorId);
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		if(mainChargeCode!=null)
			{	
				MasMainChargecode mmcc=null;
				mmcc = getMainChargeCode(mainChargeCode);
				mainChargeId=mmcc.getMainChargecodeId();
				System.out.println("mainChargeId :" +mainChargeId);
			}
		Criteria cr = session.createCriteria(DgMasInvestigation.class);
		cr.add(Restrictions.eq("mainChargecodeId", mainChargeId));
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
		if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
			String icdName=jsondata.get("icdName");
			cr.add(Restrictions.ilike("investigationName", "%"+icdName+"%"));	
		
		}
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("investigationId").as("investigationId"));
		projectionList.add(Projections.property("investigationName").as("investigationName"));
		projectionList.add(Projections.property("investigationType").as("investigationType"));
		// projectionList.add(Projections.property("icdName").as("icdName"));

		cr.setProjection(projectionList);
		if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
			cr.setFirstResult(0);
			cr.setMaxResults(30);
		}
		List<DgMasInvestigation> list = cr
				.setResultTransformer(new AliasToBeanResultTransformer(DgMasInvestigation.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;

	}

	@Override
	public List<MasStoreItem> getMasStoreItem(HashMap<String, String> jsondata) {
		  Long itemId = null;
		  Long sectionId=null;
		  Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		/*
		 * String itemTypeCodePvms=HMSUtil.getProperties("adt.properties",
		 * "itemTypeCodePvms").trim(); if(itemTypeCodePvms!=null &&
		 * itemTypeCodePvms!="") { MasItemType mty=null; mty =
		 * getItemTypeId(itemTypeCodePvms); itemId=mty.getItemTypeId();
		 * System.out.println("itemTypeId" +itemId); }
		 */
		/*
		 * String sectionCode=HMSUtil.getProperties("adt.properties",
		 * "sectionCode").trim(); if(sectionCode!=null && sectionCode!="") {
		 * MasStoreSection mss=null; mss = getSectionId(sectionCode);
		 * sectionId=mss.getSectionId(); System.out.println("sectionId :::::::"
		 * +sectionId); }
		 */
		  String typeOfItem="E";
		  Criteria cr = session.createCriteria(MasStoreItem.class,"masStoreItem");
		  cr.add(Restrictions.eq("status", "Y").ignoreCase());
		  
		  cr=cr.createAlias("masStoreItem.masStoreUnit", "mh");
		  cr=cr.createAlias("masStoreItem.masStoreUnit1", "mc");
		
		  if(jsondata.containsKey("icdName") && jsondata.get("icdName")!=null) {
			  String nameNomenclature=jsondata.get("icdName");
			  Criterion byName1=Restrictions.ilike("masStoreItem.nomenclature",   "%"+nameNomenclature+"%");
				Criterion byCode1=Restrictions.ilike("masStoreItem.pvmsNo",   "%"+nameNomenclature+"%");
				LogicalExpression byNameCode1 = Restrictions.or(byName1, byCode1);
				cr.add(byNameCode1);
				
		  }
		  
		  /*
		  // Added one condition for non edl restriction in create indent page
		  if(jsondata.containsKey("flag") && jsondata.get("flag")!=null && jsondata.get("flag").equalsIgnoreCase("disp")) {			  
			  
			  cr.add(Restrictions.or(
					    Restrictions.eq("edl", "n").ignoreCase(),
					    Restrictions.eq("edl", "y").ignoreCase()
					));
			  
		  }
		  else {			  
			  
			  cr.add(Restrictions.eq("edl", "y").ignoreCase());
			  
			  
		  }
		  */
		  ProjectionList projectionList = Projections.projectionList();
		  projectionList.add(Projections.property("masStoreItem.itemId").as("itemId"));
		  projectionList.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"));
		  projectionList.add(Projections.property("masStoreItem.nomenclature").as("nomenclature"));
		  projectionList.add(Projections.property("mh.storeUnitName").as("dispUnitId"));
		  projectionList.add(Projections.property("mc.storeUnitName").as("itemUnitId")); //set store unit id(Accounting  unit
		  projectionList.add(Projections.property("masStoreItem.itemClassId").as("itemClassId"));
		  projectionList.add(Projections.property("masStoreItem.dispUnitId").as("dispUnitIdVal"));
		  projectionList.add(Projections.property("masStoreItem.frequencyId").as("frequencyId"));
		  projectionList.add(Projections.property("masStoreItem.dosage").as("dosage"));
		  projectionList.add(Projections.property("masStoreItem.noOfDays").as("noOfDays"));
		  if(jsondata.containsKey("icdName")&& jsondata.get("icdName")!="") {
				cr.setFirstResult(0);
				cr.setMaxResults(30);
			}
		  cr.setProjection(projectionList); 
		  @SuppressWarnings("unchecked")
		  List<MasStoreItem> list = cr.list();
		  
		  System.out.println("list="+list);
		  System.out.println("name="+jsondata.get("icdName"));
		  
		  getHibernateUtils.getHibernateUtlis().CloseConnection(); 
		  return list;
		 
	}

	@Override
	public List<MasFrequency> getMasFrequency() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasFrequency.class);
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
		cr.addOrder(Order.asc("orderNo"));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("frequencyId").as("frequencyId"));
		//projectionList.add(Projections.property("frequencyCode").as("frequencyCode"));
		projectionList.add(Projections.property("frequencyName").as("frequencyName"));
		projectionList.add(Projections.property("feq").as("feq"));

		cr.setProjection(projectionList);
		List<MasFrequency> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasFrequency.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<OpdTemplate> getTemplateName(String templateType,Long doctorId) {
     	Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(OpdTemplate.class);
		cr.add(Restrictions.eq("doctorId", doctorId));
		ProjectionList projectionList = Projections.projectionList();
		cr.add(Restrictions.eq("templateType", templateType));
		projectionList.add(Projections.property("templateId").as("templateId"));
		projectionList.add(Projections.property("templateName").as("templateName"));
		cr.setProjection(projectionList);
		List<OpdTemplate> list = cr.setResultTransformer(new AliasToBeanResultTransformer(OpdTemplate.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	@Override
	public List<MasEmpanelledHospital> getEmpanelledHospital(HashMap<String, String> jsondata) {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Long cityId=(Long.parseLong(String.valueOf(jsondata.get("cityId"))));
		Criteria cr = session.createCriteria(MasEmpanelledHospital.class);
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
		cr.add(Restrictions.eq("cityId", cityId));
		//cr.add(Restrictions.ilike("hospitalId", "%" +hosId+"%",MatchMode.ANYWHERE));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("empanelledHospitalId").as("empanelledHospitalId"));
	//	projectionList.add(Projections.property("empanelledHospitalCode").as("empanelledHospitalCode"));
		projectionList.add(Projections.property("empanelledHospitalName").as("empanelledHospitalName"));

		cr.setProjection(projectionList);
		List<MasEmpanelledHospital> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasEmpanelledHospital.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}
	
	

	

	@Override
	public Map<String, Object> getMasNursingCare(HashMap<String, Object> jsonData) {
		List<MasNursingCare> list = null;
		String nursingType = (String) jsonData.get("nursingType");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasNursingCare.class).add(Restrictions.eq("nursingType", "M"));
			
			if(jsonData.containsKey("icdName")&& jsonData.get("icdName")!="") {
				String icdName=(String) jsonData.get("icdName");
				cr.add(Restrictions.ilike("nursingName", "%"+icdName+"%"));	
				
				cr.setFirstResult(0);
				cr.setMaxResults(30);
			}
			list = cr.list();
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getTemplateInvestigation(HashMap<String, Object> jsonData) {
		List<OpdTemplateInvestigation> list = null;
		long templateId = Long.parseLong((String) jsonData.get("templateId"));
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdTemplateInvestigation.class)
					.add(Restrictions.eq("templateId", templateId));
			list = cr.list();
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getTemplateTreatment(HashMap<String, Object> jsonData) {
		List<OpdTemplateTreatment> list = null;
		long templateId = Long.parseLong((String) jsonData.get("templateId"));
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdTemplateTreatment.class)
					.add(Restrictions.eq("templateId", templateId));
			list = cr.list();
			map.put("list", list);
			getHibernateUtils.getHibernateUtlis().CloseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * Call stored procedure: Kaushal Mishra
	 * 
	 * @Override public Map<String, Object> executeDbProcedure(long hospitalId) {
	 * Map<String, Object> mapObj = new HashMap<String, Object>(); boolean
	 * exeFlag=false; List unitlist = new ArrayList(); try{ Session session =
	 * getHibernateUtils.getHibernateUtlis().OpenSession();
	 * 
	 * Query query =session.createSQLQuery("CALL Asp_Hierarchy_Data (:param1)")
	 * .addEntity(TempHierarchy.class) .setLong("param1", hospitalId);
	 * 
	 * 
	 * unitlist =query.list(); exeFlag =true; mapObj.put("exeFlag", exeFlag);
	 * mapObj.put("unitlist", unitlist); }catch(Exception e) { e.printStackTrace();
	 * }finally { getHibernateUtils.getHibernateUtlis().CloseConnection(); } return
	 * mapObj; }
	 */

	/* Call stored procedure: Kaushal Mishra */
	@Override
	public Map<String, Object> executeDbProcedure(long hospitalId,long userId)
	{
		Map<String, Object> mapObj = new HashMap<String, Object>();
		boolean exeFlag=false;
		 List<HierarchyData> unitlist=new ArrayList<HierarchyData>(); 
		
		try{
			
			  Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 
			  Query query =session.createSQLQuery("CALL Asp_Hierarchy_Data_Show (:param1,:param2)")
					    .addEntity(HierarchyData.class) 
			            .setLong("param1", userId)
			            .setLong("param2", hospitalId);
			  query.executeUpdate();

			unitlist=session.createCriteria(HierarchyData.class)
					.add(Restrictions.eq("userId", userId))
					.add(Restrictions.eq("userHospitalId", hospitalId))
					.list();
			
		exeFlag =true;
		mapObj.put("exeFlag", exeFlag);
		mapObj.put("unitlist", unitlist);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			 getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		return mapObj;
	}
	
	
	@Override
	public Map<String, Object> executeDbProcedureforStatistics(long userhospitalId, long combohospitalId, long userId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		boolean exeFlag=false;
		 List<HierarchyData> unitlist=new ArrayList<HierarchyData>(); 
		
		try{
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			 
			 session.doWork(new Work() {
			  @Override
			  public void execute(java.sql.Connection connection) throws SQLException {
			   System.out.println("connection is "+connection);
			   CallableStatement call = connection.prepareCall("CALL Asp_Main_DashBoard(?, ?, ?,?)");
			   call.setLong(1, userhospitalId);
			   call.setLong(2, combohospitalId);
			   call.setLong(3, userId);
			 //  call.registerOutParameter(4, OracleTypes.CURSOR);
			   call.execute();
			   ResultSet rs = (ResultSet)call.getObject(4);
			   String s = call.getObject(4)+"";
			   JSONArray jsonArray = new JSONArray();
			   while(rs.next()) {   
				   
				   System.out.println("1="+rs.getString(1));
				   System.out.println("2="+rs.getString("Total_opd"));
				   JSONObject obj = new JSONObject();
				   obj.put("opd", rs.getString("Total_opd") !=null?rs.getString("Total_opd"):"0");
				   obj.put("attc", rs.getString("Att_c") !=null?rs.getString("Att_c"):"0");
				   obj.put("me", rs.getString("Total_me") !=null?rs.getString("Total_me"):"0");
				   obj.put("mb", rs.getString("Total_mb") !=null?rs.getString("Total_mb"):"0");
			    
			             map.put("stat_summary", obj);
			   }
			  }
			 });
			 
			 System.out.println("map"+map);
			     
			
		}catch(Exception e)
		{
			System.out.println("Exception in procedure"+e);
		}
		finally {
			 getHibernateUtils.getHibernateUtlis().CloseConnection();
		}
		 return map;
	}
	
	@Override
	public List<MasItemClass> getMasItemClass() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasItemClass.class);
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
	
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("itemClassId").as("itemClassId"));
		projectionList.add(Projections.property("itemClassCode").as("itemClassCode"));
		projectionList.add(Projections.property("itemClassName").as("itemClassName"));
		
		cr.setProjection(projectionList);
		List<MasItemClass> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasItemClass.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}
	
	@Override
	public List<MasStoreUnit> getMasStoreUnit() {
		Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		Criteria cr = session.createCriteria(MasStoreUnit.class);
		cr.add(Restrictions.eq("status", "Y").ignoreCase());
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("storeUnitId").as("storeUnitId"));
		projectionList.add(Projections.property("storeUnitName").as("storeUnitName"));
		cr.setProjection(projectionList);
		List<MasStoreUnit> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasStoreUnit.class)).list();
		getHibernateUtils.getHibernateUtlis().CloseConnection();
		return list;
	}

	
	@Override
	public List<MasStoreItem> getMasStoreItemNip(HashMap<String, String> jsondata) {
		Long itemTypeId=null;
		String itemTypeCodeNip=HMSUtil.getProperties("adt.properties", "itemTypeCodeNIP").trim();
		  if(itemTypeCodeNip!=null && itemTypeCodeNip!="")
			{	
				MasItemType mty=null;
				mty = getItemTypeId(itemTypeCodeNip);
				itemTypeId=mty.getItemTypeId();
				
				System.out.println("itemTypeIdNIP " +itemTypeId);
			}	
		  Long hospitalId=Long.parseLong(jsondata.get("hospitalId"));
		  Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
		  Criteria cr = session.createCriteria(MasStoreItem.class,"masStoreItem");
		  
		  if(jsondata.containsKey("lpFlag") && jsondata.get("lpFlag").toString().equalsIgnoreCase("lp")) {
			  cr = cr.createAlias("masStoreItem.masStoreUnit1", "mh");
		  }
			
		  
		  cr.add(Restrictions.eq("itemTypeId", itemTypeId));
		  cr.add(Restrictions.eq("hospitalId", hospitalId));
		  if(jsondata.containsKey("icdName") && jsondata.get("icdName")!=null) {
			  String nameNomenclatureniv=jsondata.get("icdName").toString();
			  cr.add(Restrictions.ilike("masStoreItem.nomenclature","%"+ nameNomenclatureniv+"%"));

		  }
		  
		  ProjectionList projectionList = Projections.projectionList();
		  projectionList.add(Projections.property("masStoreItem.itemId").as("itemId"));
		  projectionList.add(Projections.property("masStoreItem.pvmsNo").as("pvmsNo"));
		  projectionList.add(Projections.property("masStoreItem.nomenclature").as("nomenclature"));
		  
		  if(jsondata.containsKey("lpFlag") && jsondata.get("lpFlag").toString().equalsIgnoreCase("lp")) {
			  projectionList.add(Projections.property("mh.storeUnitId").as("itemUnitId"));
		  }
		 
		  
		  if(jsondata.containsKey("icdName") && jsondata.get("icdName")!=null) {
			  cr.setFirstResult(0);
			  cr.setMaxResults(30);
		  }
		  cr.setProjection(projectionList); 
		  List<MasStoreItem> list = cr.setResultTransformer(new  AliasToBeanResultTransformer(MasStoreItem.class)).list();
		  getHibernateUtils.getHibernateUtlis().CloseConnection(); 
		  return list;
		 
	}
	
	
	//rajdeo

		@Override
		public Map<String, Object> executeProcedureForDashBoard(Map<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
				Map<String,Object> map = new HashMap<String, Object>();
				JSONObject object = new JSONObject();
				JSONArray jsonArray1 = new JSONArray();
				JSONArray jsonArray2 = new JSONArray();
				JSONArray jsonArray3 = new JSONArray();
				JSONArray jsonArray4 = new JSONArray();
				JSONArray jsonArray5 = new JSONArray();
				JSONArray jsonArray6 = new JSONArray();
				try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();	
				session.doWork(new Work() {
					
					@Override
					public void execute(Connection connection) throws SQLException {
						
						long hospitalId = Long.parseLong(jsondata.get("hospitalId").toString());
						long userHospitalId = Long.parseLong(jsondata.get("tUserHospitalId").toString());
						long iOption = Long.parseLong(jsondata.get("iOption").toString());
						long mbValue = Long.parseLong(jsondata.get("mbVal").toString());
						long meValue = Long.parseLong(jsondata.get("meVal").toString());
					
					
					 
						String sss="{ ? = call "+databaseScema+".Asp_Hierarchy_Data_new( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) }";
						//CallableStatement cstatement = connection.prepareCall("CALL Asp_Hierarchy_Data_new(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
						CallableStatement cstatement = connection.prepareCall(sss);
						cstatement.setLong(1, hospitalId);
						cstatement.setLong(2, userHospitalId);
						cstatement.setLong(3, iOption);
						cstatement.setLong(4, mbValue);
						cstatement.setLong(5, meValue);
						//cstatement.setDate(3, fromDate_d1);
						//cstatement.setDate(4, toDate_d1);
						
						//output parameteres
						/*cstatement.registerOutParameter(6, OracleTypes.CURSOR);
						cstatement.registerOutParameter(7, OracleTypes.CURSOR);
						cstatement.registerOutParameter(8, OracleTypes.CURSOR);
						cstatement.registerOutParameter(9, OracleTypes.CURSOR);
						cstatement.registerOutParameter(10, OracleTypes.CURSOR);
						cstatement.registerOutParameter(11, OracleTypes.CURSOR)*/;
						cstatement.executeQuery();
						ResultSet rs1 = (ResultSet)cstatement.getObject(6);
						ResultSet rs2 = (ResultSet)cstatement.getObject(7);
						ResultSet rs3 = (ResultSet)cstatement.getObject(8);
						ResultSet rs4 = (ResultSet)cstatement.getObject(9);
						ResultSet rs5 = (ResultSet)cstatement.getObject(10);
						ResultSet rs6 = (ResultSet)cstatement.getObject(11);
						while(rs1.next()) {
							//System.out.println(rs1.getString(1)+" "+rs1.getString(2)+" "+rs1.getString(3));
							int columnCount = rs1.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();

						      for (int i=0; i<columnCount; i++) {
						    	  jsonObj.put(rs1.getMetaData().getColumnLabel(i+1), rs1.getObject(i+1));
						      }
						      jsonArray1.put(jsonObj);
						}
						object.put("ref_cursor1", jsonArray1);
						
						while(rs2.next()) {
							//System.out.println(rs2.getString(1)+" "+rs2.getString(2)+" "+rs2.getString(3)); 
							int columnCount = rs2.getMetaData().getColumnCount();
							JSONObject jsonObj = new JSONObject();
							for(int i=0; i<columnCount;i++) {						
								jsonObj.put(rs2.getMetaData().getColumnLabel(i+1), rs2.getObject(i+1));
							}					
							jsonArray2.put(jsonObj);
						}
						object.put("ref_cursor2", jsonArray2);
						
						while(rs3.next()) {
							//System.out.println(rs3.getString(1)+" "+rs3.getString(2)+" "+rs3.getString(3)+" "+rs3.getString(4));
							JSONObject jsonObj = new JSONObject();
							int columnCount = rs3.getMetaData().getColumnCount();
							for(int i=0;i<columnCount;i++) {
								jsonObj.put(rs3.getMetaData().getColumnLabel(i+1), rs3.getObject(i+1));
							}
							jsonArray3.put(jsonObj);
						}
						object.put("ref_cursor3", jsonArray3);
						
						while(rs4.next()) {
							//System.out.println(rs3.getString(1)+" "+rs3.getString(2)+" "+rs3.getString(3)+" "+rs3.getString(4));
							JSONObject jsonObj = new JSONObject();
							int columnCount = rs4.getMetaData().getColumnCount();
							for(int i=0;i<columnCount;i++) {
								jsonObj.put(rs4.getMetaData().getColumnLabel(i+1), HMSUtil.convertNullToEmptyString(rs4.getObject(i+1)));
							}
							jsonArray4.put(jsonObj);
						}
						object.put("ref_cursor4", jsonArray4);
						
						while(rs5.next()) {
							JSONObject jsonObj = new JSONObject();
							int columnCount = rs5.getMetaData().getColumnCount();
							for(int i=0;i<columnCount;i++) {
								jsonObj.put(rs5.getMetaData().getColumnLabel(i+1), HMSUtil.convertNullToEmptyString(rs5.getObject(i+1)));
							}
							jsonArray5.put(jsonObj);
						}
						object.put("ref_cursor5", jsonArray5);
						
						while(rs6.next()) {
							//System.out.println(rs3.getString(1)+" "+rs3.getString(2)+" "+rs3.getString(3)+" "+rs3.getString(4));
							JSONObject jsonObj = new JSONObject();
							int columnCount = rs6.getMetaData().getColumnCount();
							for(int i=0;i<columnCount;i++) {
								jsonObj.put(rs6.getMetaData().getColumnLabel(i+1), rs6.getObject(i+1));
							}
							jsonArray6.put(jsonObj);
						}
						object.put("ref_cursor6", jsonArray6);
						
					}
				}); 
					
				map.put("dashboardData", object);
				
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					getHibernateUtils.getHibernateUtlis().CloseConnection();
				}
			return map;
	}

		@Override
		public String saveOpdNursingCareDetails(HashMap<String, Object> jsondata) {
			// TODO Auto-generated method stub
			Date currentDate = ProjectUtils.getCurrentDate();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(OpdPatientDetail.class);
			Transaction tx = session.beginTransaction();
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			try {
			if (jsondata.get("listofProcedureMaster") != null && jsondata.get("listofProcedureMaster")!="") {
				 List<HashMap<String, Object>> listofOpdProcedure = (List<HashMap<String, Object>>) jsondata.get("listofProcedureMaster");
				 if (listofOpdProcedure != null) {
				 {
				 for (HashMap<String, Object> singleNipopd: listofOpdProcedure)
				 {
					 
				  MasNursingCare msit=new MasNursingCare();
				  Random rand = new Random(); 
				  int str=rand.nextInt(10000);
				  String pcode=(String.valueOf(str));
				  System.out.println(pcode);
				  msit.setNursingCode(pcode);
				  msit.setNursingName(String.valueOf(singleNipopd.get("procedureName")));
				  msit.setDefaultstatus("Y");
				  msit.setStatus("Y");
				  msit.setLastChgDate(ourJavaTimestampObject);
				  msit.setNursingType(String.valueOf(singleNipopd.get("procedureTypes")));
				 session.save(msit);
				
				}
				
				 }
		 
				 }
			
		}
			
		 }catch (Exception ex) {
				
				//System.out.println("Exception e="+ex.);
				ex.printStackTrace();
				tx.rollback();
				return "Error while saving records";
			} finally {
				tx.commit();
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}


		return "200";
		}
		
		@Override
		public String saveEmpanlledHospitalDetails(HashMap<String, Object> jsondata) {
			// TODO Auto-generated method stub
			Date currentDate = ProjectUtils.getCurrentDate();
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Transaction tx = session.beginTransaction();
						
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
			try {
			if (jsondata.get("listofEmpanlledHospital") != null && jsondata.get("listofEmpanlledHospital")!="") {
				 List<HashMap<String, Object>> listofOpdProcedure = (List<HashMap<String, Object>>) jsondata.get("listofEmpanlledHospital");
				 if (listofOpdProcedure != null) {
				 {
				 for (HashMap<String, Object> singleNipopd: listofOpdProcedure)
				 {
					 
				  MasEmpanelledHospital mehList=new MasEmpanelledHospital();
				  Random rand = new Random(); 
				  int str=rand.nextInt(10000); 
				 
				  mehList.setEmpanelledHospitalName(String.valueOf(singleNipopd.get("empanlledName")));
				  mehList.setEmpanelledHospitalAddress(String.valueOf(singleNipopd.get("empanlleAddress")));
				  //mehList.setHospitalId(jsondata.get("hospitalId").toString());
				  mehList.setLastchgBy(Long.parseLong(jsondata.get("doctorId").toString()));
				  mehList.setCityId(Long.parseLong(jsondata.get("cityId").toString()));
				  mehList.setStatus("Y");
				  mehList.setLastChgDate(ourJavaTimestampObject);
				  Long cityId = Long.parseLong((String.valueOf(jsondata.get("cityId"))));
				  Criteria cr = session.createCriteria(MasEmpanelledHospital.class);
				   cr.add(Restrictions.eq("empanelledHospitalName", singleNipopd.get("empanlledName")));
				   cr.add(Restrictions.eq("cityId", cityId));
				   MasEmpanelledHospital opdEmpaneName = (MasEmpanelledHospital) cr.uniqueResult();
				   if(opdEmpaneName!=null) {
					
					return "Empanlled Hospital Name Already Exists";
				   }
				  
				  
				  session.save(mehList);
				
				}
				
				 }
		 
				 }
			
		}
			
		 }catch (Exception ex) {
				
				//System.out.println("Exception e="+ex.);
				ex.printStackTrace();
				tx.rollback();
				return "Error while saving records";
			} finally {
				tx.commit();
				getHibernateUtils.getHibernateUtlis().CloseConnection();
			}


		return "200";
		}
		
		@Override
		public List<MasHospital> getHospitalList() {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasHospital.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("hospitalId").as("hospitalId"));
			projectionList.add(Projections.property("hospitalName").as("hospitalName"));
			cr.setProjection(projectionList);
			List<MasHospital> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasHospital.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return list;
		}
		@Override
		public MasItemType getItemTypeId(String itemTypeCode)
		{
			Session session =null;
			MasItemType itemTypeId=null;
			try {
				session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(MasItemType.class)
						.add(Restrictions.eq("itemTypeCode", itemTypeCode));
				List<MasItemType>listInvHd=cr.list();
				if(CollectionUtils.isNotEmpty(listInvHd)) {
					itemTypeId=listInvHd.get(0);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			return itemTypeId;
		}
		@Override
		public MasStoreSection getSectionId(String sectionCode)
		{
			Session session =null;
			MasStoreSection sectionId=null;
			try {
				session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(MasStoreSection.class)
						.add(Restrictions.eq("sectionCode", sectionCode));
				List<MasStoreSection>listInvHd=cr.list();
				if(CollectionUtils.isNotEmpty(listInvHd)) {
					sectionId=listInvHd.get(0);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			return sectionId;
		}
		
		public MasDepartment getDepartmentCode(String departmentCode)
		{
			Session session =null;
			MasDepartment departmentId=null;
			try {
				session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(MasDepartment.class)
						.add(Restrictions.eq("departmentCode", departmentCode));
				List<MasDepartment>listInvHd=cr.list();
				if(CollectionUtils.isNotEmpty(listInvHd)) {
					departmentId=listInvHd.get(0);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			return departmentId;
		}
		
		public MasMainChargecode getMainChargeCode(String mainChargeCode)
		{
			Session session =null;
			MasMainChargecode mainChargeId=null;
			try {
				session=getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(MasMainChargecode.class)
						.add(Restrictions.eq("mainChargecodeCode", mainChargeCode));
				List<MasMainChargecode>listInvHd=cr.list();
				if(CollectionUtils.isNotEmpty(listInvHd)) {
					mainChargeId=listInvHd.get(0);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			return mainChargeId;
		}

		@Override
		public List<MasSpeciality> getSpecialistList(HashMap<String, Object> map) {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasSpeciality.class);
			cr.add(Restrictions.eq("status", "Y").ignoreCase());
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("specialityCode").as("specialityCode"));
			projectionList.add(Projections.property("specialityId").as("specialityId"));
			projectionList.add(Projections.property("specialityName").as("specialityName"));
			cr.setProjection(projectionList);
			List<MasSpeciality> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasSpeciality.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return list;
		}
		
		
		
		@SuppressWarnings("unchecked")
		//@Override
		public List<MasIcd> getIcdByName___(String icdName) {
			
			List<MasIcd> list = new ArrayList<MasIcd>();
			List<MasIcd> listA = new ArrayList<MasIcd>();
			List<MasIcd> listB = new ArrayList<MasIcd>();
			
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			
			Criteria cr = session.createCriteria(MasIcd.class);
			Criterion byName=Restrictions.ilike("icdName", icdName+"%");
			Criterion byCode=Restrictions.ilike("icdCode", icdName+"%");
			LogicalExpression byNameCode = Restrictions.or(byName, byCode);
			cr.add(byNameCode);
			cr.add(Restrictions.eq("status", "Y").ignoreCase());
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("icdId").as("icdId"));
			projectionList.add(Projections.property("icdCode").as("icdCode"));
			projectionList.add(Projections.property("icdName").as("icdName"));
				cr.setFirstResult(0);
				cr.setMaxResults(10);
			cr.setProjection(projectionList);
			listA = cr.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
			
			if(listA.size()>0 && listA.size()<10) {
				Criteria crit = session.createCriteria(MasIcd.class);
				Criterion byNameiLike=Restrictions.ilike("icdName", "%"+icdName+"%");
				Criterion byCodeiLike=Restrictions.ilike("icdCode", "%"+icdName+"%");
				LogicalExpression byNameCodeiLike = Restrictions.or(byNameiLike, byCodeiLike);
				crit.add(byNameCodeiLike);
				crit.add(Restrictions.eq("status", "Y").ignoreCase());
				ProjectionList projectionListiLike = Projections.projectionList();
				projectionListiLike.add(Projections.property("icdId").as("icdId"));
				projectionListiLike.add(Projections.property("icdCode").as("icdCode"));
				projectionListiLike.add(Projections.property("icdName").as("icdName"));
					crit.setFirstResult(0);
					crit.setMaxResults(30);
				crit.setProjection(projectionListiLike);
				listB = crit.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
				
				for (int i = 0; i < listB.size(); i++) {
					for (int j = 0; j < listA.size(); j++) {
						if ((listB.get(i).getIcdId()).equals(listA.get(j).getIcdId())) {
							listB.remove(listB.get(i));
						}
					}
				}
			   
			}
			
			list=ListUtils.union(listA, listB);
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return list;
		}
		
		
		//@Override
		public List<MasIcd> getIcdByName_ongonig(String icdName) {
			List<MasIcd> listA = null;
			List<MasIcd> listB = null;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasIcd.class);
			Criterion byName=Restrictions.ilike("icdName",  icdName+"%");
			Criterion byCode=Restrictions.ilike("icdCode",  icdName+"%");
			LogicalExpression byNameCode = Restrictions.or(byName, byCode);
			cr.add(byNameCode);
			cr.add(Restrictions.eq("status", "Y").ignoreCase());
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("icdId").as("icdId"));
			projectionList.add(Projections.property("icdCode").as("icdCode"));
			projectionList.add(Projections.property("icdName").as("icdName"));
				cr.setFirstResult(0);
				cr.setMaxResults(10);
				cr.setProjection(projectionList);
			  listA = cr.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
			
			if(listA.size()>0 && listA.size()<10) {
				Criteria cr1 = session.createCriteria(MasIcd.class);
		  		Criterion byNameiLike=Restrictions.ilike("icdName", "%"+icdName+"%");
				Criterion byCodeiLike=Restrictions.ilike("icdCode", "%"+icdName+"%");
				LogicalExpression byNameCodeiLike = Restrictions.or(byNameiLike, byCodeiLike);
				cr1.add(byNameCodeiLike);
				cr1.add(Restrictions.eq("status", "Y").ignoreCase());
				ProjectionList projectionList1 = Projections.projectionList();
				projectionList1.add(Projections.property("icdId").as("icdId"));
				projectionList1.add(Projections.property("icdCode").as("icdCode"));
				projectionList1.add(Projections.property("icdName").as("icdName"));
					cr1.setFirstResult(0);
					cr1.setMaxResults(30);
					cr1.setProjection(projectionList1);
				listB = cr1.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
			 }
			if(CollectionUtils.isNotEmpty(listB))
				 listA.addAll(listB);
			List<MasIcd>newList=listA.stream().distinct().collect(Collectors.toList());
			
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return newList;
		}
		
		
		@Override
		public List<MasIcd> getIcdByName(String icdName) {
			List<MasIcd> listA = null;
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasIcd.class);
			Criterion byName1=Restrictions.ilike("icdName",   "%"+icdName+"%");
			Criterion byCode1=Restrictions.ilike("icdCode",   "%"+icdName+"%");
			LogicalExpression byNameCode1 = Restrictions.or(byName1, byCode1);
			cr.add(byNameCode1);
			cr.add(Restrictions.eq("status", "Y").ignoreCase());
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("icdId").as("icdId"));
			projectionList.add(Projections.property("icdCode").as("icdCode"));
			projectionList.add(Projections.property("icdName").as("icdName"));
			projectionList.add(Projections.property("communicableFlag").as("communicableFlag"));
			projectionList.add(Projections.property("infectionsFlag").as("infectionsFlag"));
				cr.setFirstResult(0);
				cr.setMaxResults(110);
				cr.setProjection(projectionList);
			  listA = cr.setResultTransformer(new AliasToBeanResultTransformer(MasIcd.class)).list();
			  
			  String s1="",s2="";
			  if(StringUtils.isNotEmpty(icdName)){
			    s1=icdName.substring(0, 1);
			    s1=s1.toUpperCase();
			    if(icdName.length()>1) {
			    	s2=icdName.substring(1, icdName.length());
			    	}
			  }
			  String icdName1=s1.concat(s2);
			  Predicate<MasIcd>listPre=e->e.icdName.startsWith(icdName1)  ;  
			  List<MasIcd>newList=new ArrayList<>();
			  List<MasIcd>newList1=null;
			  listA.stream().filter(listPre).forEach(e->newList.add(e));
			  
			  if(newList.size()>0 && newList.size()<10) {
				  newList.addAll(listA);
			  }
			  if(newList.size()==0) {
				  newList.addAll(listA);  
			  }
			  newList1=newList.stream().limit(30).distinct().collect(Collectors.toList());
			  getHibernateUtils.getHibernateUtlis().CloseConnection();
			return newList1;
		}
		
		
		@Override
		public Map<String, Object> getTemplateMedicalAdvice(HashMap<String, Object> jsonData) {
			List<OpdTemplateMedicalAdvice> list = null;
			//long templateId = Long.parseLong((String) jsonData.get("templateId"));
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(OpdTemplateMedicalAdvice.class);
						//.add(Restrictions.eq("opdTemplateId", templateId));
				list = cr.list();
				map.put("list", list);
				getHibernateUtils.getHibernateUtlis().CloseConnection();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
		}
		
		@Override
		public List<Long> getTemplateInvestigationOp(HashMap<String, Object> jsonData) {
			//List<OpdTemplateInvestigation> list = null;
			List<Long>list=null;
			long templateId = Long.parseLong((String) jsonData.get("templateId"));
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
				Criteria cr = session.createCriteria(OpdTemplateInvestigation.class)
						.add(Restrictions.eq("templateId", templateId));
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.property("investigationId").as("investigationId"));
				//projectionList.add(Projections.property("templateInvestigationId").as("templateInvestigationId"));
				cr.setProjection(projectionList);
				cr.addOrder(Order.asc("templateInvestigationId"));
				list=cr.list();//cr.setResultTransformer(new AliasToBeanResultTransformer(OpdTemplateInvestigation.class)).list();
			//	list = cr.list();
				//map.put("list", list);
				getHibernateUtils.getHibernateUtlis().CloseConnection();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}

		@Override
		public List getTreatmentInstruction() {
			Session session = getHibernateUtils.getHibernateUtlis().OpenSession();
			Criteria cr = session.createCriteria(MasTreatmentInstruction.class);

			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("instructionsId").as("instructionsId"));
			projectionList.add(Projections.property("instructionsName").as("instructionsName"));
			cr.setProjection(projectionList);
			List<MasTreatmentInstruction> list = cr.setResultTransformer(new AliasToBeanResultTransformer(MasTreatmentInstruction.class)).list();
			getHibernateUtils.getHibernateUtlis().CloseConnection();
			return list;
		}



}